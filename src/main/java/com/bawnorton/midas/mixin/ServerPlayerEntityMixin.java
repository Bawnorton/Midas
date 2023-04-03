package com.bawnorton.midas.mixin;

import com.bawnorton.midas.access.ServerPlayerAccess;
import com.bawnorton.midas.api.MidasApi;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements ServerPlayerAccess {
    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Shadow public abstract boolean damage(DamageSource source, float amount);
    @Shadow public abstract ServerWorld getWorld();

    private int ticks = 0;
    private boolean diedToGold;
    private final Random random = Random.create();

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        ticks++;
        if(MidasApi.isCursed((ServerPlayerEntity) (Object) this) && ticks % 20 == 0) {
            EquipmentSlot[] slots = EquipmentSlot.values();
            EquipmentSlot slot = slots[random.nextInt(slots.length)];
            ItemStack stack = getEquippedStack(slot);
            this.equipStack(slot, MidasApi.turnToGold(stack));
        }
    }

    @Inject(method = "attack", at = @At("HEAD"))
    public void attack(Entity target, CallbackInfo ci) {
        if(MidasApi.isCursed((ServerPlayerEntity) (Object) this)) {
            MidasApi.turnToGold(target);
        }
        if(target instanceof PlayerEntity targetPlayer && MidasApi.isCursed(targetPlayer)) {
            MidasApi.turnToGold((ServerPlayerEntity) (Object) this);
        }
    }

    @Override
    public void dieToGold() {
        diedToGold = true;
    }

    @Override
    public boolean didDieToGold() {
        return diedToGold;
    }
}
