package domain.constraints.types.softConstraints.fullySoftConstraints;

import domain.constraints.counter.ConstraintCounter;
import domain.constraints.types.softConstraints.SoftConstraint;
import domain.entities.Exam;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

/**
 * The aim of this class is to try that the calendar days have the same amount of exams, so that there are no empty
 * days, while others are full.
 *
 * The idea was adding the duration of all the exams, computing the average duration. Then, an addition of the duration
 * of each day will be computed and the distance between that value and the previously computed mean would be computed.
 *
 * At the moment, this is class is not used since it was tested and it did not work well.
 */
public class UnbalancedDaysPenalization implements SoftConstraint {

    /**
     * Constraint id of this type of {@code Constraint}.
     */
    public final static String CONSTRICTION_ID = "RIP";


    /**
     * List of {@link Exam} to check the schedule.
     */
    private final List<Exam> exams;

    private long minutes = 0;

    /**
     * Constructor for the class
     * @param exams The list of exams to check
     */
    public UnbalancedDaysPenalization(List<Exam> exams) {
        this.exams = exams;
    }

    @Override
    public String getConstraintID() {
        return CONSTRICTION_ID;
    }

    @Override
    public void checkConstraint(ConstraintCounter counter) {

        HashMap<LocalDate, Long> schedule = new HashMap<>();
        for (Exam exam: exams) {
            if (! schedule.containsKey(exam.getDate())){
                schedule.put(exam.getDate(), exam.getChunkOfTime().toMinutes());
            }
            else{
                schedule.put(exam.getDate(), schedule.get(exam.getDate()) + exam.getChunkOfTime().toMinutes());
            }
            minutes += exam.getChunkOfTime().toMinutes();
        }


        counter.count(this);

        // Tengo la suma en minutos de todos los exámenes

    }

    public long getMinutes(){
        return minutes;
    }
}
