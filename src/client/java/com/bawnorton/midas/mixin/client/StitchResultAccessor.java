package com.bawnorton.midas.mixin.client;

import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteLoader;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(SpriteLoader.StitchResult.class)
public interface StitchResultAccessor {
    @Accessor @Mutable
    void setRegions(Map<Identifier, Sprite> spriteMap);
}
