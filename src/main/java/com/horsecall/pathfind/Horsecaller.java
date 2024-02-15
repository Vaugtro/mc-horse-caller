package com.horsecall.pathfind;

import com.horsecall.pathfind.networking.C2SPackets;
import com.horsecall.pathfind.util.ID;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Horsecaller implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(ID.MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		C2SPackets.register();
		LOGGER.info("Hello Fabric world!");

		//ServerSidePacketRegistryImpl.INSTANCE.register(SEARCH_HORSES_PACKET_ID);
	}
}