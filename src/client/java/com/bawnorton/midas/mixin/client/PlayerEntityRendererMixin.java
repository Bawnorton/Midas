package com.bawnorton.midas.mixin.client;

import com.bawnorton.midas.access.LivingEntityRendererAccess;
import com.bawnorton.midas.api.MidasApi;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexConsumers;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> implements LivingEntityRendererAccess<AbstractClientPlayerEntity> {

    private AbstractClientPlayerEntity playerEntity;
    private VertexConsumerProvider vertexConsumerProvider;
    private MatrixStack matrixStack;

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"))
    public void renderHead(AbstractClientPlayerEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        this.playerEntity = livingEntity;
        this.matrixStack = matrixStack;
    }

    @Override
    public VertexConsumer getVertexConsumer(AbstractClientPlayerEntity livingEntity, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, boolean bl, boolean bl2, boolean bl3, RenderLayer renderLayer) {
        if(MidasApi.isCursed(livingEntity)) {
            VertexConsumer glint = vertexConsumerProvider.getBuffer(RenderLayer.getEntityGlint());
            VertexConsumer normal = vertexConsumerProvider.getBuffer(renderLayer);
            return VertexConsumers.union(glint, normal);
        }
        return vertexConsumerProvider.getBuffer(renderLayer);
    }

    @Inject(method = "renderArm", at = @At("HEAD"))
    public void renderArmHead(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve, CallbackInfo ci) {
        this.playerEntity = player;
        this.matrixStack = matrices;
        this.vertexConsumerProvider = vertexConsumers;
    }

    @ModifyArg(method = "renderArm", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V", ordinal = 0), index = 1)
    public VertexConsumer getArmBuffer(VertexConsumer vertices) {
        return getVertexConsumer(playerEntity, matrixStack, vertexConsumerProvider, false, false, false, RenderLayer.getEntitySolid(playerEntity.getSkinTexture()));
    }

    @ModifyArg(method = "renderArm", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V", ordinal = 1), index = 1)
    public VertexConsumer getBufferSleeveBuffer(VertexConsumer vertices) {
        return getVertexConsumer(playerEntity, matrixStack, vertexConsumerProvider, false, false, false, RenderLayer.getEntityTranslucent(playerEntity.getSkinTexture()));
    }
}
