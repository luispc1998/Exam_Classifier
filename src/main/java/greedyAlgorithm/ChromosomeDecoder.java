package greedyAlgorithm;

import domain.ExamsSchedule;
import domain.constraints.types.hardConstraints.HardConstraint;
import domain.entities.Exam;
import domain.entities.Interval;
import geneticAlgorithm.Individual;
import domain.configuration.Configurer;
import domain.configuration.DateTimeConfigurer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This decodes an {@code Individual} by using a greedy deterministic algorithm over the collection of exams
 * stored at {@code DataHandler}.
 *
 * <p>
 * The decoding process takes into account the {@link HardConstraint} that
 * each {@code Exam} has.
 *
 * @see geneticAlgorithm.fitnessFunctions.FitnessFunction
 */
public class ChromosomeDecoder {

    /**
     * Limit depth of the repairing algorithm.
     */
    private int limitDepth;


    public ChromosomeDecoder(Configurer configurer) {
        limitDepth = configurer.getGeneticParameters().getRepairingAlgorithmMaxDepth();
    }

    /**
     * Gets the exam list ordered as stated in the chromosome.
     * @param chromosome The chromosome of the {@code Individual} that we are decoding.
     * @param examsSchedule The {@code DataHandler} instance.
     * @return The list of {@code Exam} to schedule ordered as stated in {@code chromosome}.
     */
    private List<Exam> getExamsOrderedForChromosome(List<Integer> chromosome, ExamsSchedule examsSchedule){
        List<Exam> exams = new ArrayList<>();

        for (Integer examId: chromosome) {
            exams.add(examsSchedule.getExamById(examId));
        }

        return exams;
    }

    /**
     * Initializes a map with all the calendar days as keys and the initial day hour specified at {@link DateTimeConfigurer}.
     * @param dateTimeConfigurer The configurer instance.
     * @return A {@code HashMap} instance with all the calendar days as keys and the initial day hour specified at {@link DateTimeConfigurer}.
     * @see DateTimeConfigurer
     */
    private HashMap<LocalDate, LocalTime> initializeDays(DateTimeConfigurer dateTimeConfigurer) {
        HashMap<LocalDate, LocalTime> daysTimes = new HashMap<>();
        for (Map.Entry<LocalDate, Interval> dayTime :dateTimeConfigurer.getExamDatesWithTimes().entrySet()) {
            daysTimes.put(dayTime.getKey(), dayTime.getValue().getStart());
        }
        return daysTimes;
    }

    /**
     * Decodes the provided individual.
     * @param individual The {@code Individual} to be decoded.
     * @param examsSchedule The {@code DataHandler} instance over which the individual will be decoded.
     */
    public void decode(Individual individual, ExamsSchedule examsSchedule){

        DateTimeConfigurer dateTimeConfigurer = examsSchedule.getConfigurer().getDateTimeConfigurer();


        HashMap<LocalDate, LocalTime> daysTimes = initializeDays(dateTimeConfigurer);


        List<Integer> chromosome = individual.getChromosome();
        List<Exam> exams = getExamsOrderedForChromosome(chromosome, examsSchedule);



        for(Exam exam : exams) {
            classifyExam(examsSchedule, dateTimeConfigurer, daysTimes, exam, 0);
        }

    }

