package com.horsecall.pathfind.networking.client.packet;

import com.horsecall.pathfind.helper.EntityWithDistance;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import java.util.List;

public class HorseSearch{

    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
                               PacketByteBuf receiveBuffer, PacketSender responseSender) {

        List<EntityWithDistance> results = receiveBuffer.readList(PacketByteBuf::readByteArray).stream().map(EntityWithDistance.Serializer::desserialize).toList();
        System.out.println(results);
    }
}
