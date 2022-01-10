package geneticAlgorithm.output;

import domain.DataHandler;
import geneticAlgorithm.Individual;
import utils.ConsoleLogger;
import utils.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class OutputHandler {

    /**
     * The output directory where all the files will be generated.
     */
    private final String outputDirectory;

    /**
     * The name of the output excel file.
     */
    private final String outputFilename;

    /**
     * The {@code DataHandler} instance with all the execution data.
     */
    private final DataHandler dataHandler;

    /**
     * The {@code ExcelWriter} to be used when writing the output.
     */
    private final ExcelWriter excelWriter;

    /**
     * Default constructor for the class.
     * @param dataHandler The {@code DataHandler} instance with all the execution data.
     * @param outputFileName The name of the output excel file.
     * @param excelWriter The {@code ExcelWriter} to be used when writing the output.
     */
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
    public void writeOutputFiles(HashSet<Individual> outputIndividuals, String loggedData, String fitnessGraphData)  {
        excelWriter.excelWrite(outputIndividuals, dataHandler, outputDirectory, outputFilename);
        writeLogData(loggedData);
        writeFitnessGraphData(fitnessGraphData);
        //writeInputLogData();
    }

    /**
     * Writes the logs of the input loading.
     */
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
     * Writes to a cvs file the evolution of the mean fitness during the execution.
     * @param string The data in csv format.
     */
    public void writeFitnessGraphData(String string) {
        String path = outputDirectory + "fitnessGraph.csv";
        try (BufferedWriter bfWriterUncolored = new BufferedWriter(new FileWriter(path))) {
            bfWriterUncolored.write(string);
        } catch (IOException e) {
            throw new IllegalStateException("Could not write the genetic algorithm log at path: ["
                    + path + "]" );
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
    public String createOutputDirectory(String outputBaseDirectory) {



        StringBuilder directoryBuilder = new StringBuilder();
        directoryBuilder.append(outputBaseDirectory);
        directoryBuilder.append(Utils.createDirectoryStringBasedOnHour());

        File theDir = new File(directoryBuilder.toString());

        if (!theDir.exists()){
            if (!theDir.mkdirs()) {
                throw new RuntimeException("No se ha podido crear el directorio de salida. Path: " + theDir);
            }
        }

        return directoryBuilder + "/";
    }



}
