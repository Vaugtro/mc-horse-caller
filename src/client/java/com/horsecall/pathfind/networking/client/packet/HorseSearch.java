package com.horsecall.pathfind.networking.client.packet;

import com.horsecall.pathfind.util.ID;
import com.horsecall.pathfind.util.client.LOCK;
import com.horsecall.pathfind.util.data.SearchData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import java.util.List;

public class HorseSearch{

    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
                               PacketByteBuf receiveBuffer, PacketSender responseSender) {

        List<SearchData> results = receiveBuffer.readList(PacketByteBuf::readByteArray).stream().map(SearchData.Serializer::deserialize).toList();

        PacketByteBuf sendBuffer = PacketByteBufs.create();

        sendBuffer.writeUuid(results.get(0).getEntityUUID());

        ClientPlayNetworking.send(ID.Packet.PATHFIND_HORSE_CLIENT_ID, sendBuffer);

        LOCK.horseSearch.toggle(); // TODO: Maybe replace by a map, avoiding conflicts with players
    }
}
