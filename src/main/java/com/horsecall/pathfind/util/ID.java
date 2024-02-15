package com.horsecall.pathfind.util;

import com.horsecall.pathfind.Horsecaller;
import net.minecraft.util.Identifier;

public class ID {

    public static final String MOD_ID = "horsecaller";
    public static class Packet {
        public static final Identifier SEARCH_HORSE_SERVER_ID = new Identifier(MOD_ID, "serversearch");
        public static final Identifier SEARCH_HORSE_CLIENT_ID = new Identifier(MOD_ID, "clientsearch");
    }

    public static class Sprite {

    }
}
