package com.bawnorton.midas.mixin;

import com.bawnorton.midas.access.DataSaverAccess;
import com.bawnorton.midas.api.MidasApi;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin implements DataSaverAccess {
    private NbtCompound midasData;

    @Override
    public NbtCompound getMidasData() {
        if(midasData == null) {
            midasData = new NbtCompound();
        }
        return midasData;
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    private void writeNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        if(midasData != null) {
            nbt.put("midas", midasData);
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    private void readNbt(NbtCompound nbt, CallbackInfo ci) {
        if(nbt.contains("midas")) {
            midasData = nbt.getCompound("midas");
            if(midasData.getBoolean("gold")) {
                MidasApi.turnToGold((Entity) (Object) this);
            }
            if(midasData.getBoolean("cursed") && (Entity) (Object) this instanceof PlayerEntity player) {
                MidasApi.cursePlayer(player);
            }
        }
    }

    @Inject(method = "collidesWith", at = @At("RETURN"), cancellable = true)
    private void collidesWith(Entity other, CallbackInfoReturnable<Boolean> cir) {
        if(other instanceof LivingEntity && MidasApi.isGold(other)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "onPlayerCollision", at = @At("HEAD"))
    private void onPlayerCollision(PlayerEntity player, CallbackInfo ci) {
        if(MidasApi.isCursed(player)) {
            MidasApi.turnToGold((Entity) (Object) this);
        }
    }

    @Inject(method = "isFireImmune", at = @At("RETURN"), cancellable = true)
    private void isFireImmune(CallbackInfoReturnable<Boolean> cir) {
        if(MidasApi.isGold((Entity) (Object) this)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "isSilent", at = @At("RETURN"), cancellable = true)
    private void isSilent(CallbackInfoReturnable<Boolean> cir) {
        if(MidasApi.isGold((Entity) (Object) this)) {
            cir.setReturnValue(true);
        }
    }

    @Override
    public boolean isGold() {
        return false;
    }

    @Override
    public void setGold(boolean gold) {
    }
}
