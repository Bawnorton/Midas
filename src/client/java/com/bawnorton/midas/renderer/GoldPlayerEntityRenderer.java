package com.bawnorton.midas.renderer;

import com.bawnorton.midas.entity.GoldPlayerEntity;
import com.bawnorton.midas.renderer.model.GoldPlayerEntityModel;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.ArmorEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;

public class GoldPlayerEntityRenderer extends BipedEntityRenderer<GoldPlayerEntity, GoldPlayerEntityModel<GoldPlayerEntity>> {

    public GoldPlayerEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new GoldPlayerEntityModel<>(ctx.getPart(GoldPlayerEntityModel.LAYER_LOCATION)), 0.5f);
        this.addFeature(new ArmorFeatureRenderer<>(this, new ArmorEntityModel<>(ctx.getPart(EntityModelLayers.PLAYER_INNER_ARMOR)), new ArmorEntityModel<>(ctx.getPart(EntityModelLayers.PLAYER_OUTER_ARMOR)), ctx.getModelManager()));
    }

    @Override
    public Identifier getTexture(GoldPlayerEntity entity) {
        return GoldPlayerEntityModel.LAYER_LOCATION.getId();
    }
}
