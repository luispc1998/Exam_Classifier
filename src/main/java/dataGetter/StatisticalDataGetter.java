package dataGetter;

import domain.DataHandler;
import domain.constrictions.counter.ConstrictionCounter;
import domain.constrictions.counter.DefaultConstrictionCounter;
import geneticAlgorithm.Individual;
import geneticAlgorithm.fitnessFunctions.greedyAlgorithm.ChromosomeDecoder;
import main.PrettyTimetable;
import utils.Utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This gets the values needed to compute how good a solution is and writes them into a file.
 */
public class StatisticalDataGetter {

    /**
     * Filename given to the statistics file.
     */
    private String statisticsFileName;

    /**
     * Subdirectory created for the algorithm files.
     */
    private String subDirectory;

    /**
     * Default constructor for the class
     * @param statisticsFileName Filename given to the statistics file.
     * @param subDirectory Subdirectory created for the algorithm files.
     */
    public StatisticalDataGetter(String statisticsFileName, String subDirectory) {
        this.statisticsFileName = statisticsFileName;
        this.subDirectory = subDirectory;
    }


    /**
     * Writes the statistics log file, with the results of the execution.
     * @param finalOne Best individual of the execution
     * @param decoder Decoding method.
     * @param dataHandler Data handler instance with the data of the execution.
     */
    public void writeLogFor(Individual finalOne, ChromosomeDecoder decoder, DataHandler dataHandler) {

        Utils.createDirectory(dataHandler.getConfigurer().getFilePaths("statisticsBaseDirectory") + "/" + subDirectory);

        dataHandler.resetScheduling();

        decoder.decodeNew(finalOne, dataHandler);
        PrettyTimetable prettyTimetable = new PrettyTimetable();
        prettyTimetable.orderScheduling(dataHandler);
        ConstrictionCounter constrictionCounter = new DefaultConstrictionCounter();
        dataHandler.verifyConstrictions(constrictionCounter);


        int unplacedExams = constrictionCounter.getCountOfUnclassifiedExamsConstriction();

        int unfulfilledConstrictionCounter = 0;
        unfulfilledConstrictionCounter += constrictionCounter.getDayIntervalConstrictionCounter();
        unfulfilledConstrictionCounter += constrictionCounter.getCountOfTimeDisplacementConstriction();
        unfulfilledConstrictionCounter += constrictionCounter.getCountOfDifferentDayConstriction();
        unfulfilledConstrictionCounter += constrictionCounter.getCountOrderExamsConstriction();
        unfulfilledConstrictionCounter += constrictionCounter.getCountOfDaysBannedConstriction();
        unfulfilledConstrictionCounter += constrictionCounter.getCountOfSameDayConstriction();

        long minutesOnProhibitedInterval = constrictionCounter.getCountProhibitedIntervalPenalization();

        String sb = unplacedExams +
                "," +
                unfulfilledConstrictionCounter +
                "," +
                minutesOnProhibitedInterval +
                "\n";
        writeToFile(dataHandler.getConfigurer().getFilePaths("statisticsBaseDirectory") + "/" + subDirectory,
                sb);
    }

    /**
     * Writes the output to a file.
     * @param statisticsBaseDirectory The directory in which the file will be.
     * @param dataToWrite The String to write into the file.
     */
    private void writeToFile(String statisticsBaseDirectory, String dataToWrite) {
        String path = statisticsBaseDirectory + "/" + statisticsFileName;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, true))) {
            bw.write(dataToWrite);
        } catch (IOException e) {
            System.err.println("No ha sido posible escribir el archivo de estaísticas en el path: " + path);
        }

    }




}
