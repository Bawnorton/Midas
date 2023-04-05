package com.bawnorton.midas.network.packet;

import com.bawnorton.midas.access.DataSaverAccess;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

public class MidasSyncPlayerDataS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf buf, PacketSender packetSender) {
        if(client.player instanceof DataSaverAccess dataAccess) {
            NbtCompound midasData = dataAccess.getMidasData();
            midasData.putBoolean("cursed", buf.readBoolean());
            midasData.putBoolean("gold", buf.readBoolean());
        }
    }
}
