package domain.constraints.types.softConstrictions.fullySoftConstraints;

import domain.constraints.counter.ConstrictionCounter;
import domain.constraints.types.softConstrictions.WeakConstraint;
import domain.entities.Exam;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

//TODO, what to do with this?
public class UnbalancedDaysPenalization implements WeakConstraint {

    /**
     * Constriction id of this type of {@code Constriction}.
     */
    public final static String CONSTRICTION_ID = "PIP";


    /**
     * List of {@link Exam} to check the schedule.
     */
    private final List<Exam> exams;

    private long minutes = 0;

    public UnbalancedDaysPenalization(List<Exam> exams) {
        this.exams = exams;
    }

    @Override
    public String getConstrictionID() {
        return CONSTRICTION_ID;
    }

    @Override
    public void checkConstriction(ConstrictionCounter counter) {

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
