package com.bawnorton.midas.mixin;

import com.bawnorton.midas.access.DataSaverAccess;
import com.bawnorton.midas.api.MidasApi;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin implements DataSaverAccess {
    @Shadow public abstract void setAiDisabled(boolean aiDisabled);

    @Inject(method = "isAiDisabled", at = @At("RETURN"), cancellable = true)
    private void isAiDisabled(CallbackInfoReturnable<Boolean> cir) {
        if(MidasApi.isGold(this)) {
            cir.setReturnValue(true);
        }
    }
}
