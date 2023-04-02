package com.bawnorton.midas.mixin;

import com.bawnorton.midas.Midas;
import com.bawnorton.midas.access.BlockEntityAccess;
import com.bawnorton.midas.access.PlayerEntityAccess;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Inject(method = "onSteppedOn", at = @At("HEAD"))
    private void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity, CallbackInfo ci) {
        if(entity instanceof PlayerEntityAccess playerEntityAccess) {
            if(!playerEntityAccess.isCursed() || state.isAir()) return;
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if(blockEntity instanceof BlockEntityAccess access) {
                access.turnToGold();
            }
            if(blockEntity != null) return;

            BlockState goldifiedState = Midas.goldify(state);
            if(goldifiedState != state) {
                world.setBlockState(pos, goldifiedState, Block.NOTIFY_ALL);
            }
        }
    }
}
