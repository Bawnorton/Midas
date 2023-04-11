package com.bawnorton.midas.entity;

import com.bawnorton.midas.Midas;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class MidasEntities {
    public static final EntityType<GoldPlayerEntity> GOLD_PLAYER = Registry.register(Registries.ENTITY_TYPE, Midas.id("gold_player"), FabricEntityTypeBuilder.create(SpawnGroup.MISC, GoldPlayerEntity::new).dimensions(EntityDimensions.fixed(EntityType.ZOMBIE.getWidth(), EntityType.ZOMBIE.getHeight())).build());

    public static void init() {
        Midas.LOGGER.debug("Initializing Midas Entities");
        FabricDefaultAttributeRegistry.register(MidasEntities.GOLD_PLAYER, GoldPlayerEntity.createZombieAttributes());
    }
}
