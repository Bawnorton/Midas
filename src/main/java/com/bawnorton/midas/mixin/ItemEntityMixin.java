package com.bawnorton.midas.mixin;

import com.bawnorton.midas.access.DataSaverAccess;
import com.bawnorton.midas.api.MidasApi;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin implements DataSaverAccess {
    @Shadow public abstract ItemStack getStack();

    private static final TrackedData<Boolean> IS_GOLD = DataTracker.registerData(ItemEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void initDataTracker(CallbackInfo ci) {
        ((ItemEntity) (Object) this).getDataTracker().startTracking(IS_GOLD, false);
    }

    @Override
    public boolean isGold() {
        return ((ItemEntity) (Object) this).getDataTracker().get(IS_GOLD);
    }

    @Override
    public void setGold(boolean gold) {
        ((ItemEntity) (Object) this).getDataTracker().set(IS_GOLD, gold);
        World world = ((ItemEntity) (Object) this).world;
        if(!world.isClient) {
            MidasApi.turnToGold(this.getStack());
        }
    }
}
