package com.bawnorton.midas.mixin.client;

import com.bawnorton.midas.Midas;
import com.bawnorton.midas.util.ColourUtil;
import com.bawnorton.midas.util.GreyscaleBlockRegistry;
import com.bawnorton.midas.util.GreyscaledSpriteContents;
import net.minecraft.client.texture.*;
import net.minecraft.resource.Resource;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;

@Mixin(SpriteLoader.class)
public abstract class SpriteLoaderMixin {

    @Shadow @Final private Identifier id;
    @Unique
    private final ThreadLocal<GreyscaledSpriteContents> cachedInfo = ThreadLocal.withInitial(() -> null);


    @Inject(method = "stitch", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I", ordinal = 3), locals = LocalCapture.CAPTURE_FAILHARD)
    private void createGreyscaleSprites(List<SpriteContents> sprites, int mipLevel, Executor executor, CallbackInfoReturnable<SpriteLoader.StitchResult> cir, int i, TextureStitcher<SpriteContents> textureStitcher, int j, int k) {
        if(id.equals(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)) {
            List<TextureStitcher.Holder<SpriteContents>> holders = ((TextureStitcherAccessor) textureStitcher).getHolders();
            for(Identifier identifier: GreyscaleBlockRegistry.GREYSCALE_BLOCK_SPRITES) {
                Optional<TextureStitcher.Holder<SpriteContents>> spriteData = holders.stream().filter(holder -> holder.sprite().getId().equals(identifier)).findAny();
                if(spriteData.isPresent()) {
                    textureStitcher.add(GreyscaledSpriteContents.of(spriteData.get().sprite()));
                } else {
                    textureStitcher.add(GreyscaledSpriteContents.ofMissing(identifier, MissingSprite.createSpriteContents()));
                    Midas.LOGGER.error("[GreyscaleSpriteInject]: Using missing texture, sprite {} not found", identifier);
                }
            }
        }
    }

    @Inject(method = "load(Lnet/minecraft/util/Identifier;Lnet/minecraft/resource/Resource;)Lnet/minecraft/client/texture/SpriteContents;", at = @At("HEAD"), cancellable = true)
    private static void loadHead(Identifier id, Resource resource, CallbackInfoReturnable<SpriteContents> cir) {
        System.out.println("Loading sprite " + id);
    }

    @ModifyArg(method = "load(Lnet/minecraft/util/Identifier;Lnet/minecraft/resource/Resource;)Lnet/minecraft/client/texture/SpriteContents;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/SpriteContents;<init>(Lnet/minecraft/util/Identifier;Lnet/minecraft/client/texture/SpriteDimensions;Lnet/minecraft/client/texture/NativeImage;Lnet/minecraft/client/resource/metadata/AnimationResourceMetadata;)V"))
    private static NativeImage greyscaleImageCall(NativeImage value){
        return ColourUtil.convertToGreyscale(value);
    }
}
