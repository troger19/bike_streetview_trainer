package com.itible.bike.util;

public class Util {

    public static String formatDuration(long durationInSeconds) {
        long seconds = durationInSeconds % 60;
        long minutes = durationInSeconds / 60;
        if (minutes >= 60) {
            long hours = minutes / 60;
            minutes %= 60;
            if (hours >= 24) {
                long days = hours / 24;
                return String.format("%d days %02d:%02d:%02d", days, hours % 24, minutes, seconds);
            }
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
        return String.format("00:%02d:%02d", minutes, seconds);
    }
}
