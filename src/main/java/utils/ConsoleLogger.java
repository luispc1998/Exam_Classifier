package utils;

public class ConsoleLogger {


    private static ConsoleLogger instance;

    private final StringColorer sc;

    private final StringBuilder uncoloredMessages;
    private final StringBuilder coloredMessages;

    private ConsoleLogger() {
        sc = new StringColorer();
        uncoloredMessages = new StringBuilder();
        coloredMessages = new StringBuilder();
    }

    public static ConsoleLogger getConsoleLoggerInstance(){
        if (instance == null) {
            instance = new ConsoleLogger();
        }
        return instance;
    }

    public void logInfo(String msg) {
        String finalMessage = "[INFO] " + msg;
        //System.out.println(sc.colorBlue(finalMessage));
        System.out.println(finalMessage);
        logMessage(finalMessage);
    }

    public void logWarning(String msg) {
        String finalMessage = "[WARN] " + msg;
        //System.out.println(sc.colorYellow(finalMessage));
        System.out.println(finalMessage);
        logMessage(finalMessage);
    }

    public void logError(String msg) {
        String finalMessage = "[ERROR] " + msg;
        //System.out.println(sc.colorRed(finalMessage));
        System.out.println(finalMessage);
        logMessage(finalMessage);
    }

    private void logMessage(String finalMessage) {
        uncoloredMessages.append(finalMessage);
        uncoloredMessages.append("\n");
        coloredMessages.append(sc.colorBlue(finalMessage));
        coloredMessages.append("\n");
    }

    public String getColoredMessages() {
        return coloredMessages.toString();
    }

    public String getUncoloredMessages() {
        return uncoloredMessages.toString();
    }
}
