package domain;

import domain.constraints.counter.ConstraintCounter;
import domain.constraints.types.hardConstraints.fullyHardConstraints.IsolateCourseOnDayConstraint;
import domain.constraints.types.softConstraints.SoftConstraint;
import domain.constraints.types.softConstraints.fullySoftConstraints.NumericalComplexityPenalization;
import domain.constraints.types.softConstraints.fullySoftConstraints.RestingIntervalPenalization;
import domain.constraints.types.softConstraints.fullySoftConstraints.SameCourseDifferentDayConstraint;
import domain.constraints.types.softConstraints.fullySoftConstraints.UnscheduledExamsConstraint;
import domain.entities.Exam;
import domain.entities.Interval;
import domain.parsers.ConstraintParser;
import domain.configuration.Configurer;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This handles all the data. That is the list of exams, the configurations, etc.
 *
 * <p>
 * This is responsible to provide the logic to schedule the exam, as well to check the collisions. It holds the list
 * of {@code Exam} as well as the list of {@code SoftConstraints}.
 */
public class ExamsSchedule {

    /**
     * Configurer instance of the program.
     *
     * @see Configurer
     */
    private final Configurer configurer;

    /**
     * List of {@code Exam} to be scheduled
     */
    private final List<Exam> exams;

    /**
     * Set of {@code Exam} that were already scheduled for a date.
     */
    private final Set<Integer> preScheduledExams;

    /**
     * List of {@code SoftConstraints} to be considered.
     */
    private final List<SoftConstraint> constraints;


    /**
     * Constructor for the class
     * @param configurer Configurer that contains all the configurations options.
     */
    public ExamsSchedule(Configurer configurer, List<Exam> exams, ConstraintParser constraintParser) {

        this.configurer = configurer;
        this.preScheduledExams = new HashSet<>();
        this.constraints = new ArrayList<>();

        String inputDataFile = configurer.getFilePaths("inputFile");
        this.exams = new ArrayList<>(exams);
        identifyScheduledExams();

        addConstraints();
        this.constraints.addAll(constraintParser.parseConstraints(inputDataFile, this));

    }

    /**
     * Adds all the default constraints.
     */
    private void addConstraints() {

        //Hard
        for (Exam exam: exams) {
            exam.addHardConstraint(new IsolateCourseOnDayConstraint(exam));
        }

        //Weak
        addConstraint(new UnscheduledExamsConstraint(exams));
        addConstraint(new SameCourseDifferentDayConstraint(exams));
        addConstraint(new RestingIntervalPenalization(exams, configurer));
        addConstraint(new NumericalComplexityPenalization(exams));
    }

    /**
     * Checks {@code exams} and add the id to {@code preScheduledExams}
     */
    private void identifyScheduledExams() {
        for (Exam exam: exams) {
            if (exam.isScheduled()){
                preScheduledExams.add(exam.getId());
            }
        }
    }

    /**
     * Resets the scheduling of all {@code Exam} at {@code exams} whose id
     * is not in {@code preScheduledExams}
     */
    public void resetScheduling(){
        for (Exam exam: exams) {
            if (! wasScheduled(exam)){
               exam.resetScheduling();
            }
        }

        IsolateCourseOnDayConstraint.resetAvailabilities(configurer.getDateTimeConfigurer().getExamDates(),
                getPreScheduledExams());
    }

    /**
     * Returns the exams that were initially scheduled.
     * @return A list of the {@code Exam} instances that were initially scheduled.
     */
    public List<Exam> getPreScheduledExams() {
        List<Exam> result = new ArrayList<>();
        for (Exam exam: exams) {
            if (preScheduledExams.contains(exam.getId())){
                result.add(exam);
            }
        }
        return result;
    }

    /**
     * Checks wether an exam was scheduled from the beginning or not.
     * @param exam The {@code Exam} to be checked.
     * @return true in case it was scheduled from the beginning, false otherwise.
     */
    private boolean wasScheduled(Exam exam) {
        return preScheduledExams.contains(exam.getId());
    }

    /**
     * Returns the list of exams that are being handled by the algorithm, being those the one that were not scheduled
     * from the very beginning.
     * @return A list of {@code exams} which were not initially scheduled.
     */
    public List<Exam> getPreUnscheduledExams(){
        return exams.stream().filter( (ex) -> !preScheduledExams.contains(ex.getId())).collect(Collectors.toList());
    }


    /**
     * Provides a cloned instance of the schedule.
     * @return A copy of {@code Exams} with all the instances within it also cloned.
     */
    public List<Exam> getClonedSchedule() {
        List<Exam> clonedExams = new ArrayList<>();
        for (Exam exam: exams) {
            clonedExams.add(exam.clone());
        }
        return clonedExams;
    }

