package com.bawnorton.midas;

import com.bawnorton.midas.network.ClientNetworking;
import com.bawnorton.midas.renderer.GoldPlayerEntityRenderer;
import com.bawnorton.midas.renderer.model.GoldPlayerEntityModel;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class MidasClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityModelLayerRegistry.registerModelLayer(GoldPlayerEntityModel.LAYER_LOCATION, GoldPlayerEntityModel::getTextureModelData);
		EntityRendererRegistry.register(Midas.GOLD_PLAYER, GoldPlayerEntityRenderer::new);

		ClientNetworking.init();
	}
}