package com.horsecall.pathfind.util.client;

public class LOCK {

    public static class horseSearch{

        private static boolean lock = false;
        public static void toggle(){
            lock = !lock;
        }

        public static boolean get(){
            return lock;
        }
    }
}
