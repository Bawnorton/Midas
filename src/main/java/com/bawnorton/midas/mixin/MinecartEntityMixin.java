package com.bawnorton.midas.mixin;

import com.bawnorton.midas.access.DataSaverAccess;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecartEntity.class)
public abstract class MinecartEntityMixin implements DataSaverAccess {
    private static final TrackedData<Boolean> IS_GOLD = DataTracker.registerData(AbstractMinecartEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void initDataTracker(CallbackInfo ci) {
        ((AbstractMinecartEntity) (Object) this).getDataTracker().startTracking(IS_GOLD, false);
    }

    @Override
    public boolean isGold() {
        return ((AbstractMinecartEntity) (Object) this).getDataTracker().get(IS_GOLD);
    }

    @Override
    public void setGold(boolean gold) {
        ((AbstractMinecartEntity) (Object) this).getDataTracker().set(IS_GOLD, gold);
    }
}
