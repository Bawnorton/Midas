package com.bawnorton.midas;

import com.bawnorton.midas.block.GoldBlock;
import com.bawnorton.midas.block.GoldBlockEntity;
import com.bawnorton.midas.command.CommandHandler;
import com.bawnorton.midas.entity.GoldPlayerEntity;
import com.bawnorton.midas.item.GoldItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Midas implements ModInitializer {
	public static final String MOD_ID = "midas";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final EntityType<GoldPlayerEntity> GOLD_PLAYER = Registry.register(Registries.ENTITY_TYPE, id("gold_player"), FabricEntityTypeBuilder.create(SpawnGroup.MISC, GoldPlayerEntity::new).dimensions(EntityDimensions.fixed(EntityType.ZOMBIE.getWidth(), EntityType.ZOMBIE.getHeight())).build());

	public static final Block DEFAULT_GOLD_BLOCK = Registry.register(Registries.BLOCK, id("gold_block"), new GoldBlock());

	public static final Item DEFAULT_GOLD_BLOCK_ITEM = Registry.register(Registries.ITEM, id("gold_block"), new BlockItem(DEFAULT_GOLD_BLOCK, new Item.Settings()));
	public static final Item DEFAULT_GOLD_ITEM = Registry.register(Registries.ITEM, id("gold_item"), new GoldItem());

	public static final BlockEntityType<GoldBlockEntity> GOLD_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("gold_block_entity"), FabricBlockEntityTypeBuilder.create(GoldBlockEntity::new, DEFAULT_GOLD_BLOCK).build());

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Midas");
		CommandHandler.init();

		FabricDefaultAttributeRegistry.register(GOLD_PLAYER, GoldPlayerEntity.createZombieAttributes());
	}
}