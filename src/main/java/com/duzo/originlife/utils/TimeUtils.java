package com.duzo.originlife.utils;

import java.util.Locale;

public class TimeUtils {
    public static float secondsToHours(int seconds) {
        return (seconds / 60f) / 60f;
    }
    public static String getDisplayText(int val) {
        int hours = val / 3600;
        int minutes = (val % 3600) / 60;
        int seconds = val % 60;

        return String.format(Locale.ENGLISH,"%02d:%02d:%02d", hours, minutes, seconds);
    }
}
