package com.horsecall.pathfind;

import com.horsecall.pathfind.networking.client.S2CPackets;
import com.horsecall.pathfind.util.identifier.ID;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class HorsecallerClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Horse Caller");

	// Set a default keybinding for calling the horse. Being the minus key the default key.
	private static final KeyBinding PRINT_KEY_BINDING = KeyBindingHelper.registerKeyBinding(
			new KeyBinding(
					"key.horse-caller.print",
					InputUtil.Type.KEYSYM,
					GLFW.GLFW_KEY_MINUS,
					"key.category.call-horse"
			)
	);

	private static void onEndTick(MinecraftClient client) {
		assert client.player != null;
		assert client.getServer() != null;

		while (PRINT_KEY_BINDING.wasPressed()) {

			PacketByteBuf sendBuffer = PacketByteBufs.create();

			ClientPlayNetworking.send(ID.Packet.SEARCH_HORSE_CLIENT_ID, sendBuffer);


			client.player.sendMessage(Text.literal("The Key MINUS was pressed!"), false);
		}
	}

	@Override
	public void onInitializeClient() {

		S2CPackets.register();

		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ClientTickEvents.END_CLIENT_TICK.register(HorsecallerClient::onEndTick);
	}
}