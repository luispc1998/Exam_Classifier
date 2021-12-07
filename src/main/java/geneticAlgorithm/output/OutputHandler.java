package geneticAlgorithm.output;

import domain.DataHandler;
import geneticAlgorithm.Individual;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;

public class OutputHandler {

    private String outputDirectory;
    private HashSet<Individual> outputIndividuals;
    private String outputFilename;
    private String loggedData;
    private DataHandler dataHandler;


    public OutputHandler(HashSet<Individual> outputIndividuals, DataHandler dataHandler, String outputFileName, String logging) {
        this.outputDirectory = createOutputDirectory(dataHandler.getConfigurer().getFilePaths("outputBaseDirectory"));
        this.dataHandler = dataHandler;
        this.outputIndividuals = outputIndividuals;
        this.outputFilename = outputFileName;
        this.loggedData = logging;
    }

    public void writeOutputFiles() throws IOException {
        ExcelWriter.excelWrite(outputIndividuals, dataHandler, outputDirectory, outputFilename);
        writeLogData();
    }

    private void writeLogData() throws IOException {
        BufferedWriter bfWriter = new BufferedWriter(new FileWriter(outputDirectory + "log.txt"));
        bfWriter.write(loggedData);
        bfWriter.close();
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
            theDir.mkdirs();
        }

        return directoryBuilder + "/";
    }
}