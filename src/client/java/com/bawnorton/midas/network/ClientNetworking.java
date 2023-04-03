package com.bawnorton.midas.network;

import com.bawnorton.midas.network.packet.MidasSyncDataS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientNetworking {
    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(Networking.MIDAS_SYNC, MidasSyncDataS2CPacket::receive);
    }
}
