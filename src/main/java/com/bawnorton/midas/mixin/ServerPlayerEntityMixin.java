package com.bawnorton.midas.mixin;

import com.bawnorton.midas.Midas;
import com.bawnorton.midas.access.PlayerEntityAccess;
import com.bawnorton.midas.entity.GoldPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin implements PlayerEntityAccess {
    @Shadow public abstract boolean damage(DamageSource source, float amount);

    @Shadow public abstract ServerWorld getWorld();

    private int ticks = 0;
    private boolean canTurnToGold = true;
    private final Random random = Random.create();

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        ticks++;
        if(this.isCursed() && ticks % 20 == 0) {
            EquipmentSlot[] slots = EquipmentSlot.values();
            EquipmentSlot slot = slots[random.nextInt(slots.length)];
            ItemStack stack = ((LivingEntity) (Object) this).getEquippedStack(slot);
            ((LivingEntity) (Object) this).equipStack(slot, Midas.goldify(stack));
        }
    }

    @Override
    public void killedByGold(PlayerEntity player) {
        if(canTurnToGold && !player.world.isClient) {
            damage(getWorld().getDamageSources().playerAttack(player), Float.MAX_VALUE);
            ((ServerPlayerEntity) (Object) this).dropItem(Items.GOLD_NUGGET);
            canTurnToGold = false;
            turnToGold();
        }
    }

    @Override
    public void turnToGold() {
        GoldPlayerEntity.create((ServerPlayerEntity) (Object) this);
    }
}
