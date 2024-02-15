package com.horsecall.pathfind.networking.client.packet;

import com.horsecall.pathfind.HorsecallerClient;
import com.horsecall.pathfind.util.client.LOCK;
import com.horsecall.pathfind.util.data.EntityData;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import java.util.List;

public class HorseSearch{

    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
                               PacketByteBuf receiveBuffer, PacketSender responseSender) {

        List<EntityData> results = receiveBuffer.readList(PacketByteBuf::readByteArray).stream().map(EntityData.Serializer::deserialize).toList();
        System.out.println(results);

        LOCK.horseSearch.toggle(); // TODO: Maybe replace by a map, avoiding conflicts with players
    }
}
