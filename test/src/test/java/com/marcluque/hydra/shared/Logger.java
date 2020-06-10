package de.datasecs.hydra.shared;

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

    public static void printMetrics(long[] measures) {
        System.out.printf("%s[MEASUREMENT] Connection client: %s%d ns = %d µs = %d ms%n", ANSI_PURPLE, ANSI_RESET, measures[1], measures[1] / 1000, measures[1] / 1_000_000);
        System.out.printf("%s[MEASUREMENT] Packets (n=1000, size_packet=10 byte): %s%d ns = %d µs = %d ms%n", ANSI_PURPLE, ANSI_RESET, measures[2], measures[2] / 1000, measures[2] / 1_000_000);
        System.out.printf("%s[MEASUREMENT] String-List (n=1000, size_string=5 byte): %s%d ns = %d µs = %d ms%n", ANSI_PURPLE, ANSI_RESET, measures[3], measures[3] / 1000, measures[3] / 1_000_000);
        System.out.printf("%s[MEASUREMENT] String-Array (n=1000, size_string=5 byte): %s%d ns = %d µs = %d ms%n", ANSI_PURPLE, ANSI_RESET, measures[4], measures[4] / 1000, measures[4] / 1_000_000);
        System.out.printf("%s[MEASUREMENT] Shutdown server: %s%d ns = %d µs = %d ms%n", ANSI_PURPLE, ANSI_RESET, measures[5], measures[5] / 1000, measures[5] / 1_000_000);
        System.out.printf("%s[MEASUREMENT] Shutdown client: %s%d ns = %d µs = %d ms%n", ANSI_PURPLE, ANSI_RESET, measures[6], measures[6] / 1000, measures[6] / 1_000_000);
    }
}
