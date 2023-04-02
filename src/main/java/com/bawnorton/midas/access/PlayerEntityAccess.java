package com.bawnorton.midas.access;

import net.minecraft.entity.player.PlayerEntity;

public interface PlayerEntityAccess extends EntityAccess {
    boolean isCursed();
    void setCursed(boolean cursed);
    void killedByGold(PlayerEntity player);
}
