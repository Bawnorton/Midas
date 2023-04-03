package com.bawnorton.midas.api;

import com.bawnorton.midas.access.EntityAccess;
import com.bawnorton.midas.access.PlayerEntityAccess;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;

public class MidasApi {
    public static void cursePlayer(ServerPlayerEntity player) {
        if(player instanceof PlayerEntityAccess playerEntityAccess) {
            playerEntityAccess.setCursed(true);
        }
    }

    public static void cleansePlayer(ServerPlayerEntity player) {
        if(player instanceof PlayerEntityAccess playerEntityAccess) {
            playerEntityAccess.setCursed(false);
        }
    }

    public static void turnToGold(Entity entity) {
        if(entity instanceof EntityAccess entityAccess) {
            entityAccess.turnToGold();
        }
    }
}
