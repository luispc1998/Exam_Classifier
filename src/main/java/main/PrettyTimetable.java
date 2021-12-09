package main;

import domain.DataHandler;
import domain.entities.Exam;
import domain.entities.Interval;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

/**
 * This takes an exam schedule and improves the starting hours according to a given criteria.
 *
 * <p>
 * The main purpose of this is to avoid having exams starting at 9:02 or 15:25, replacing those by 9:15 and 15:30
 * if possible.
 */
public class PrettyTimetable {
    //TODO, no puede tocar los exámenes que ya estén fechados de antes, ahora lo hace.

    /**
     * Valid starting minutes part for the exam's initial hour.
     */
    private final static int[] startingMinutes = new int[]{60, 15, 30, 45};

    /**
     * Improves the exam schedule in the provided {@code dataHandler}
     * @param datahandler The {@code DataHandler} object in which the exam schedule is.
     */
    public void orderScheduling(DataHandler datahandler) {
        List<LocalDate> calendar = datahandler.getConfigurer().getDateTimeConfigurer().getExamDates();
        for (LocalDate day : calendar) {
            orderDay(datahandler, day);
        }
    }

    private void orderDay(DataHandler dataHandler, LocalDate day) {

        List<Interval> validIntervals = dataHandler.getConfigurer().getDateTimeConfigurer().getValidIntervals();

        for (Interval interval: validIntervals) {
            List<Exam> examsOnDay = dataHandler.getExamsAt(day, interval);
            if (isThereFixedExam(dataHandler, examsOnDay)){
                continue;
            }
            Duration examsChunkOfTime = addDurations(examsOnDay);
            long freeMinutes = interval.getDuration().minus(examsChunkOfTime).toMinutes();

            for (int i = 0; i < examsOnDay.size(); i++) {
                long neededMovement = checkMovement(examsOnDay.get(i));
                if (freeMinutes == 0) {
                    break;
                }
                if (neededMovement > 0 && freeMinutes >= neededMovement) {
                    moveExams(examsOnDay, i, neededMovement);
                }
            }
        }
    }

    private boolean isThereFixedExam(DataHandler dataHandler, List<Exam> examsOnDay) {
        List<Exam> preScheduledExams = dataHandler.getPreScheduledExams();
        for(Exam exam: examsOnDay) {
            if (preScheduledExams.contains(exam)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Moves the exam on the list secuentially skipping until {@code i}
     * @param examsOnDay List of exams that need to be rescheduled
     * @param i Index of the first exam to reschedule
     * @param neededMovement Minutes that all the exams must be posposed.
     */
    private void moveExams(List<Exam> examsOnDay, int i, long neededMovement) {
        for (int j = i; j < examsOnDay.size(); j++) {
            Exam exam = examsOnDay.get(j);
            exam.setInitialHour(exam.getInitialHour().plus(Duration.ofMinutes(neededMovement)));
        }
    }

    /**
     * Checks how much is needed to be moved an exam to fit the criteria specified.
     * @param exam The exams to be checked
     * @return The amount of time that the exam should be postponed.
     */
    private long checkMovement(Exam exam) {
        long minimumDistance = Long.MAX_VALUE;
        for (int value: startingMinutes) {
            int distance = value - exam.getInitialHour().getMinute();

            if ( distance >= 0 && distance < minimumDistance){
                minimumDistance = distance;
            }

            if (distance == 60) {
                minimumDistance = 0;
            }
        }
        return minimumDistance;
    }

    /**
     * Adds the the durations of all the exams' chunks of time in a list.
     * @param examsOnDay List of exams.
     * @return The additions of all the exams' chunks of time.
     */
    private Duration addDurations(List<Exam> examsOnDay) {
        Duration result = Duration.ZERO;
        for (Exam exam: examsOnDay) {
            result = result.plus(exam.getChunkOfTime());
        }
        return result;
    }
}
