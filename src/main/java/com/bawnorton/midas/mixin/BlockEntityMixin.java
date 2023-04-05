package com.bawnorton.midas.mixin;

import com.bawnorton.midas.access.DataSaverAccess;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin implements DataSaverAccess {
    NbtCompound midasData;

    @Override
    public NbtCompound getMidasData() {
        if(midasData == null) {
            midasData = new NbtCompound();
        }
        return midasData;
    }

    @Override
    public boolean isGold() {
        return false;
    }

    @Override
    public void setGold(boolean gold) {
    }
}
