package com.horsecall.pathfind;

import com.horsecall.pathfind.helper.EntityWithDistance;
import com.horsecall.pathfind.task.SearchEntitiesInRangeSupplier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.HorseColor;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

			List<EntityWithDistance> result = SearchEntitiesInRangeSupplier(client);

			client.player.sendMessage(Text.literal("The Key MINUS was pressed!"), false);
		}
	}
	
	private static List<EntityWithDistance> SearchEntitiesInRangeSupplier(MinecraftClient client) {

		assert client.player != null;
		assert client.getServer() != null;

		// Get the player UUID
		UUID playerUuid = client.player.getUuid();

		// Get the player position
		double x = client.player.getX();
		double y = client.player.getY();
		double z = client.player.getZ();

		// Set a box search range around the player position
		Box searchRange = new Box(x + 128, y + 32, z + 128, x - 128, y - 32, z - 128);

		// Get the server overworld where the player is
		ServerWorld world = client.getServer().getOverworld();

		// Set a predicate to set multiple conditions on the entity search
		Predicate<Entity> predicate = entity -> ((entity instanceof AbstractHorseEntity horseEntity) && horseEntity.getOwnerUuid() != null) && !horseEntity.hasPassengers() && horseEntity.getOwnerUuid().compareTo(playerUuid) == 0;;

		// Create a runnable thread to search the entities

		// Execute the async runnable thread on the server
		CompletableFuture<List<EntityWithDistance>> completableFutureSearch = client.getServer().submit(new SearchEntitiesInRangeSupplier(searchRange, world, predicate, client.player));

		// Wait for the search to finish and return the results
		try {
			return completableFutureSearch.get();
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}


	}

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ClientTickEvents.END_CLIENT_TICK.register(HorsecallerClient::onEndTick);
	}
}