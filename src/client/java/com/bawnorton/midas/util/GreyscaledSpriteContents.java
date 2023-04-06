package com.bawnorton.midas.util;

import com.bawnorton.midas.access.SpriteContentsAccess;
import net.minecraft.client.resource.metadata.AnimationResourceMetadata;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.SpriteContents;
import net.minecraft.client.texture.SpriteDimensions;
import net.minecraft.util.Identifier;

public class GreyscaledSpriteContents extends SpriteContents {
    private final Identifier defaultTextureId;

    public GreyscaledSpriteContents(Identifier id, int width, int height, NativeImage image, AnimationResourceMetadata metadata) {
        super(id, new SpriteDimensions(width, height), image, metadata);
        this.defaultTextureId = id;
    }


    public static GreyscaledSpriteContents of(SpriteContents contents){
        return new GreyscaledSpriteContents(GreyscaleBlockRegistry.INSTANCE.createGreyscaleIdentifier(contents.getId()), contents.getWidth(), contents.getHeight(), contents.image, ((SpriteContentsAccess) contents).getAnimationData());
    }

    public static GreyscaledSpriteContents ofMissing(Identifier id, SpriteContents contents) {
        return new GreyscaledSpriteContents(GreyscaleBlockRegistry.INSTANCE.createGreyscaleIdentifier(id), contents.getWidth(), contents.getHeight(), contents.image, ((SpriteContentsAccess) contents).getAnimationData());
    }

    public Identifier getDefaultTextureId(){
        return defaultTextureId;
    }
}