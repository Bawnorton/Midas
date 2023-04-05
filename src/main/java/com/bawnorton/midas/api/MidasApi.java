package com.bawnorton.midas.api;

import com.bawnorton.midas.Midas;
import com.bawnorton.midas.access.DataSaverAccess;
import com.bawnorton.midas.access.ServerPlayerAccess;
import com.bawnorton.midas.block.GoldBlock;
import com.bawnorton.midas.block.GoldBlockEntity;
import com.bawnorton.midas.entity.GoldPlayerEntity;
import com.bawnorton.midas.item.GoldItem;
import com.bawnorton.midas.network.Networking;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.DeathMessageS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public abstract class MidasApi {

    private static Item getGoldCopy(Item item) {
        if(item == Items.AIR) return item;
        if(item instanceof GoldItem) return item;
        return Midas.DEFAULT_GOLD_ITEM;
    }

    private static Block getGoldCopy(Block block) {
        if(block.getDefaultState().isAir()) return block;
        if(block instanceof GoldBlock) return block;
        return Midas.DEFAULT_GOLD_BLOCK;
    }

    public static void cursePlayer(PlayerEntity player) {
        DataSaverAccess playerEntityAccess = getDataAccess(player);
        if(playerEntityAccess == null) return;
        if(playerEntityAccess.isCursed()) return;
        playerEntityAccess.setCursed(true);
        NbtCompound midasData = playerEntityAccess.getMidasData();
        midasData.putBoolean("cursed", true);
        if(player instanceof ServerPlayerEntity serverPlayer) {
            syncPlayerData(serverPlayer, midasData.getBoolean("cursed"), midasData.getBoolean("gold"));
        }
    }

    public static void cleansePlayer(PlayerEntity player) {
        DataSaverAccess playerEntityAccess = getDataAccess(player);
        if(playerEntityAccess == null) return;
        if(!playerEntityAccess.isCursed()) return;
        playerEntityAccess.setCursed(false);
        NbtCompound midasData = playerEntityAccess.getMidasData();
        midasData.putBoolean("cursed", false);
        if(player instanceof ServerPlayerEntity serverPlayer) {
            syncPlayerData(serverPlayer, midasData.getBoolean("cursed"), midasData.getBoolean("gold"));
        }
    }

    public static boolean isCursed(PlayerEntity player) {
        DataSaverAccess playerEntityAccess = getDataAccess(player);
        if(playerEntityAccess == null) return false;
        return playerEntityAccess.isCursed();
    }

    public static void turnToGold(Entity entity) {
        if(entity instanceof ItemEntity itemEntity) {
            ItemStack stack = itemEntity.getStack();
            ItemStack newStack = turnToGold(stack);
            if(newStack != stack) {
                itemEntity.setStack(newStack);
            }
        }
        DataSaverAccess entityAccess = getDataAccess(entity);
        if(entityAccess == null) return;
        if(entityAccess.isGold()) return;
        entityAccess.setGold(true);
        NbtCompound midasData = entityAccess.getMidasData();
        midasData.putBoolean("gold", true);
        if(entity instanceof ServerPlayerEntity serverPlayer && !serverPlayer.isCreative() && !((ServerPlayerAccess) serverPlayer).didDieToGold()) {
            if(serverPlayer.networkHandler == null) return;
            GoldPlayerEntity goldPlayer = GoldPlayerEntity.create(serverPlayer);
            if(goldPlayer == null) return;
            goldPlayer.copyPositionAndRotation(serverPlayer);
            ((ServerPlayerAccess) serverPlayer).dieToGold();
            serverPlayer.setHealth(0);
            Text text = Text.translatable("death.attack.midas", serverPlayer.getDisplayName());
            serverPlayer.networkHandler.sendPacket(new DeathMessageS2CPacket(serverPlayer.getDamageTracker(), text));
            serverPlayer.server.getPlayerManager().broadcast(text, false);
            syncPlayerData(serverPlayer, midasData.getBoolean("cursed"), midasData.getBoolean("gold"));
        }
    }

    public static void turnToGold(BlockEntity blockEntity) {
        DataSaverAccess blockEntityAccess = getDataAccess(blockEntity);
        if(blockEntityAccess == null) return;
        if(blockEntityAccess.isGold()) return;
        NbtCompound midasData = blockEntityAccess.getMidasData();
        midasData.putBoolean("gold", true);
        blockEntity.markDirty();
        blockEntity.getWorld().updateListeners(blockEntity.getPos(), blockEntity.getCachedState(), blockEntity.getCachedState(), 3);
    }

    public static boolean isGold(Object obj) {
        DataSaverAccess dataAccess = getDataAccess(obj);
        if(dataAccess == null) return false;
        return dataAccess.isGold();
    }

    @Nullable
    private static DataSaverAccess getDataAccess(Object obj) {
        if(obj instanceof DataSaverAccess dataSaverAccess) {
            return dataSaverAccess;
        } else if (obj == null) {
            return null;
        }
        throw new IllegalArgumentException(obj + " is not a DataSaverAccess");
    }

    public static ItemStack turnToGold(ItemStack stack) {
        Item item = turnToGold(stack.getItem());
        if (item == stack.getItem()) return stack;
        ItemStack newStack = new ItemStack(item, stack.getCount());
        if(stack.hasNbt()) {
            newStack.setNbt(stack.getNbt());
        }
        return newStack;
    }

    public static Item turnToGold(Item item) {
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
            return turnToGold(((BlockItem) item).getBlock()).asItem();
        return getGoldCopy(item);
    }

    public static Block turnToGold(Block block) {
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
        return getGoldCopy(block);
    }

    public static BlockState turnToGold(BlockState blockState, BlockPos pos, ServerWorld world) {
        if(blockState.isOf(Midas.DEFAULT_GOLD_BLOCK)) return blockState;

        Block original = blockState.getBlock();
        Block block = turnToGold(blockState.getBlock());
        BlockState newState = copyProperties(blockState, block.getDefaultState());
        world.setBlockState(pos, newState, Block.NOTIFY_ALL);
        if(block instanceof GoldBlock) {
            BlockEntity entity = world.getBlockEntity(pos);
            if(entity instanceof GoldBlockEntity goldBlockEntity) {
                goldBlockEntity.setData(original);
            } else {
                GoldBlockEntity goldBlockEntity = new GoldBlockEntity(pos, newState);
                goldBlockEntity.setData(original);
                world.addBlockEntity(goldBlockEntity);
            }
        }
        return newState;
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

    private static void syncPlayerData(ServerPlayerEntity player, boolean isCursed, boolean isGold) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBoolean(isCursed);
        buf.writeBoolean(isGold);
        if(player.networkHandler != null) {
            ServerPlayNetworking.send(player, Networking.MIDAS_PLAYER_SYNC, buf);
        }
    }
}
