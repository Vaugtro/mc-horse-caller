package com.horsecall.pathfind;

import com.horsecall.pathfind.runnable.SearchEntitiesInRangeRunnable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Thread.sleep;

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
		while (PRINT_KEY_BINDING.wasPressed()) {

			assert client.player != null;
			assert client.getServer() != null;


			UUID playerUuid = client.player.getUuid();

			// Get the player position
			double x = client.player.getX();
			double y = client.player.getY();
			double z = client.player.getZ();

			// Set a box search range around the player position
			Box searchRange = new Box(x + 32, y + 32, z + 16, x - 32, y - 32, z - 16);

			// Get the server overworld where the player is
			ServerWorld world = client.getServer().getOverworld();

			// Set a predicate to set multiple conditions on the entity search
			Predicate<Entity> predicate = entity -> entity instanceof Tameable;

			// Create a runnable thread to search the entities
			SearchEntitiesInRangeRunnable search = new SearchEntitiesInRangeRunnable(searchRange, world, predicate);

			// Execute the async runnable thread on the server
			CompletableFuture<Void> completableFutureSearch = client.getServer().submit(search);
			
			// Wait for the search to finish
			try {
				completableFutureSearch.get();
			} catch (InterruptedException | ExecutionException e) {
				throw new RuntimeException(e);
			}

            // Iterate through the search results
			for (Entity entity : search.getResults()) {
				if (entity instanceof AbstractHorseEntity horseEntity) {

					if (horseEntity.isTame()) {
						client.player.sendMessage(Text.literal(String.format("Horse name: %s", horseEntity.getOwnerUuid())), false);
					}
				}
			}

			client.player.sendMessage(Text.literal("The Key MINUS was pressed!"), false);
		}
	}

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ClientTickEvents.END_CLIENT_TICK.register(HorsecallerClient::onEndTick);
	}
}