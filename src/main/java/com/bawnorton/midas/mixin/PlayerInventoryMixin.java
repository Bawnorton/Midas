package com.bawnorton.midas.mixin;

import com.bawnorton.midas.api.MidasApi;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@SuppressWarnings("unchecked")
@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {
    @Shadow @Final public PlayerEntity player;

    @ModifyArg(method = "insertStack(ILnet/minecraft/item/ItemStack;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;set(ILjava/lang/Object;)Ljava/lang/Object;"), index = 1)
    private <E> E insertStack(E element) {
        if (MidasApi.isCursed(player)) {
            return (E) MidasApi.turnToGold((ItemStack) element);
        }
        return element;
    }

    @ModifyArg(method = "insertStack(ILnet/minecraft/item/ItemStack;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;addStack(ILnet/minecraft/item/ItemStack;)I"), index = 1)
    private ItemStack addSlotStack(ItemStack element) {
        if (MidasApi.isCursed(player)) {
            return MidasApi.turnToGold(element);
        }
        return element;
    }

    @ModifyArg(method = "insertStack(ILnet/minecraft/item/ItemStack;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;addStack(Lnet/minecraft/item/ItemStack;)I"), index = 0)
    private ItemStack addStack(ItemStack element) {
        if (MidasApi.isCursed(player)) {
            return MidasApi.turnToGold(element);
        }
        return element;
    }
}
