package com.horsecall.pathfind;

import net.fabricmc.api.DedicatedServerModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HorsecallerServer implements DedicatedServerModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("horse-caller/Server");
    @Override
    public void onInitializeServer() {
        LOGGER.info("POOP");
    }
}
