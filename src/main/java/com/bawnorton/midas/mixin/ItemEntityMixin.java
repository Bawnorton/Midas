package com.bawnorton.midas.mixin;

import com.bawnorton.midas.Midas;
import com.bawnorton.midas.access.EntityAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin implements EntityAccess {
    @Shadow public abstract void setStack(ItemStack stack);
    @Shadow public abstract ItemStack getStack();

    @Unique
    private static TrackedData<Boolean> GOLD;
    @Override
    public void turnToGold() {
        setStack(Midas.goldify(getStack()));
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
}
