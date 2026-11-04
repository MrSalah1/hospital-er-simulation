package util;

public final class ConsoleLogger {
    private ConsoleLogger() {
    }

    public static synchronized void log(String message) {
        System.out.println(message);
    }

    public static synchronized void logAll(String... messages) {
        for (String message : messages) {
            System.out.println(message);
        }
    }
}
