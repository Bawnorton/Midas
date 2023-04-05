package com.bawnorton.midas.renderer.model;

import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record MeshModelBakery(BakedModel baseModel, Sprite template) implements GoldBlockModelBakery<Mesh> {

    public Mesh bake(@Nullable Object cacheKey, @Nullable Block original) {
        BlockRenderManager manager = MinecraftClient.getInstance().getBlockRenderManager();
        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        assert renderer != null;

        MeshBuilder meshBuilder = renderer.meshBuilder();
        QuadEmitter emitter = meshBuilder.getEmitter();

        BlockState state = original == null ? Blocks.AIR.getDefaultState() : original.getDefaultState();

        ModelIdentifier modelIdentifier = BlockModels.getModelId(state);
        manager.getModel(state);
        BakedModelManager modelManager = MinecraftClient.getInstance().getBakedModelManager();
        BakedModel model = modelManager.getModel(modelIdentifier);
        Sprite sprite = model.getParticleSprite();

        Direction[] directionsAndNull = new Direction[]{null, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN};

        Random random = Random.create();
        for (Direction cullFace : directionsAndNull) {
            List<BakedQuad> quads = model.getQuads(state, cullFace, random);
            for (BakedQuad quad : quads) {
                emitter.fromVanilla(quad, null, cullFace);
                emitter.material(null);

                SpriteUvBounds bounds = SpriteUvBounds.readOff(emitter);

                if (bounds.displaysSprite(template)) {
                    bounds.normalizeEmitter(emitter, template);
                    emitter.spriteBake(0, sprite, MutableQuadView.BAKE_NORMALIZED);
                    emitter.emit();
                    continue;
                }
                emitter.emit();
            }
        }

        return meshBuilder.build();
    }

    private record SpriteUvBounds(float minU, float maxU, float minV, float maxV) {
        static SpriteUvBounds readOff(QuadEmitter emitter) {
            float minU = Float.POSITIVE_INFINITY;
            float maxU = Float.NEGATIVE_INFINITY;
            float minV = Float.POSITIVE_INFINITY;
            float maxV = Float.NEGATIVE_INFINITY;

            for (int i = 0; i < 4; i++) {
                float u = emitter.spriteU(i, 0);
                if (minU > u) minU = u;
                if (maxU < u) maxU = u;

                float v = emitter.spriteV(i, 0);
                if (minV > v) minV = v;
                if (maxV < v) maxV = v;
            }

            return new SpriteUvBounds(minU, maxU, minV, maxV);
        }

        boolean displaysSprite(Sprite sprite) {
            return minU <= sprite.getMinU() && maxU >= sprite.getMaxU() && minV <= sprite.getMinV() && maxV >= sprite.getMaxV();
        }

        void normalizeEmitter(QuadEmitter emitter, Sprite sprite) {
            float remappedMinU = remap(minU, sprite.getMinU(), sprite.getMaxU(), 0, 1);
            float remappedMaxU = remap(maxU, sprite.getMinU(), sprite.getMaxU(), 0, 1);
            float remappedMinV = remap(minV, sprite.getMinV(), sprite.getMaxV(), 0, 1);
            float remappedMaxV = remap(maxV, sprite.getMinV(), sprite.getMaxV(), 0, 1);

            for (int i = 0; i < 4; i++) {
                float writeU = MathHelper.approximatelyEquals(emitter.spriteU(i, 0), minU) ? remappedMinU : remappedMaxU;
                float writeV = MathHelper.approximatelyEquals(emitter.spriteV(i, 0), minV) ? remappedMinV : remappedMaxV;
                emitter.sprite(i, 0, writeU, writeV);
            }
        }

        private static float remap(float value, float fromMin, float fromMax, float toMin, float toMax) {
            return toMin + (MathHelper.clamp(value, fromMin, fromMax) - fromMin) * (toMax - toMin) / (fromMax - toMin);
        }
    }
}