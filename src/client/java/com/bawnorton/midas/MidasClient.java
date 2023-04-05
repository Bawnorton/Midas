package com.bawnorton.midas;

import com.bawnorton.midas.network.ClientNetworking;
import com.bawnorton.midas.renderer.GoldPlayerEntityRenderer;
import com.bawnorton.midas.renderer.model.entity.GoldPlayerEntityModel;
import com.bawnorton.midas.renderer.model.MeshGoldBlockModel;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.render.model.BasicBakedModel;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class MidasClient implements ClientModInitializer {

	public static UnbakedModel GOLD_BLOCK_MODEL;
	public static Map<Identifier, BasicBakedModel> GOLD_BLOCK_MODELS = new HashMap<>();

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

		ClientNetworking.init();
	}
}