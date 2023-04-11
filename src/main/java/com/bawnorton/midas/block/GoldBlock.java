package com.bawnorton.midas.block;

import com.bawnorton.midas.block.entity.GoldBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.MiningToolItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class GoldBlock extends BlockWithEntity {
    public GoldBlock() {
        super(Settings.copy(Blocks.GOLD_BLOCK));
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        if(!world.isClient && !player.isCreative()) {
            Item brokenWith = player.getMainHandStack().getItem();
            if(!(brokenWith instanceof MiningToolItem miningToolItem)) return;
            if(!miningToolItem.isSuitableFor(state)) return;

            GoldBlockEntity blockEntity = (GoldBlockEntity) world.getBlockEntity(pos);
            if(blockEntity != null) {
                blockEntity.dropOriginal();
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new GoldBlockEntity(pos, state);
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
