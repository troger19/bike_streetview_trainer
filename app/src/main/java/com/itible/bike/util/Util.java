package com.itible.bike.util;

import java.util.List;

public class Util {

    /**
     * Format duration of the training
     *
     * @param durationInSeconds durationInSeconds
     * @return readable format
     */
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

    /**
     * Create Google maps route with points and distance
     *
     * @param longitudes longitudes
     * @param latitudes  latitudes
     * @return bike route map in google maps
     */
    public static String createRouteUrl(List<String> longitudes, List<String> latitudes) {
        String s = "https://www.google.com/maps/dir/";
        for (int i = 0; i < latitudes.size() - 2; i++) {
            s = s.concat(longitudes.get(i)).concat(",").concat(latitudes.get(i)).concat("/");
        }
        s = s.concat("@").concat(longitudes.get(longitudes.size() - 1)).concat(",").concat(latitudes.get(latitudes.size() - 1));

        return s.concat("/data=!4m2!4m1!3e2");
    }
}
