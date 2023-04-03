package com.bawnorton.midas;

import com.bawnorton.midas.command.CommandHandler;
import com.bawnorton.midas.entity.GoldPlayerEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Midas implements ModInitializer {
	public static final String MOD_ID = "midas";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final EntityType<GoldPlayerEntity> GOLD_PLAYER = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier(MOD_ID, "gold_player"),
			FabricEntityTypeBuilder.create(SpawnGroup.MISC, GoldPlayerEntity::new)
					.dimensions(EntityDimensions.fixed(EntityType.ZOMBIE.getWidth(), EntityType.ZOMBIE.getHeight()))
					.build()
	);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Midas");
		CommandHandler.init();

		FabricDefaultAttributeRegistry.register(GOLD_PLAYER, GoldPlayerEntity.createZombieAttributes());
	}
}