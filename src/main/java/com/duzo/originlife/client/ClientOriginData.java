package com.duzo.originlife.client;

public class ClientOriginData {
    private static int playerOrigin;

    public static void set(int origin) {
        ClientOriginData.playerOrigin = origin;
    }
    public static int getPlayerOrigin() {
        return playerOrigin;
    }
}
