package com.bawnorton.midas.mixin;

import com.bawnorton.midas.access.EntityAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements EntityAccess {
    @Shadow public abstract Box getVisibilityBoundingBox();
    @Unique
    private static TrackedData<Boolean> GOLD;

    @Override
    public void turnToGold() {
        setGold(true);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("gold", isGold());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        setGold(nbt.getBoolean("gold"));
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void initDataTracker(CallbackInfo ci) {
        ((Entity) (Object) this).getDataTracker().startTracking(GOLD, false);
    }

    @Override
    public void setGold(boolean gold) {
        ((Entity) (Object) this).getDataTracker().set(GOLD, gold);
    }

    @Override
    public boolean isGold() {
        return ((Entity) (Object) this).getDataTracker().get(GOLD);
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void clinit(CallbackInfo ci) {
        GOLD = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }

    @Inject(method = "dropLoot", at = @At("HEAD"), cancellable = true)
    private void dropLoot(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
        if(isGold()) {
            Box box = this.getVisibilityBoundingBox();
            double volume = box.getXLength() * box.getYLength() * box.getZLength();

            int i;
            for(i = (int) Math.max(Math.floor(volume * 9.0 * 9.0), 1); i >= 81; i -= 81) {
                ((LivingEntity) (Object) this).dropItem(Items.GOLD_BLOCK);
            }

            while(i >= 9) {
                ((LivingEntity) (Object) this).dropItem(Items.GOLD_INGOT);
                i -= 9;
            }

            while(i > 0) {
                ((LivingEntity) (Object) this).dropItem(Items.GOLD_NUGGET);
                --i;
            }
            ci.cancel();
        }
    }
}
