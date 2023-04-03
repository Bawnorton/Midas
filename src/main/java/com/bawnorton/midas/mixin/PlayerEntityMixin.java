package com.bawnorton.midas.mixin;

import com.bawnorton.midas.access.DataSaverAccess;
import com.bawnorton.midas.api.MidasApi;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements DataSaverAccess {
    private static final TrackedData<Boolean> IS_CURSED = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void initDataTracker(CallbackInfo ci) {
        ((PlayerEntity) (Object) this).getDataTracker().startTracking(IS_CURSED, false);
    }

    @Override
    public boolean isCursed() {
        return ((PlayerEntity) (Object) this).getDataTracker().get(IS_CURSED);
    }

    @Override
    public void setCursed(boolean cursed) {
        ((PlayerEntity) (Object) this).getDataTracker().set(IS_CURSED, cursed);
    }

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void tickMovement(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (!player.world.isClient && MidasApi.isCursed(player)) {
            Vec3d pos = player.getPos().add(0, 1, 0);

            Vec3d north = pos.add(0, 0, -0.5);
            Vec3d east = pos.add(0.5, 0, 0);
            Vec3d south = pos.add(0, 0, 0.5);
            Vec3d west = pos.add(-0.5, 0, 0);
            Vec3d northDown = north.add(0, -0.5, 0);
            Vec3d eastDown = east.add(0, -0.5, 0);
            Vec3d southDown = south.add(0, -0.5, 0);
            Vec3d westDown = west.add(0, -0.5, 0);
            Vec3d straightUp = pos.add(0, 0.85, 0);
            Vec3d straightDown = pos.add(0, -1.25, 0);

            List<Vec3d> positions = List.of(north, east, south, west, northDown, eastDown, southDown, westDown, straightUp, straightDown);

            positions.stream().collect(Collectors.toMap(vec3d -> vec3d, vec3d -> player.world.getBlockState(BlockPos.ofFloored(vec3d)))).entrySet().stream().filter(entry -> ((AbstractBlockAccessor) entry.getValue().getBlock()).isCollidable()).map(entry -> Map.entry(BlockPos.ofFloored(entry.getKey()), MidasApi.turnToGold(entry.getValue()))).forEach(entry -> {
                BlockPos blockPos = entry.getKey();
                BlockState blockState = entry.getValue();
                BlockEntity blockEntity = player.world.getBlockEntity(blockPos);
                MidasApi.turnToGold(blockEntity);
                if (blockEntity != null) return;
                player.world.setBlockState(blockPos, MidasApi.turnToGold(blockState), Block.NOTIFY_ALL);
            });
        }
    }
}
