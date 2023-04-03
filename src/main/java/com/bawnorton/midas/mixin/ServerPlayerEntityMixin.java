package com.bawnorton.midas.mixin;

import com.bawnorton.midas.Midas;
import com.bawnorton.midas.access.PlayerEntityAccess;
import com.bawnorton.midas.entity.GoldPlayerEntity;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.DeathMessageS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements PlayerEntityAccess {
    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Shadow public abstract boolean damage(DamageSource source, float amount);

    @Shadow public abstract ServerWorld getWorld();

    @Shadow public ServerPlayNetworkHandler networkHandler;
    @Shadow @Final public MinecraftServer server;
    private int ticks = 0;
    private boolean diedToGold;
    private final Random random = Random.create();

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        ticks++;
        if(this.isCursed() && ticks % 20 == 0) {
            EquipmentSlot[] slots = EquipmentSlot.values();
            EquipmentSlot slot = slots[random.nextInt(slots.length)];
            ItemStack stack = getEquippedStack(slot);
            this.equipStack(slot, Midas.goldify(stack));
        }
    }

    @Override
    public void turnToGold() {
        if(!this.world.isClient && !this.isCreative() && !didDieToGold()) {
            GoldPlayerEntity goldPlayer = GoldPlayerEntity.create((ServerPlayerEntity) (Object) this);
            if(goldPlayer == null) return;
            goldPlayer.copyPositionAndRotation(this);
            dieToGold();
            setHealth(0);
            Text text = Text.translatable("death.attack.midas", getDisplayName());
            networkHandler.sendPacket(new DeathMessageS2CPacket(getDamageTracker(), text));
            server.getPlayerManager().broadcast(text, false);
        }
    }

    @Override
    public void dieToGold() {
        diedToGold = true;
    }

    @Override
    public boolean didDieToGold() {
        return diedToGold;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("cursed", isCursed());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        setCursed(nbt.getBoolean("cursed"));
    }
}
