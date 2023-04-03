package com.bawnorton.midas.mixin.client;

import com.bawnorton.midas.access.EntityAccess;
import com.bawnorton.midas.access.LivingEntityRendererAccess;
import com.bawnorton.midas.entity.GoldPlayerEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin extends EntityRenderer<LivingEntity> implements LivingEntityRendererAccess<LivingEntity> {
	@Shadow
	protected EntityModel<LivingEntity> model;

	@Shadow
	protected abstract boolean isVisible(LivingEntity entity);

	private static final Identifier GOLD_BLOCK = new Identifier("textures/block/gold_block.png");
	private MatrixStack matrixStack;
	private LivingEntity livingEntity;

	protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
		super(ctx);
	}

	@Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"))
	private void renderHead(LivingEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
		this.matrixStack = matrixStack;
		this.livingEntity = livingEntity;
	}

	@Redirect(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumerProvider;getBuffer(Lnet/minecraft/client/render/RenderLayer;)Lnet/minecraft/client/render/VertexConsumer;"))
	private VertexConsumer getBuffer(VertexConsumerProvider vertexConsumerProvider, RenderLayer renderLayer) {
		boolean isVisible = isVisible(livingEntity);
		return getVertexConsumer(livingEntity, matrixStack, vertexConsumerProvider, isVisible, !isVisible && !livingEntity.isInvisibleTo(MinecraftClient.getInstance().player), MinecraftClient.getInstance().hasOutline(livingEntity), renderLayer);
	}

	@Override
	public VertexConsumer getVertexConsumer(LivingEntity livingEntity, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, boolean isVisible, boolean isTranslucent, boolean hasOutline, RenderLayer defaultRenderLayer) {
		if (((EntityAccess) livingEntity).isGold()) {
			return new OverlayVertexConsumer(vertexConsumerProvider.getBuffer(RenderLayer.getEntitySolid(GOLD_BLOCK)), matrixStack.peek().getPositionMatrix(), matrixStack.peek().getNormalMatrix(), 1.0F);
		} else {
			return vertexConsumerProvider.getBuffer(defaultRenderLayer);
		}
	}
}