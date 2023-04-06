package com.bawnorton.midas.renderer.model;

import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public record MeshModelBakery(BakedModel baseModel, Sprite template) implements GoldBlockModelBakery<Mesh> {

    public Mesh bake(@Nullable Object cacheKey, @Nullable Block original) {
        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        assert renderer != null;

        MeshBuilder meshBuilder = renderer.meshBuilder();
        QuadEmitter emitter = meshBuilder.getEmitter();

        BlockState state = original == null ? Blocks.AIR.getDefaultState() : original.getDefaultState();

        ModelIdentifier modelIdentifier = BlockModels.getModelId(state);
        BakedModelManager modelManager = MinecraftClient.getInstance().getBakedModelManager();
        BakedModel model = modelManager.getModel(modelIdentifier);
//        RenderLayer layer = RenderLayers.getBlockLayer(state);
        Sprite sprite = model.getParticleSprite();

        int gold = 0xFED231;
        for(Direction direction: Direction.values()) {
            emitter.square(direction, 0, 0, 1, 1, 0);
            emitter.material(renderer.materialFinder().blendMode(0, BlendMode.fromRenderLayer(RenderLayer.getSolid())).find());
            emitter.spriteBake(0, sprite, MutableQuadView.BAKE_LOCK_UV);
            emitter.spriteColor(0, gold, gold, gold, gold);
            emitter.emit();
        }

        return meshBuilder.build();
    }
}