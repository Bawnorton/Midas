package com.bawnorton.midas.access;

public interface PlayerEntityAccess extends EntityAccess {
    boolean isCursed();
    void setCursed(boolean cursed);
    boolean didDieToGold();
    void dieToGold();
}
