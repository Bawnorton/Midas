package com.bawnorton.midas;

import com.bawnorton.midas.command.CommandHandler;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.state.property.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Midas implements ModInitializer {
	public static final String MOD_ID = "midas";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Midas");
		CommandHandler.init();
	}

	public static ItemStack goldify(ItemStack stack) {
		Item item = goldify(stack.getItem());
		if (item == stack.getItem()) return stack;
		ItemStack newStack = new ItemStack(item, stack.getCount());
		if(stack.hasNbt()) {
			newStack.setNbt(stack.getNbt());
		}
		return newStack;
	}

	public static Item goldify(Item item) {
		RegistryEntry.Reference<Item> reference = Registries.ITEM.entryOf(RegistryKey.of(RegistryKeys.ITEM, Registries.ITEM.getId(item)));
		if (reference.isIn(ItemTags.RAILS))
			return Items.POWERED_RAIL;
		if (reference.isIn(ItemTags.PICKAXES))
			return Items.GOLDEN_PICKAXE;
		if (reference.isIn(ItemTags.SWORDS))
			return Items.GOLDEN_SWORD;
		if (reference.isIn(ItemTags.SHOVELS))
			return Items.GOLDEN_SHOVEL;
		if (reference.isIn(ItemTags.HOES))
			return Items.GOLDEN_HOE;
		if (reference.isIn(ItemTags.AXES))
			return Items.GOLDEN_AXE;
		if (item instanceof ArmorItem armorItem) {
			return switch (armorItem.getType()) {
				case HELMET -> Items.GOLDEN_HELMET;
				case CHESTPLATE -> Items.GOLDEN_CHESTPLATE;
				case LEGGINGS -> Items.GOLDEN_LEGGINGS;
				case BOOTS -> Items.GOLDEN_BOOTS;
			};
		}
		if (item == Items.APPLE)
			return Items.GOLDEN_APPLE;
		if (item == Items.GOLDEN_APPLE || item == Items.ENCHANTED_GOLDEN_APPLE)
			return Items.ENCHANTED_GOLDEN_APPLE;
		if (item == Items.GOLDEN_CARROT || item == Items.CARROT)
			return Items.GOLDEN_CARROT;
		if (item == Items.ARROW || item == Items.SPECTRAL_ARROW)
			return Items.SPECTRAL_ARROW;
		if (item == Items.IRON_INGOT || item == Items.COPPER_INGOT || item == Items.NETHERITE_INGOT || item == Items.GOLD_INGOT)
			return Items.GOLD_INGOT;
		if (item == Items.IRON_NUGGET || item == Items.GOLD_NUGGET)
			return Items.GOLD_NUGGET;
		if (item == Items.RAW_GOLD || item == Items.RAW_IRON || item == Items.RAW_COPPER)
			return Items.RAW_GOLD;
		if (item == Items.MELON_SLICE || item == Items.GLISTERING_MELON_SLICE)
			return Items.GLISTERING_MELON_SLICE;
		if (item instanceof HorseArmorItem)
			return Items.GOLDEN_HORSE_ARMOR;
		if (item instanceof BlockItem)
			return goldify(((BlockItem) item).getBlock()).asItem();
		return Items.GOLD_INGOT;
	}

	public static Block goldify(Block block) {
		if(block == Blocks.RAW_COPPER_BLOCK || block == Blocks.RAW_IRON_BLOCK || block == Blocks.RAW_GOLD_BLOCK) {
			return Blocks.RAW_GOLD_BLOCK;
		}
		if(block == Blocks.GOLD_ORE || block == Blocks.IRON_ORE || block == Blocks.COAL_ORE || block == Blocks.DIAMOND_ORE || block == Blocks.EMERALD_ORE || block == Blocks.LAPIS_ORE || block == Blocks.REDSTONE_ORE) {
			return Blocks.GOLD_ORE;
		}
		if(block == Blocks.DEEPSLATE_GOLD_ORE || block == Blocks.DEEPSLATE_IRON_ORE || block == Blocks.DEEPSLATE_COAL_ORE || block == Blocks.DEEPSLATE_DIAMOND_ORE || block == Blocks.DEEPSLATE_EMERALD_ORE || block == Blocks.DEEPSLATE_LAPIS_ORE || block == Blocks.DEEPSLATE_REDSTONE_ORE) {
			return Blocks.DEEPSLATE_GOLD_ORE;
		}
		if(block == Blocks.NETHER_QUARTZ_ORE || block == Blocks.NETHER_GOLD_ORE) {
			return Blocks.NETHER_GOLD_ORE;
		}
		return Blocks.GOLD_BLOCK;
	}

	public static BlockState goldify(BlockState blockState) {
		Block goldBlock = goldify(blockState.getBlock());
		return goldBlock == blockState.getBlock() ? blockState : copyProperties(blockState, goldBlock.getDefaultState());
	}

	private static BlockState copyProperties(BlockState blockState, BlockState newBlockState) {
		for (Property<?> property : blockState.getProperties()) {
			newBlockState = copyProperty(blockState, newBlockState, property);
		}
		return newBlockState;
	}

	private static <T extends Comparable<T>> BlockState copyProperty(BlockState blockState, BlockState newBlockState, Property<T> property) {
		if (newBlockState.contains(property)) {
			newBlockState = newBlockState.with(property, blockState.get(property));
		}

		return newBlockState;
	}
}