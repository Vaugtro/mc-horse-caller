package com.horsecall.pathfind.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class HorsePathfind {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf receiveBuffer, PacketSender responseSender) {

        player.sendMessageToClient(Text.literal("Será que ta dando bom?"), false);

        UUID horseUUID = receiveBuffer.readUuid();
        Thread continuePathThread = getThread(player, horseUUID, server);

        Thread waitThread = new Thread(() -> {
            try {
                continuePathThread.join(15000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        waitThread.start();
    }

    @NotNull
    private static Thread getThread(Entity entity, UUID horseUUID, MinecraftServer server) {

        AbstractHorseEntity horse = (AbstractHorseEntity) server.getOverworld().getEntity(horseUUID);
        EntityNavigation horseNavigation = horse.getNavigation();
        horseNavigation.setRangeMultiplier(10.0f);

        Thread continuePathThread = new Thread(() -> {
            while (horse.getPos().distanceTo(entity.getPos()) > 4.0f){

                System.out.println("Outer Loop");

                server.submitAndJoin(() -> {
                    horseNavigation.stop();
                    horseNavigation.startMovingTo(entity, 2);
                });
                System.out.println(horseNavigation.isIdle());

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        continuePathThread.start();
        return continuePathThread;
    }
}
