package com.bawnorton.midas.access;

import net.minecraft.nbt.NbtCompound;

public interface DataSaverAccess {
    NbtCompound getMidasData();

    boolean isCursed();
    boolean isGold();

    void setCursed(boolean cursed);
    void setGold(boolean gold);
}
