package com.bawnorton.midas.mixin;

import com.bawnorton.midas.access.EntityAccess;
import net.minecraft.entity.mob.CreeperEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin implements EntityAccess {
    @Inject(method = "explode", at = @At("HEAD"), cancellable = true)
    private void explode(CallbackInfo ci) {
        if(this.isGold()) {
            ci.cancel();
        }
    }
}
