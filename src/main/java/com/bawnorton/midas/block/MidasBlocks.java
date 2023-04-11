package com.bawnorton.midas.block;

import com.bawnorton.midas.Midas;
import com.bawnorton.midas.block.entity.GoldBlockEntity;
import com.bawnorton.midas.liquid.MidasFluids;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class MidasBlocks {
    // blocks
    public static final Block GOLD_BLOCK = Registry.register(Registries.BLOCK, Midas.id("gold_block"), new GoldBlock());

    // fluids
    public static final Block pactolus_WATER = Registry.register(Registries.BLOCK, Midas.id("pactolus_water"), new FluidBlock(MidasFluids.STILL_PACTOLUS_WATER, FabricBlockSettings.copy(Blocks.WATER)){});

    // block entities
    public static final BlockEntityType<GoldBlockEntity> GOLD_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, Midas.id("gold_block_entity"), FabricBlockEntityTypeBuilder.create(GoldBlockEntity::new, GOLD_BLOCK).build());

    public static void init() {
        Midas.LOGGER.debug("Initializing Midas Blocks");
    }
}
