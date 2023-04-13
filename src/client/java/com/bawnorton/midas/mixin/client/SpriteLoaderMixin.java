package com.bawnorton.midas.mixin.client;

import com.bawnorton.midas.util.ColourUtil;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.SpriteLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.IOException;
import java.io.InputStream;

@Mixin(SpriteLoader.class)
public abstract class SpriteLoaderMixin {
//    @Redirect(method = "load(Lnet/minecraft/util/Identifier;Lnet/minecraft/resource/Resource;)Lnet/minecraft/client/texture/SpriteContents;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/NativeImage;read(Ljava/io/InputStream;)Lnet/minecraft/client/texture/NativeImage;"))
//    private static NativeImage load(InputStream stream) throws IOException {
//        return ColourUtil.convertToGreyscale(NativeImage.read(stream));
//    }
}
