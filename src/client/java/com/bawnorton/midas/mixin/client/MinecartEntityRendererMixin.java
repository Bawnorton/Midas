package com.bawnorton.midas.mixin.client;

import com.bawnorton.midas.api.MidasApi;
import net.minecraft.client.render.OverlayVertexConsumer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.MinecartEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecartEntityRenderer.class)
public abstract class MinecartEntityRendererMixin {
    private MatrixStack matrixStack;
    private AbstractMinecartEntity minecartEntity;

    private static final Identifier GOLD_BLOCK = new Identifier("textures/block/gold_block.png");

    @Inject(method = "render(Lnet/minecraft/entity/vehicle/AbstractMinecartEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"))
    private void renderHead(AbstractMinecartEntity abstractMinecartEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        this.matrixStack = matrixStack;
        this.minecartEntity = abstractMinecartEntity;
    }

    @Redirect(method = "render(Lnet/minecraft/entity/vehicle/AbstractMinecartEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumerProvider;getBuffer(Lnet/minecraft/client/render/RenderLayer;)Lnet/minecraft/client/render/VertexConsumer;"))
    private VertexConsumer getBuffer(VertexConsumerProvider vertexConsumerProvider, RenderLayer renderLayer) {
        return getVertexConsumer(minecartEntity, matrixStack, vertexConsumerProvider, renderLayer);
    }

    public VertexConsumer getVertexConsumer(AbstractMinecartEntity abstractMinecartEntity, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, RenderLayer defaultRenderLayer) {
        if (MidasApi.isGold(abstractMinecartEntity)) {
            return new OverlayVertexConsumer(vertexConsumerProvider.getBuffer(RenderLayer.getEntitySolid(GOLD_BLOCK)), matrixStack.peek().getPositionMatrix(), matrixStack.peek().getNormalMatrix(), 1.0F);
        } else {
            return vertexConsumerProvider.getBuffer(defaultRenderLayer);
        }
    }
}
