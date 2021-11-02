package domain;

import configuration.Configurer;
import domain.constrictions.Constriction;
import domain.constrictions.types.singles.SameCourseDifferentDayConstriction;
import domain.constrictions.types.singles.UnclassifiedExamsConstriction;
import domain.entities.Exam;
import domain.parsers.ConstrictionParser;
import domain.parsers.ExamParser;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This handles all the data. That is the list of exams, the configurations, etc.
 *
 * This is responsible to provide the logic to schedule the exam, as well to check the collisions. It holds the list
 * of {@code Exam} as well as the list of {@code Constriction}.
 */
public class DataHandler {

    /**
     * Configurer instance of the program.
     *
     * @see Configurer
     */
    private final Configurer configurer;

    /**
     * List of {@code Exam} to be scheduled
     */
    private List<Exam> exams;

    /**
     * Set of {@code Exam} that were already scheduled for a date.
     */
    private Set<Integer> preScheduledExams;

    /**
     * List of {@code Constriction} to be considered.
     */
    private List<Constriction> constrictions;


    /**
     * Constructor for the class
     * @param configurer Configurer that contains all the configurations options.
     * @throws IOException In case any property loading fails.
     */
    public DataHandler(Configurer configurer) throws IOException {

        this.configurer = configurer;
        this.preScheduledExams = new HashSet<>();

        String inputDataFile = configurer.getFilePaths("inputFile");
        this.exams = ExamParser.parseExams(inputDataFile, this);
        identifyScheduledExams();


        this.constrictions = ConstrictionParser.parseConstrictions(inputDataFile, this);
        addConstriction(new UnclassifiedExamsConstriction(exams));
        addConstriction(new SameCourseDifferentDayConstriction(exams));

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
     * Returns a list of exams.
     * @return A copy of {@code exams}
     */
    public List<Exam> getExams(){
        return new ArrayList<>(exams);
    }


    public Exam getExam(String code){
        for (Exam exam: exams) {
            if (exam.getCode().equals(code)){
                return exam;
            }
        }
        return null;
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
     * Returns the list of {@code Constriction}
     * @return The list of {@code Constriction}
     */
    public List<Constriction> getConstrictions() {
        return new ArrayList<>(constrictions);
    }


    /**
     * Checks if a new schedule is possible or it collides with something already scheduled.
     * @param currentDate The new date of the task.
     * @param currentHour The initial hour of the task.
     * @param examDuration The duration of the task.
     * @param extraTime Extra time for the exam.
     * @return The instance that collides with the provided options. Null if there is no collision.
     */
    public Exam checkCollisionOf(LocalDate currentDate, LocalTime currentHour, Duration examDuration, Duration extraTime) {
        for (Exam exam: exams) {
            if (exam.isScheduled()){
                if (exam.willCollideWith(currentDate,currentHour,examDuration, extraTime)){
                    return exam;
                }
            }
        }
        return null;
    }

    /**
     * Schedules an exam, at the specified date and hour.
     * @param exam The exam to be scheduled.
     * @param currentDate The date in which the exam will take place.
     * @param currentHour The hour at which the exam will tale place.
     */
    public void schedule(Exam exam, LocalDate currentDate, LocalTime currentHour) {
        exam.scheduleFor(currentDate, currentHour);
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
     * @param index The index of the exam to be returned.
     * @return The {@code Exam} object at the given index in {@code exams}.
     */
    public Exam getExam(Integer index) {
        return exams.get(index);
    }

    /**
     * Adds a {@code Constriction}.
     * @param constriction The {@code Constriction} to be added.
     */
    public void addConstriction(Constriction constriction) {
        constrictions.add(constriction);
    }
}
