package com.bawnorton.midas;

import com.bawnorton.midas.network.ClientNetworking;
import com.bawnorton.midas.renderer.GoldPlayerEntityRenderer;
import com.bawnorton.midas.renderer.model.MeshGoldBlockModel;
import com.bawnorton.midas.renderer.model.entity.GoldPlayerEntityModel;
import com.bawnorton.midas.util.GreyscaleBlockRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public class MidasClient implements ClientModInitializer {

	public static UnbakedModel GOLD_BLOCK_MODEL;

	@Override
	public void onInitializeClient() {
		EntityModelLayerRegistry.registerModelLayer(GoldPlayerEntityModel.LAYER_LOCATION, GoldPlayerEntityModel::getTextureModelData);
		EntityRendererRegistry.register(Midas.GOLD_PLAYER, GoldPlayerEntityRenderer::new);

		ModelLoadingRegistry.INSTANCE.registerResourceProvider(resourceManager -> (modelIdentifier, modelProviderContext) -> {
			if(MeshGoldBlockModel.GOLD_ITEM.equals(modelIdentifier) || MeshGoldBlockModel.GOLD_BLOCK.equals(modelIdentifier)) {
				if(GOLD_BLOCK_MODEL == null) {
					GOLD_BLOCK_MODEL = new MeshGoldBlockModel();
				}
				return GOLD_BLOCK_MODEL;
			}
			return null;
		});

		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public Identifier getFabricId() {
				return Midas.id("gold_block_model");
			}

			@Override
			public void reload(ResourceManager manager) {
				GOLD_BLOCK_MODEL = null;
			}
		});

		GreyscaleBlockRegistry.register(Blocks.GLOWSTONE);

		ClientNetworking.init();
	}
}