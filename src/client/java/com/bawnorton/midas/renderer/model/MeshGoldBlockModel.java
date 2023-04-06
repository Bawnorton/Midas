package com.bawnorton.midas.renderer.model;

import com.bawnorton.midas.Midas;
import com.bawnorton.midas.util.GoldBlockData;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class MeshGoldBlockModel implements UnbakedModel {
    public static final Identifier GOLD_BLOCK = Midas.id("special/gold_block");
    public static final Identifier GOLD_ITEM = Midas.id("item/gold_block");
    public static final SpriteIdentifier GOLD_SPRITE = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft", "block/gold_block"));
    public static Sprite GOLD_TEXTURE;

    private final GoldBlockModelBakery.Factory<Mesh> factory = new GoldBlockModelBakery.Factory<>(Midas.id("block/gold_block")) {
        @Override
        public GoldBlockModelBakery<Mesh> createBakery(BakedModel baseModel, Sprite placeHolderSprite) {
            return new MeshModelBakery(baseModel, placeHolderSprite);
        }
    };

    @Override
    public Collection<Identifier> getModelDependencies() {
        return factory.getModelDependencies();
    }

    @Override
    public void setParents(Function<Identifier, UnbakedModel> modelLoader) {
        factory.setParents(modelLoader);
    }

    @Nullable
    @Override
    public BakedModel bake(Baker baker, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings settings, Identifier modelId) {
        GOLD_TEXTURE = textureGetter.apply(GOLD_SPRITE);
        return new Baked(factory.createBakery(baker, textureGetter, settings, modelId));
    }

    private static class Baked extends ForwardingBakedModel {
        public Baked(GoldBlockModelBakery<Mesh> bakery) {
            this.wrapped = bakery.baseModel();
            this.bakery = bakery;
        }

        private final GoldBlockModelBakery<Mesh> bakery;

        @Override
        public boolean isVanillaAdapter() {
            return false;
        }

        @Override
        public Sprite getParticleSprite() {
            return GOLD_TEXTURE;
        }

        @Override
        public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
            if(((RenderAttachedBlockView) blockView).getBlockEntityRenderAttachment(pos) instanceof GoldBlockData data) {
                context.meshConsumer().accept(bakery.bake(data));
            }
        }

        @Override
        public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
            context.meshConsumer().accept(bakery.bake(GoldBlockData.fromItemStack(stack)));
        }
    }
}