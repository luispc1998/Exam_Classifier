package geneticAlgorithm.output;

import domain.DataHandler;
import geneticAlgorithm.Individual;

import java.io.BufferedWriter;
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
    public OutputHandler(DataHandler dataHandler, String outputFileName, String outputDirectory,
                         ExcelWriter excelWriter) {
        this.outputDirectory = outputDirectory;
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





}
