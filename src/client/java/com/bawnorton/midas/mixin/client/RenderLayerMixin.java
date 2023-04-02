package com.bawnorton.midas.mixin.client;

import com.bawnorton.midas.Midas;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(RenderLayer.class)
public abstract class RenderLayerMixin {
    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderPhase$Texture;<init>(Lnet/minecraft/util/Identifier;ZZ)V", ordinal = 5), index = 0)
    private static Identifier init(Identifier id) {
        return new Identifier(Midas.MOD_ID, "textures/misc/enchanted_glint_gold.png");
    }
}
