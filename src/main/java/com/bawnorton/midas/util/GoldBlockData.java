package com.bawnorton.midas.util;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public record GoldBlockData(@NotNull Block block) {
    public static GoldBlockData fromNbt(NbtCompound nbt) {
        return new GoldBlockData(Registries.BLOCK.getOrEmpty(Identifier.tryParse(nbt.getString("block"))).orElse(Blocks.GOLD_BLOCK));
    }

    public static GoldBlockData fromItemStack(ItemStack stack) {
        NbtCompound nbt = stack.getSubNbt("BlockEntityTag");
        if (nbt != null) {
            return fromNbt(nbt.getCompound("GoldBlockData"));
        }
        return new GoldBlockData(Blocks.GOLD_BLOCK);
    }

    public NbtCompound toNbt(NbtCompound nbt) {
        nbt.putString("block", Registries.BLOCK.getId(block).toString());
        return nbt;
    }

    public NbtCompound toNbt() {
        return toNbt(new NbtCompound());
    }

    public ItemStack toItemStack(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateSubNbt("BlockEntityTag");
        nbt.put("GoldBlockData", toNbt());
        return stack;
    }
}
