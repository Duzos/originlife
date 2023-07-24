package com.duzo.originlife.utils;

public class ColourUtils {
    private static final int WHITE = 0xffffff;
    private static final int PINK = 0xeb91dd;
    private static final int PURPLE = 0xb891eb;
    private static final int BLUE = 0x3e9af0;
    private static final int GREEN = 0x3ef062;
    private static final int YELLOW = 0xeaf03e;
    private static final int ORANGE = 0xf09a3e;
    private static final int RED = 0xf03e3e;

    public static int getColourFromOrigin(int origin) {
        return switch (origin) {
            case 1 -> RED;
            case 2 -> ORANGE;
            case 3 -> YELLOW;
            case 4 -> GREEN;
            case 5 -> BLUE;
            case 6 -> PURPLE;
            case 7 -> PINK;
            default -> WHITE;
        };
    }
}
