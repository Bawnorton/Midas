package com.bawnorton.midas.mixin.client;

import net.minecraft.client.texture.SpriteContents;
import net.minecraft.client.texture.TextureStitcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(TextureStitcher.class)
public interface TextureStitcherAccessor {
    @Accessor
    List<TextureStitcher.Holder<SpriteContents>> getHolders();
}