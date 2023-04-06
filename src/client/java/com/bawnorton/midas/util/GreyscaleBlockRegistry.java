package com.bawnorton.midas.util;

import com.bawnorton.midas.Midas;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Set;

public class GreyscaleBlockRegistry {
    public static final Set<Identifier> GREYSCALE_BLOCK_SPRITES = new HashSet<>();
    public static GreyscaleBlockRegistry INSTANCE = new GreyscaleBlockRegistry();

    public static void register(Block block) {
        register(Registries.BLOCK.getId(block));
    }

    public static void register(Identifier id) {
        GREYSCALE_BLOCK_SPRITES.add(new Identifier(id.getNamespace(), "block/" + id.getPath()));
    }

    public Identifier createGreyscaleIdentifier(Identifier id) {
        return Midas.id(id.getPath() + "_grey");
    }
}
