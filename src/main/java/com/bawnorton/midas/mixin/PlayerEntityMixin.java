package com.bawnorton.midas.mixin;

import com.bawnorton.midas.access.EntityAccess;
import com.bawnorton.midas.access.PlayerEntityAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityAccess {
    @Unique private static TrackedData<Boolean> IS_CURSED;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    public boolean isCursed() {
        return getDataTracker().get(IS_CURSED);
    }

    public void setCursed(boolean cursed) {
        this.getDataTracker().set(IS_CURSED, cursed);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("cursed", isCursed());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        setCursed(nbt.getBoolean("cursed"));
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void initDataTracker(CallbackInfo ci) {
        this.getDataTracker().startTracking(IS_CURSED, false);
    }

    @Inject(method = "collideWithEntity", at = @At("HEAD"))
    public void collideWithEntity(Entity entity, CallbackInfo ci) {
        if(isCursed() && !this.world.isClient) {
            if(entity instanceof PlayerEntityAccess playerEntityAccess) {
                playerEntityAccess.killedByGold((PlayerEntity) (Object) this);
            } else {
                ((EntityAccess) entity).turnToGold();
            }
        }
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void clinit(CallbackInfo ci) {
        IS_CURSED = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    public void attack(Entity target, CallbackInfo ci) {
        if(isCursed() && !this.world.isClient) {
            if(target instanceof PlayerEntityAccess access) {
                access.killedByGold((PlayerEntity) (Object) this);
            } else {
                ((EntityAccess) target).turnToGold();
            }
            ci.cancel();
        }
    }
}
