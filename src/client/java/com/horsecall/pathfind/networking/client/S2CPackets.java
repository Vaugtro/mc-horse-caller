package com.horsecall.pathfind.networking.client;

import com.horsecall.pathfind.util.ID;
import com.horsecall.pathfind.networking.client.packet.HorseSearch;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class S2CPackets {

    // Server to Client Packet Register
    public static void register(){
        ClientPlayNetworking.registerGlobalReceiver(ID.Packet.SEARCH_HORSE_SERVER_ID, HorseSearch::receive);
    }
}
