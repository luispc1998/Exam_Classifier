package logger.dataGetter;

import domain.DataHandler;
import domain.constraints.counter.ConstraintCounter;
import domain.constraints.counter.DefaultConstraintCounter;
import domain.constraints.types.softConstraints.userConstraints.UserConstraint;
import geneticAlgorithm.Individual;
import greedyAlgorithm.ChromosomeDecoder;
import main.PrettyTimetable;
import utils.Utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

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
    private int weakConstraints;
    private int constraints;

    /**
     * Default constructor for the class
     * @param statisticsFileName Filename given to the statistics file.
     * @param subDirectory Subdirectory created for the algorithm files.
     */
    public StatisticalDataGetter(String statisticsFileName, String subDirectory) {
        this.statisticsFileName = statisticsFileName;
        this.subDirectory = subDirectory;

        weakConstraints = 0;
        constraints = 0;
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

        decoder.decode(finalOne, dataHandler);
        PrettyTimetable prettyTimetable = new PrettyTimetable();
        prettyTimetable.orderScheduling(dataHandler);
        ConstraintCounter constraintCounter = new DefaultConstraintCounter();
        dataHandler.verifyConstraints(constraintCounter);


        int unplacedExams = constraintCounter.getCountOfUnclassifiedExamsConstraint();

        int unfulfilledConstraintCounter = 0;
        unfulfilledConstraintCounter += constraintCounter.getCountDayIntervalConstraint();
        unfulfilledConstraintCounter += constraintCounter.getCountOfTimeDisplacementConstraint();
        unfulfilledConstraintCounter += constraintCounter.getCountOfDifferentDayConstraint();
        unfulfilledConstraintCounter += constraintCounter.getCountOrderExamsConstraint();
        unfulfilledConstraintCounter += constraintCounter.getCountOfDaysBannedConstraint();
        unfulfilledConstraintCounter += constraintCounter.getCountOfSameDayConstraint();

        long minutesOnProhibitedInterval = constraintCounter.getCountProhibitedIntervalPenalization();

        String sb = unplacedExams +
                "," +
                unfulfilledConstraintCounter +
                "," +
                String.format(Locale.UK, "%.2f",unfulfilledConstraintCounter / (double) weakConstraints) +
                "," +
                String.format(Locale.UK, "%.2f",unfulfilledConstraintCounter / (double) constraints) +
                "," +
                minutesOnProhibitedInterval +
                "\n";
        writeStatisticsToFile(dataHandler.getConfigurer().getFilePaths("statisticsBaseDirectory") + "/" + subDirectory,
                sb);
    }

    /**
     * Writes the output to a file.
     * @param statisticsBaseDirectory The directory in which the file will be.
     * @param dataToWrite The String to write into the file.
     */
    private void writeStatisticsToFile(String statisticsBaseDirectory, String dataToWrite) {
        String path = statisticsBaseDirectory + "/" + statisticsFileName + ".csv";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, true))) {
            bw.write(dataToWrite);
        } catch (IOException e) {
            System.err.println("No ha sido posible escribir el archivo de estaísticas en el path: " + path);
        }

    }

    /**
     * Increments the constraints counters {@code constraints} and {@code weakConstraints}
     * @param constraint The constraint that will increment the counters.
     */
    public void countConstraint(UserConstraint constraint) {
        if (! constraint.wasHardified()) {
            this.weakConstraints++;
        }
        this.constraints++;
    }

    /**
     * Sets {@code constraints} and {@code weakConstraints} to 0.
     */
    public void resetConstraintCounter() {
        this.weakConstraints = 0;
        this.constraints = 0;
    }
}
