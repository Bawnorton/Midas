package com.bawnorton.midas.renderer.model.entity;

import com.bawnorton.midas.Midas;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class GoldPlayerEntityModel<T extends LivingEntity> extends BipedEntityModel<T> {
	public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(new Identifier(Midas.MOD_ID, "textures/entity/gold_player"), "main");

	public GoldPlayerEntityModel(ModelPart root) {
		super(root);
	}

	public static TexturedModelData getTextureModelData() {
		ModelData meshdefinition = getModelData(new Dilation(0), 0);
		return TexturedModelData.of(meshdefinition, 64, 64);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		super.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}

	@Override
	public void setAngles(T livingEntity, float f, float g, float h, float i, float j) {
		Vec3d rotation = livingEntity.getRotationVector();
		this.head.yaw = (float) Math.toRadians(rotation.y);
		this.head.pitch = (float) Math.toRadians(rotation.x);
		this.head.roll = (float) Math.toRadians(rotation.z);
		body.yaw = (float) Math.toRadians(rotation.y);
		body.pitch = (float) Math.toRadians(rotation.x);
		body.roll = (float) Math.toRadians(rotation.z);
	}

	@Override
	public void animateModel(T livingEntity, float f, float g, float h) {
	}
}