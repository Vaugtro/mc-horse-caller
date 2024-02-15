package com.horsecall.pathfind;

import com.horsecall.pathfind.util.ID;
import net.fabricmc.api.DedicatedServerModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HorsecallerServer implements DedicatedServerModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger(ID.MOD_ID + "/Server");
    @Override
    public void onInitializeServer() {
        LOGGER.info("POOP");
    }
}
