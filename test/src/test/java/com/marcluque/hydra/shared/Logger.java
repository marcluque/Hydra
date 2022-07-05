package com.marcluque.hydra.shared;

import java.util.List;

public class Logger {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";

    public static void logInfo(String info) {
        System.out.printf("%s[INFO] %s%s%n", ANSI_BLUE, info, ANSI_RESET);
    }

    public static void logDebug(String debug) {
        System.out.printf("%s[DEBUG] %s%s%n", ANSI_CYAN, debug, ANSI_RESET);
    }

    public static void logSuccess(String success) {
        System.out.printf("%s[SUCCESS] %s%s%n", ANSI_GREEN, success, ANSI_RESET);
    }

    public static void flushMetrics(List<Measurement> measurements) {
        for (Measurement measurement : measurements) {
            long time = measurement.getMeasuredTime();
            System.out.printf(measurement.getFormatString(), ANSI_PURPLE, ANSI_RESET, time, time / 1000, time / 1_000_000);
        }
        measurements.clear();
    }
}
