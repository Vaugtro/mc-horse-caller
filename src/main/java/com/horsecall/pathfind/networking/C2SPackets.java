package com.horsecall.pathfind.networking;

import com.horsecall.pathfind.networking.packet.HorsePathfind;
import com.horsecall.pathfind.util.ID;
import com.horsecall.pathfind.networking.packet.HorseSearch;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class C2SPackets {

    // Client to Server Packet Register
    public static void register(){
        ServerPlayNetworking.registerGlobalReceiver(ID.Packet.SEARCH_HORSE_CLIENT_ID, HorseSearch::receive);
        ServerPlayNetworking.registerGlobalReceiver(ID.Packet.PATHFIND_HORSE_CLIENT_ID, HorsePathfind::receive);
    }
}
