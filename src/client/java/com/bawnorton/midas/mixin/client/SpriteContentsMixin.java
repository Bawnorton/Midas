package com.bawnorton.midas.mixin.client;

import com.bawnorton.midas.access.SpriteContentsAccess;
import net.minecraft.client.resource.metadata.AnimationResourceMetadata;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.SpriteContents;
import net.minecraft.client.texture.SpriteDimensions;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpriteContents.class)
public abstract class SpriteContentsMixin implements SpriteContentsAccess {
    private AnimationResourceMetadata animationData;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(Identifier id, SpriteDimensions dimensions, NativeImage image, AnimationResourceMetadata metadata, CallbackInfo ci) {
        this.animationData = metadata;
    }

    @Override
    public AnimationResourceMetadata getAnimationData() {
        return animationData;
    }
}
