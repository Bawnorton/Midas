package com.bawnorton.midas.mixin.client;

import com.bawnorton.midas.MidasClient;
import net.minecraft.client.render.model.SpriteAtlasManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.HashMap;
import java.util.Map;

@Mixin(SpriteAtlasManager.class)
public abstract class SpriteAtlasManagerMixin {
    @ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true, index = 1)
    private static Map<Identifier, Identifier> appendMidasAtlas(Map<Identifier, Identifier> value) {
        HashMap<Identifier, Identifier> map = new HashMap<>(value);
        map.put(MidasClient.MIDAS_BLOCK_ATLAS_TEXTURE, MidasClient.MIDAS_BLOCK_ATLAS);
        return map;
    }
}
