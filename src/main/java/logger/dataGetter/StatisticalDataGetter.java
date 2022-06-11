package logger.dataGetter;

import domain.ExamsSchedule;
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
import java.util.HashSet;
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
     * Default constructor for the class.
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
     * @param examsSchedule Data handler instance with the data of the execution.
     */
    public void writeLogFor(Individual finalOne, ChromosomeDecoder decoder, ExamsSchedule examsSchedule) {
        String path = examsSchedule.getConfigurer().getFilePaths("statisticsBaseDirectory") + subDirectory;
        Utils.createDirectory(path);

        writeLog(finalOne, decoder, examsSchedule, path);
    }


    private void writeLog(Individual finalOne, ChromosomeDecoder decoder, ExamsSchedule examsSchedule, String path) {

        String[] statisticHeaders = new String[] {"Unscheduled_exams", "Unfulfilled_user_constraints",
                "Unfulfilled_user_constrains / Soft_user_constraints", "Unfulfilled_user_constrains, / User_constraints",
                "Minutes_in_resting_interval"};



        examsSchedule.resetScheduling();

        decoder.decode(finalOne, examsSchedule);
        PrettyTimetable prettyTimetable = new PrettyTimetable();
        prettyTimetable.orderScheduling(examsSchedule);
        ConstraintCounter constraintCounter = new DefaultConstraintCounter();
        examsSchedule.verifyConstraints(constraintCounter);


        int unplacedExams = constraintCounter.getCountOfUnscheduledExamsConstraint();

        int unfulfilledConstraintCounter = 0;
        unfulfilledConstraintCounter += constraintCounter.getCountDayIntervalConstraint();
        unfulfilledConstraintCounter += constraintCounter.getCountOfTimeDisplacementConstraint();
        unfulfilledConstraintCounter += constraintCounter.getCountOfDifferentDayConstraint();
        unfulfilledConstraintCounter += constraintCounter.getCountOrderExamsConstraint();
        unfulfilledConstraintCounter += constraintCounter.getCountOfDaysBannedConstraint();
        unfulfilledConstraintCounter += constraintCounter.getCountOfSameDayConstraint();

        long minutesOnProhibitedInterval = constraintCounter.getCountRestingIntervalPenalization();

        StringBuilder sb = new StringBuilder();
        sb.append(statisticHeaders[0]);
        for (int i = 1; i < statisticHeaders.length; i++) {
            sb.append(",");
            sb.append(statisticHeaders[i]);
        }
        sb.append("\n");

        sb.append(unplacedExams).append(",").append(unfulfilledConstraintCounter).append(",")
                .append(String.format(Locale.UK, "%.2f", unfulfilledConstraintCounter / (double) weakConstraints))
                .append(",").append(String.format(Locale.UK, "%.2f", unfulfilledConstraintCounter / (double) constraints))
                .append(",").append(minutesOnProhibitedInterval).append("\n");
        writeStatisticsToFile(path,sb.toString());
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
     * Increments the constraints counters {@code constraints} and {@code weakConstraints}.
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


    public void writeLogFor(HashSet<Individual> elite, ChromosomeDecoder chromosomeDecoder,
                            ExamsSchedule examsSchedule, String outputFileName) {
        String path;
        int counter = 0;
        for (Individual idv: elite) {
            path = subDirectory + outputFileName + "_" + counter++;
            Utils.createDirectory(path);
            writeLog(idv, chromosomeDecoder, examsSchedule, path);
        }
    }
}
