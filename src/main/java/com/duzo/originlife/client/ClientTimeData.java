package com.duzo.originlife.client;

public class ClientTimeData {
    private static int playerTime;

    public static void set(int seconds) {
        ClientTimeData.playerTime = seconds;
    }
    public static int getPlayerTime() {
        return playerTime;
    }
}
