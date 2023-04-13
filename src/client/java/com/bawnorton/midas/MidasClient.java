package com.bawnorton.midas;

import com.bawnorton.midas.entity.MidasEntities;
import com.bawnorton.midas.liquid.MidasFluids;
import com.bawnorton.midas.network.ClientNetworking;
import com.bawnorton.midas.renderer.GoldPlayerEntityRenderer;
import com.bawnorton.midas.renderer.model.entity.GoldPlayerEntityModel;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class MidasClient implements ClientModInitializer {

	public static final Identifier MIDAS_BLOCK_ATLAS_TEXTURE = Midas.id("textures/atlas/midas_blocks.png");
	public static final Identifier MIDAS_BLOCK_ATLAS = Midas.id("midas_blocks");

	@Override
	public void onInitializeClient() {
		EntityModelLayerRegistry.registerModelLayer(GoldPlayerEntityModel.LAYER_LOCATION, GoldPlayerEntityModel::getTextureModelData);
		EntityRendererRegistry.register(MidasEntities.GOLD_PLAYER, GoldPlayerEntityRenderer::new);

		FluidRenderHandlerRegistry.INSTANCE.register(MidasFluids.STILL_PACTOLUS_WATER, MidasFluids.FLOWING_PACTOLUS_WATER, SimpleFluidRenderHandler.coloredWater(0x98eddf));
		BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), MidasFluids.STILL_PACTOLUS_WATER, MidasFluids.FLOWING_PACTOLUS_WATER);

		ClientNetworking.init();
	}
}