package com.bawnorton.midas.item;

import com.bawnorton.midas.Midas;
import com.bawnorton.midas.block.MidasBlocks;
import com.bawnorton.midas.liquid.MidasFluids;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class MidasItems {
    // item groups
    public static final ItemGroup MIDAS_GROUP = FabricItemGroup.builder(Midas.id("group"))
            .icon(() -> new ItemStack(MidasBlocks.GOLD_BLOCK))
            .build();

    // items
    public static Item GOLD_BLOCK = Registry.register(Registries.ITEM, Midas.id("gold_block"), new BlockItem(MidasBlocks.GOLD_BLOCK, new Item.Settings()));
    public static Item PACTOLUS_BUCKET = Registry.register(Registries.ITEM, Midas.id("pactolus_bucket"), new BucketItem(MidasFluids.STILL_PACTOLUS_WATER, new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1)));

    public static void init() {
        Midas.LOGGER.debug("Initializing Midas Items");

        ItemGroupEvents.modifyEntriesEvent(MIDAS_GROUP).register(content -> {
            content.add(new ItemStack(MidasBlocks.GOLD_BLOCK));
            content.add(new ItemStack(MidasItems.PACTOLUS_BUCKET));
        });
    }
}
