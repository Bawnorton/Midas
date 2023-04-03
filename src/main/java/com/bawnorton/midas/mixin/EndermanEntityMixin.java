package com.bawnorton.midas.mixin;

import com.bawnorton.midas.api.MidasApi;
import net.minecraft.entity.mob.EndermanEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndermanEntity.class)
public abstract class EndermanEntityMixin  {
    @Inject(method = "teleportTo(DDD)Z", at = @At("HEAD"), cancellable = true)
    private void teleportRandomly(CallbackInfoReturnable<Boolean> cir) {
        if(MidasApi.isGold((EndermanEntity) (Object) this)) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
