package com.bawnorton.midas.mixin;

import com.bawnorton.midas.access.EntityAccess;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityAccess {

    @Inject(method = "collidesWith", at = @At("RETURN"), cancellable = true)
    private void collidesWith(Entity other, CallbackInfoReturnable<Boolean> cir) {
        if(other instanceof EntityAccess livingEntity) {
            if(livingEntity.isGold()) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "isFireImmune", at = @At("RETURN"), cancellable = true)
    private void isFireImmune(CallbackInfoReturnable<Boolean> cir) {
        if(this.isGold()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "isSilent", at = @At("RETURN"), cancellable = true)
    private void isSilent(CallbackInfoReturnable<Boolean> cir) {
        if(this.isGold()) {
            cir.setReturnValue(true);
        }
    }

    @Override
    public void turnToGold() {
    }

    @Override
    public boolean isGold() {
        return false;
    }
}
