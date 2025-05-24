package com.nfdobbs.emptyhorizons.util;

public class TimeString {

    public static String CreateTimeString(int timeInSeconds) {
        int minutes = timeInSeconds / 60;
        int seconds = timeInSeconds % 60;

        int hours = minutes / 60;
        minutes = minutes % 60;

        String outputTime = "";
        if (hours > 0) {
            outputTime = String.format("%d:%02d:%02d", hours, minutes, seconds);
        } else if (minutes > 0) {
            outputTime = String.format("%d:%02d", minutes, seconds);
        } else {
            outputTime = String.format("%d", seconds);
        }

        return outputTime;
    }

    public static String CreatePrettyTimeString(int timeInSeconds) {
        int minutes = timeInSeconds / 60;
        int seconds = timeInSeconds % 60;

        int hours = minutes / 60;
        minutes = minutes % 60;

        String outputTime = "";
        if (hours > 0) {
            outputTime = String.format("%dh %02dm %02ds", hours, minutes, seconds);
        } else if (minutes > 0) {
            outputTime = String.format("%dm %02ds", minutes, seconds);
        } else {
            outputTime = String.format("%ds", seconds);
        }

        return outputTime;
    }
}
