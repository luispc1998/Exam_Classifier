package logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ConsoleLogger {


    private static ConsoleLogger instance;

    private final StringColorer sc;

    private final StringBuilder uncoloredMessages;
    private final StringBuilder coloredMessages;

    private final ErrorManager errorManager;

    private ConsoleLogger() {
        sc = new StringColorer();
        uncoloredMessages = new StringBuilder();
        coloredMessages = new StringBuilder();
        errorManager = new ErrorManager();
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
        //System.out.println(finalMessage);
        logMessage(finalMessage);
    }

    public void logError(String msg) {
        String finalMessage = "[ERROR] " + msg;
        //System.out.println(sc.colorYellow(finalMessage));
        //System.out.println(finalMessage);
        logMessage(finalMessage);
        errorManager.addError(finalMessage);
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

    public ErrorManager getErrorManager() {
        return errorManager;
    }


    /**
     * Writes the logs of the input loading.
     */
    public void writeInputLogData(String outputDirectory) {
        try (BufferedWriter bfWriterUncolored = new BufferedWriter(new FileWriter(outputDirectory + "inputUncoloredLog.txt"));
             BufferedWriter bfWriterColored = new BufferedWriter(new FileWriter(outputDirectory + "inputColoredLog.txt"));
             BufferedWriter bfWriterErrorLog = new BufferedWriter(new FileWriter(outputDirectory + "errorLog.txt"))){

            bfWriterUncolored.write(ConsoleLogger.getConsoleLoggerInstance().getUncoloredMessages());
            bfWriterColored.write(ConsoleLogger.getConsoleLoggerInstance().getColoredMessages());
            bfWriterErrorLog.write(ConsoleLogger.getConsoleLoggerInstance().getErrorManager().getFormattedString());
        }catch (IOException e) {
            ConsoleLogger.getConsoleLoggerInstance().logError("Could not write output files of initial logging.");
        }
    }

}
