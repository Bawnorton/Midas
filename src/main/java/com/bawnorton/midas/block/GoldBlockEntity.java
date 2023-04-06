package com.bawnorton.midas.block;

import com.bawnorton.midas.Midas;
import com.bawnorton.midas.util.GoldBlockData;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class GoldBlockEntity extends BlockEntity implements RenderAttachmentBlockEntity {
    private GoldBlockData data = new GoldBlockData(Blocks.GOLD_BLOCK);

    public GoldBlockEntity(BlockPos pos, BlockState state) {
        super(Midas.GOLD_BLOCK_ENTITY, pos, state);
    }

    public void setData(Block original) {
        this.data = new GoldBlockData(original);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.put("GoldBlockData", data.toNbt());
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.data = GoldBlockData.fromNbt(nbt.getCompound("GoldBlockData"));
        super.readNbt(nbt);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public @Nullable Object getRenderAttachmentData() {
        return data;
    }
}
