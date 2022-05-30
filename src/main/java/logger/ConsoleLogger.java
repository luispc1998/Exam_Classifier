package logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ConsoleLogger {


    private static ConsoleLogger instance;
    private final StringBuilder logMessages;
    private final ErrorManager errorManager;

    private ConsoleLogger() {
        logMessages = new StringBuilder();
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
        logMessage(finalMessage);
    }

    public void logError(String msg) {
        String finalMessage = "[ERROR] " + msg;
        logMessage(finalMessage);
        errorManager.addError(finalMessage);
    }



    private void logMessage(String finalMessage) {
        logMessages.append(finalMessage);
        logMessages.append("\n");
    }

    public String getLogMessages() {
        return logMessages.toString();
    }

    public ErrorManager getErrorManager() {
        return errorManager;
    }


    /**
     * Writes the logs of the input loading.
     */
    public void writeInputLogData(String outputDirectory) {
        try (BufferedWriter bfWriterUncolored = new BufferedWriter(new FileWriter(outputDirectory + "inputUncoloredLog.txt"));
             BufferedWriter bfWriterErrorLog = new BufferedWriter(new FileWriter(outputDirectory + "errorLog.txt"))){

            bfWriterUncolored.write(ConsoleLogger.getConsoleLoggerInstance().getLogMessages());
            String errors = ConsoleLogger.getConsoleLoggerInstance().getErrorManager().getFormattedStringOfErrors();
            bfWriterErrorLog.write(errors);
            System.out.println(errors);


        }catch (IOException e) {
            ConsoleLogger.getConsoleLoggerInstance().logError("Could not write output files of initial logging.");
        }
    }

}
