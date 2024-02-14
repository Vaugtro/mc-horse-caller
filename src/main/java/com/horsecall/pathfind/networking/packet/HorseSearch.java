package com.horsecall.pathfind.networking.packet;

import com.horsecall.pathfind.util.identifier.ID;
import com.horsecall.pathfind.helper.EntityWithDistance;
import com.horsecall.pathfind.util.task.SearchEntitiesInRangeSupplier;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;

public class HorseSearch{

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf receiveBuffer, PacketSender responseSender) {

        // Get the player UUID
        UUID playerUuid = player.getUuid();

        // Get the player position
        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();

        // Set a box search range around the player position
        Box searchRange = new Box(x + 128, y + 32, z + 128, x - 128, y - 32, z - 128);

        // Get the server overworld where the player is
        ServerWorld world = server.getOverworld();

        // Set a predicate to set multiple conditions on the entity search
        Predicate<Entity> predicate = entity -> ((entity instanceof AbstractHorseEntity horseEntity) && horseEntity.getOwnerUuid() != null) && !horseEntity.hasPassengers() && horseEntity.getOwnerUuid().compareTo(playerUuid) == 0;

        // Execute the async runnable thread on the server
        CompletableFuture<List<EntityWithDistance>> completableFutureSearch = server.submit(new SearchEntitiesInRangeSupplier(searchRange, world, predicate, player));

        // Wait for the search to finish and return the results
        List<EntityWithDistance> results;

        try {
            results = completableFutureSearch.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        // Instanciate buffer to be sended
        PacketByteBuf sendBuffer = PacketByteBufs.create();

        // Serialize packet to send results to client
        sendBuffer.writeCollection(results, new PacketByteBuf.PacketWriter<EntityWithDistance>() {
            @Override
            public void accept(PacketByteBuf packetByteBuf, EntityWithDistance entityWithDistance) {
                packetByteBuf.writeByteArray(EntityWithDistance.Serializer.serialize(entityWithDistance));
            }
        });

        //ServerPlayNetworking.send(player, ID.Packet.SEARCH_HORSE_SERVER_ID, sendBuffer);

        responseSender.sendPacket(ID.Packet.SEARCH_HORSE_SERVER_ID, sendBuffer);
    }

}
