package com.horsecall.pathfind;

import com.horsecall.pathfind.runnable.SearchEntitiesInRangeRunnable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HorsecallerClient implements ClientModInitializer {

	private static final int BACALO_DISTANCE = 3;
	private static final List<AbstractHorseEntity> bacalosList = new ArrayList<>();

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

			SearchEntitiesInRangeRunnable search = getSearchEntitiesInRangeRunnable(client);

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
					if (horseEntity.isTame() && client.player.getUuid() == horseEntity.getOwnerUuid()) {
						client.player.playSound(SoundEvents.ENTITY_HORSE_ANGRY, 0.8f, 1);
						client.player.sendMessage(
							Text.literal(
								String.format("Horse UUID: %s, Player UUID: %s\n, Pos: %s\n",
										horseEntity.getUuid(),
										horseEntity.getOwnerUuid(),
										horseEntity.getPos()
								)
							), false
						);

						bacalosList.add(horseEntity);
						break;
					}
				}
			}

			client.player.sendMessage(Text.literal("The Key MINUS was pressed!"), false);
		}

		client.submit(() -> {
			if (!bacalosList.isEmpty()) {
				var bacaloPrime = bacalosList.get(0);
				var bacaloDistance = bacaloPrime.distanceTo(client.player);
				var x = client.player.getX() - BACALO_DISTANCE;
				var y = client.player.getY() - BACALO_DISTANCE;
				var z = client.player.getZ() - BACALO_DISTANCE;
				var nav = bacaloPrime.getNavigation();

				if (bacaloDistance > BACALO_DISTANCE) {
					client.getServer().submit(() -> nav.startMovingTo(x, y, z, 2));
				} else {
					System.out.println("Bacalo Distance: " + bacaloDistance);
					bacalosList.remove(0);
				}
			}
		});
    }

	@NotNull
	private static SearchEntitiesInRangeRunnable getSearchEntitiesInRangeRunnable(MinecraftClient client) {
		UUID playerUuid = client.player.getUuid();

		// Get the player position
		double x = client.player.getX();
		double y = client.player.getY();
		double z = client.player.getZ();

		// Set a box search range around the player position
		Box searchRange = new Box(x + 32, y + 32, z + 32, x - 32, y - 32, z - 32);

		// Get the server overworld where the player is
		ServerWorld world = client.getServer().getOverworld();

		// Set a predicate to set multiple conditions on the entity search
		Predicate<Entity> predicate = entity -> entity instanceof Tameable;

		// Create a runnable thread to search the entities
		SearchEntitiesInRangeRunnable search = new SearchEntitiesInRangeRunnable(searchRange, world, predicate);
		return search;
	}

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ClientTickEvents.END_CLIENT_TICK.register(HorsecallerClient::onEndTick);
	}
}