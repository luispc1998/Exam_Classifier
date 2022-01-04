package geneticAlgorithm.output;

import domain.DataHandler;
import domain.parsers.ConstrictionParser;
import domain.parsers.ExamParser;
import geneticAlgorithm.Individual;
import utils.ConsoleLogger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;

public class OutputHandler {

    private final String outputDirectory;
    private final String outputFilename;
    private final DataHandler dataHandler;
    private final ExcelWriter excelWriter;

    public OutputHandler(DataHandler dataHandler, String outputFileName,
                         ExcelWriter excelWriter) {
        this.outputDirectory = createOutputDirectory(dataHandler.getConfigurer().getFilePaths("outputBaseDirectory"));
        this.dataHandler = dataHandler;
        this.outputFilename = outputFileName;
        this.excelWriter = excelWriter;

    }

    /**
     * Writes the output files, the excel and the log file.
     */
    public void writeOutputFiles(HashSet<Individual> outputIndividuals, String loggedData)  {
        excelWriter.excelWrite(outputIndividuals, dataHandler, outputDirectory, outputFilename);
        writeLogData(loggedData);
        //writeInputLogData();
    }


    public void writeInputLogData() {
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

    /**
     * Writes the genetic algorithm file.
     */
    private void writeLogData(String loggedData) {
        String path = outputDirectory + "gLog.txt";
        try (BufferedWriter bfWriter = new BufferedWriter(new FileWriter(path))){
            bfWriter.write(loggedData);
        } catch (IOException e) {
            throw new IllegalStateException("Could not write the genetic algorithm log at path: ["
                    + path + "]" );
        }

    }

    /**
     * Creates the output directory for each execution of the program.
     * @param outputBaseDirectory The base directory path in which the outputs of the program will be saved.
     * @return The path of the generated directory in which the results of the program execution will be written.
     */
    public static String createOutputDirectory(String outputBaseDirectory) {

        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.now();

        StringBuilder directoryBuilder = new StringBuilder();
        directoryBuilder.append(outputBaseDirectory);
        directoryBuilder.append(ld.getYear());
        directoryBuilder.append(ld.getMonthValue());
        directoryBuilder.append(ld.getDayOfMonth());

        directoryBuilder.append("_");
        directoryBuilder.append(String.format("%02d", lt.getHour()));
        directoryBuilder.append(String.format("%02d", lt.getMinute()));

        File theDir = new File(directoryBuilder.toString());

        if (!theDir.exists()){
            if (!theDir.mkdirs()) {
                throw new RuntimeException("The directory could not be created. Path: " + theDir);
            }
        }

        return directoryBuilder + "/";
    }
}
