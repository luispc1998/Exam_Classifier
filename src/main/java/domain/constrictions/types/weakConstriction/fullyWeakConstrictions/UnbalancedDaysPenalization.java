package domain.constrictions.types.weakConstriction.fullyWeakConstrictions;

import configuration.Configurer;
import domain.constrictions.counter.ConstrictionCounter;
import domain.constrictions.types.weakConstriction.WeakConstriction;
import domain.entities.Exam;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class UnbalancedDaysPenalization implements WeakConstriction {

    public final static String CONSTRICTION_ID = "UDP";

    /**
     * List of {@link Exam} to check the schedule.
     */
    private List<Exam> exams;

    public UnbalancedDaysPenalization(List<Exam> exams) {
        this.exams = exams;
    }

    @Override
    public String getConstrictionID() {
        return CONSTRICTION_ID;
    }

    @Override
    public void checkConstriction(ConstrictionCounter counter) {
        long minutes = 0;
        HashMap<LocalDate, Exam> schedule = new HashMap<>();
        for (Exam exam: exams) {
            minutes += exam.getDuration().toMinutes();
        }
        // Tengo la suma en minutos de todos los exámenes

    }
}
