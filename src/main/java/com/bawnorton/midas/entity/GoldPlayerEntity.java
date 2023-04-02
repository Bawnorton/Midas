package com.bawnorton.midas.entity;

import com.bawnorton.midas.access.EntityAccess;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class GoldPlayerEntity extends ZombieEntity {
    public GoldPlayerEntity(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    public static void create(ServerPlayerEntity forPlayer) {
        MinecraftServer server = forPlayer.getServer();
        if(server == null) return;

        RegistryKey<World> dimensionId = forPlayer.world.getRegistryKey();
        Vec3d pos = forPlayer.getPos();
        String name = "Gold " + forPlayer.getName().getString();
        ServerWorld world = server.getWorld(dimensionId);
        if (world == null) return;

        GoldPlayerEntity instance = new GoldPlayerEntity(EntityType.ZOMBIE, world);
        instance.setCustomName(Text.literal(name));
        instance.setPosition(pos);
        instance.setRotation(forPlayer.getYaw(), forPlayer.getPitch());
        instance.setHealth(20);
        instance.unsetRemoved();
        ((EntityAccess) instance).turnToGold();
        world.spawnEntity(instance);
    }

    @Override
    protected boolean burnsInDaylight() {
        return false;
    }

    @Override
    public boolean isUndead() {
        return false;
    }
}
