package com.bawnorton.midas.mixin;

import com.bawnorton.midas.access.PlayerEntityAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DamageSource.class)
public abstract class DamageSourceMixin {
    @Shadow @Final @Nullable private Entity attacker;

    @Inject(method = "getDeathMessage", at = @At("HEAD"), cancellable = true)
    private void getDeathMessage(LivingEntity killed, CallbackInfoReturnable<Text> cir) {
        if(attacker instanceof PlayerEntityAccess playerEntityAccess && playerEntityAccess.isCursed()) {
            cir.setReturnValue(Text.translatable("death.attack.midas", killed.getDisplayName(), attacker.getDisplayName()));
        }
    }
}
