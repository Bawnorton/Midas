package com.bawnorton.midas.access;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Unique;

public interface LivingEntityRendererAccess<T extends LivingEntity> {
    @Unique
    VertexConsumer getVertexConsumer(T livingEntity, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, boolean bl, boolean bl2, boolean bl3, RenderLayer defaultRenderLayer);
}
