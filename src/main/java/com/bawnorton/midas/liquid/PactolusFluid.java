package com.bawnorton.midas.liquid;

import com.bawnorton.midas.api.MidasApi;
import com.bawnorton.midas.block.GoldBlock;
import com.bawnorton.midas.block.MidasBlocks;
import com.bawnorton.midas.item.MidasItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;

public abstract class PactolusFluid extends MidasFluid {
    @Override
    public Fluid getFlowing() {
        return MidasFluids.FLOWING_PACTOLUS_WATER;
    }

    @Override
    public Fluid getStill() {
        return MidasFluids.STILL_PACTOLUS_WATER;
    }

    @Override
    public Item getBucketItem() {
        return MidasItems.PACTOLUS_BUCKET;
    }

    @Override
    public void onScheduledTick(World world, BlockPos pos, FluidState state) {
        super.onScheduledTick(world, pos, state);
        BlockPos[] neighbors = new BlockPos[6];
        for(Direction dir : Direction.values()) {
            neighbors[dir.ordinal()] = pos.offset(dir);
        }
        for(BlockPos neighbor : neighbors) {
            BlockState neighborState = world.getBlockState(neighbor);
            Block neighborBlock = neighborState.getBlock();
            if(neighborBlock instanceof GoldBlock goldBlock) {
                MidasApi.cleanse(world, neighbor, goldBlock);
            }
        }
    }

    @Override
    protected BlockState toBlockState(FluidState state) {
        return MidasBlocks.pactolus_WATER.getDefaultState().with(FluidBlock.LEVEL, getBlockStateLevel(state));
    }

    public static class Flowing extends PactolusFluid {
        @Override
        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getLevel(FluidState state) {
            return state.get(LEVEL);
        }

        @Override
        public boolean isStill(FluidState state) {
            return false;
        }
    }

    public static class Still extends PactolusFluid {
        @Override
        public int getLevel(FluidState state) {
            return 8;
        }

        @Override
        public boolean isStill(FluidState state) {
            return true;
        }
    }
}
