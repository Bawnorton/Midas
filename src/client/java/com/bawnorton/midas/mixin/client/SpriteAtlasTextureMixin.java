package com.bawnorton.midas.mixin.client;

import com.bawnorton.midas.Midas;
import com.bawnorton.midas.util.ColourUtil;
import net.minecraft.client.texture.*;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(SpriteAtlasTexture.class)
public abstract class SpriteAtlasTextureMixin {
    @Shadow @Final private Identifier id;

    @Inject(method = "upload", at = @At("HEAD"))
    private void onUpload(SpriteLoader.StitchResult stitchResult, CallbackInfo ci) {
        if(!id.getNamespace().equals(Midas.MOD_ID)) return;
        System.out.println("Uploading atlas: " + id);
        Map<Identifier, Sprite> spriteMap = stitchResult.regions();
        spriteMap.forEach((id, sprite) -> {
            SpriteContents contents = sprite.getContents();
            NativeImage greyscale = ColourUtil.convertToGreyscale(contents.image);
            ((SpriteContentsAccessor) contents).setImage(greyscale);
            ((SpriteAccessor) sprite).setContents(contents);
        });
        ((StitchResultAccessor) stitchResult).setRegions(spriteMap);
    }
}