    /**
     * Returns the list of {@code WeakConstraint}.
     * @return The list of {@code WeakConstraint}.
     */
    public List<SoftConstraint> getConstraints() {
        return new ArrayList<>(constraints);
    }


    /**
     * Checks if a new schedule is possible or it collides with something already scheduled.
     * @param currentDate The new date of the task.
     * @param currentHour The initial hour of the task.
     * @param chunkOfTime Time needed for the exam.
     * @return The instance that collides with the provided options. Null if there is no collision.
     */
    public Exam checkCollisionOf(LocalDate currentDate, LocalTime currentHour, Duration chunkOfTime) {
        for (Exam exam: exams) {
            if (exam.isScheduled()){
                if (configurer.getDateTimeConfigurer().areCollisionsEnabledFor(exam)) {
                    if (exam.willCollideWith(currentDate, currentHour, chunkOfTime)) {
                        return exam;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Schedules an exam, at the specified date and hour.
     * @param exam The exam to be scheduled.
     * @param currentDate The date in which the exam will take place.
     * @param currentHour The hour at which the exam will take place.
     */
    public void schedule(Exam exam, LocalDate currentDate, LocalTime currentHour) {
        exam.scheduleFor(currentDate, currentHour);
        IsolateCourseOnDayConstraint.addCourseToDate(currentDate, exam);
    }

    /**
     * Schedules an exam, at the specified date and hour.
     * @param exam The exam to be scheduled.
     * @param currentDate The date in which the exam was taken place.
     */
    public void unSchedule(Exam exam, LocalDate currentDate) {
        exam.scheduleFor(null, null);
        IsolateCourseOnDayConstraint.removeCourseFromDate(currentDate, exam);
    }

    /**
     * Returns the configurer.
     * @return the configurer.
     */
    public Configurer getConfigurer() {
        return configurer;
    }

    /**
     * Returns an exam by index.
     * @param examId The index of the exam to be returned.
     * @return The {@code Exam} object at the given index in {@code exams}.
     */
    public Exam getExamById(Integer examId) {
        return exams.stream().filter((ex) -> ex.getId() == examId).findFirst().get();
    }

    /**
     * Adds a {@code WeakConstraint}.
     * @param constraint The {@code WeakConstraint} to be added.
     */
    public void addConstraint(SoftConstraint constraint) {
        constraints.add(constraint);
    }

    /**
     * Recomputes the result of all the constraints over the current schedule.
     * @param counter The constraint counter instance that will be used for the checking of the weak constraints.
     * @return A {@code HashMap} being the keys the constraints ids, and the values a list of that type of constraint.
     */
    public HashMap<String, List<SoftConstraint>> verifyConstraints(ConstraintCounter counter) {
        HashMap<String, List<SoftConstraint>> verifiedConstraints = new HashMap<>();

        for (SoftConstraint cons: constraints) {
            cons.checkConstraint(counter);
            if (! verifiedConstraints.containsKey(cons.getConstraintID())) {
                verifiedConstraints.put(cons.getConstraintID(), new ArrayList<>());
            }
            verifiedConstraints.get(cons.getConstraintID()).add(cons);
        }

        return verifiedConstraints;
    }

    /**
     * Returns the exam on the provided date which take place in the provided interval.
     * @param day The day in which the returned exams will take place.
     * @param interval The time interval in which the returned exams start.
     * @return A {@code List} of {@code Exam} that take place on {@code day} in the time interval {@code interval}.
     */
    public List<Exam> getExamsAt(LocalDate day, Interval interval) {
        List<Exam> resultExams = new ArrayList<>();
        for (Exam exam: exams) {
            if (exam.takesPlaceOn(day, interval)) {
                resultExams.add(exam);
            }
        }
        return resultExams;
    }

    /**
     * Returns a list of exams, taken place on {@code days} that are viable for a swap with {@code notScheduledExam}.
     * @param notScheduledExam The exam that is wanted to be placed.
     * @param days The days in which {@code notScheduledExam} can take place and therefore the days where we need to look
     *             for interchangeable exams.
     * @return A {@code List} of {@code Exam} that are viable to be interchanged with {@code notScheduledExam}.
     */
    public List<Exam> getSwappableExamsOfOver(Exam notScheduledExam, Set<LocalDate> days) {
        List<Exam> candidates = new ArrayList<>();
        for (Exam exam: getPreUnscheduledExams()) {
            if (days.contains(exam.getDate()) &&
                exam.getChunkOfTime().toMinutes() >= notScheduledExam.getChunkOfTime().toMinutes()) {
                candidates.add(exam);
            }
        }
        return candidates;
    }

    /**
     * Returns the index of an exam in the list.
     * @param exam The exam whose index will be checked.
     * @return The index of the provided exam in {@code exams}.
     */
    public int getIndexOfExam(Exam exam) {
        return exams.indexOf(exam);
    }

}