    /**
     * Tries to classify an exam.
     * @param examsSchedule The {@code DataHandler} instance where the scheduling is.
     * @param dateTimeConfigurer The {@code DateTimeConfigurer} instance where the hour configurations, including the resting
     *                           interval bounds are.
     * @param daysTimes A {@code HashMap} where the keys are the calendar days and the value the first hour of each day in which an
     *                  exam can start.
     * @param exam The {@code Exam} to be scheduled.
     * @param depth The depth of the repairing tree.
     * @return True if the exam was classified. False otherwise.
     */
    private boolean classifyExam(ExamsSchedule examsSchedule, DateTimeConfigurer dateTimeConfigurer, HashMap<LocalDate,
            LocalTime> daysTimes, Exam exam, int depth) {

        Set<LocalDate> viableDays;
        List<LocalDate> viableDaysRandomized;
        LocalTime currentHour;
        viableDays = exam.getViableDays(daysTimes.keySet());
        viableDaysRandomized = new ArrayList<>(viableDays);
        Collections.shuffle(viableDaysRandomized, new Random(exam.getId()));
        boolean scheduled = false;

        for (LocalDate day :viableDaysRandomized){
            currentHour = daysTimes.get(day);

            Exam collidingExam = null;
            while (dateTimeConfigurer.isHourInRestingInterval(currentHour) ||
                    collidingConditions(examsSchedule, exam) &&
                    (collidingExam = examsSchedule.checkCollisionOf(day, currentHour, exam.getChunkOfTime())) != null) {


                if(collidingConditions(examsSchedule, exam) && collidingExam != null){
                    daysTimes.put(day, collidingExam.getFinishingHour());
                    currentHour = daysTimes.get(day);
                }

                if (dateTimeConfigurer.isHourInRestingInterval(currentHour)){
                    daysTimes.put(day, dateTimeConfigurer.getFinishingHourRestingInterval());
                }

                currentHour = daysTimes.get(day);
            }


            if (dateTimeConfigurer.isValidEndingHourFor(day, currentHour.plus(exam.getChunkOfTime()))){
                examsSchedule.schedule(exam, day, currentHour);
                if (collidingConditions(examsSchedule, exam)) {
                    daysTimes.put(day, exam.getFinishingHour());
                }
                scheduled = true;
                break;
            }
        }


        if (!scheduled && depth < limitDepth) {
            List<Exam> candidates = examsSchedule.getSwappableExamsOfOver(exam, viableDays);

            for (Exam examCandidate : candidates) {
                LocalDate actualDate = examCandidate.getDate();
                LocalTime actualHour = examCandidate.getInitialHour();

                // Lo asignamos por restricciones como exámenes el mismo día o en día.
                examsSchedule.schedule(exam, actualDate , actualHour);
                examsSchedule.unSchedule(examCandidate, actualDate);

                HashMap<LocalDate, LocalTime> daysTimesCopy = cloneMap(daysTimes);

                if (classifyExam(examsSchedule, dateTimeConfigurer, daysTimesCopy, examCandidate, depth + 1)) {
                    writeNewDaysTimes(daysTimes, daysTimesCopy);
                    scheduled = true;
                    break;
                }
                else {
                    examsSchedule.unSchedule(exam, actualDate);
                    examsSchedule.schedule(examCandidate, actualDate, actualHour);
                }

            }


        }
        return scheduled;
    }

    private boolean collidingConditions(ExamsSchedule examsSchedule, Exam exam) {
        return examsSchedule.getConfigurer().getDateTimeConfigurer().areCollisionsEnabledFor(exam);
    }

    /**
     * Overrides {@code daysTimes} data with the one in {@code daysTimesCopy}.
     * @param daysTimes The {@code HashMap} containing the dates as keys and the first hour that can be used as value.
     * @param daysTimesCopy A copy of {@code daysTimes} used by the repairing algorithm.
     */
    private void writeNewDaysTimes(HashMap<LocalDate, LocalTime> daysTimes, HashMap<LocalDate, LocalTime> daysTimesCopy) {
        for (Map.Entry<LocalDate, LocalTime> entry: daysTimesCopy.entrySet()) {
            daysTimes.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Creates a copy of the provided map.
     * @param map The {@code HashMap} instance that we want to clone.
     * @return A new {@code HashMap} instance with a copy of the values of {@code map}.
     */
    private HashMap<LocalDate, LocalTime> cloneMap(HashMap<LocalDate, LocalTime> map) {
        HashMap<LocalDate, LocalTime> result = new HashMap<>();
        for (Map.Entry<LocalDate, LocalTime> entry: map.entrySet()) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }


}
