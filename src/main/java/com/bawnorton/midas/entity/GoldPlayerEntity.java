package com.bawnorton.midas.entity;

import com.bawnorton.midas.Midas;
import com.bawnorton.midas.api.MidasApi;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.List;

public class GoldPlayerEntity extends ZombieEntity {
    private PlayerEntity ofPlayer;
    private List<ItemStack> inventory = DefaultedList.ofSize(36, ItemStack.EMPTY);

    public GoldPlayerEntity(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    public static GoldPlayerEntity create(ServerPlayerEntity forPlayer) {
        MinecraftServer server = forPlayer.getServer();
        if(server == null) return null;

        RegistryKey<World> dimensionId = forPlayer.world.getRegistryKey();
        ServerWorld world = server.getWorld(dimensionId);
        if (world == null) return null;

        GoldPlayerEntity instance = new GoldPlayerEntity(Midas.GOLD_PLAYER, world);
        instance.ofPlayer = forPlayer;
        instance.inventory = forPlayer.getInventory().main.stream().map(MidasApi::turnToGold).toList();
        instance.equipStack(EquipmentSlot.HEAD, MidasApi.turnToGold(forPlayer.getEquippedStack(EquipmentSlot.HEAD)));
        instance.equipStack(EquipmentSlot.CHEST, MidasApi.turnToGold(forPlayer.getEquippedStack(EquipmentSlot.CHEST)));
        instance.equipStack(EquipmentSlot.LEGS, MidasApi.turnToGold(forPlayer.getEquippedStack(EquipmentSlot.LEGS)));
        instance.equipStack(EquipmentSlot.FEET, MidasApi.turnToGold(forPlayer.getEquippedStack(EquipmentSlot.FEET)));
        instance.equipStack(EquipmentSlot.MAINHAND, MidasApi.turnToGold(forPlayer.getEquippedStack(EquipmentSlot.MAINHAND)));
        instance.equipStack(EquipmentSlot.OFFHAND, MidasApi.turnToGold(forPlayer.getEquippedStack(EquipmentSlot.OFFHAND)));
        instance.setHealth(20);
        instance.unsetRemoved();
        MidasApi.turnToGold(instance);
        world.spawnEntity(instance);
        return instance;
    }

    @Override
    protected boolean burnsInDaylight() {
        return false;
    }

    @Override
    public boolean isUndead() {
        return false;
    }

    public PlayerEntity getPlayer() {
        return ofPlayer;
    }

    @Override
    protected void dropInventory() {
        inventory.forEach(this::dropStack);
        getArmorItems().forEach(this::dropStack);
        if(getOffHandStack() != ItemStack.EMPTY) dropStack(getOffHandStack());
        super.dropInventory();
    }
}
