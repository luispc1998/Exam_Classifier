package domain.constrictions.types.weakConstriction.fullyWeakConstrictions;

import domain.constrictions.Constriction;
import domain.constrictions.counter.ConstrictionCounter;
import domain.constrictions.types.weakConstriction.WeakConstriction;
import domain.entities.Exam;

import java.util.List;

/**
 * This {@link Constriction} penalizes schedules by the number of unclassified {@link Exam}.
 */
public class UnclassifiedExamsConstriction implements WeakConstriction {

    /**
     * Constriction with the identifier for this type of {@link domain.constrictions.Constriction}.
     */
    public final static String CONSTRICTION_ID = "UE";

    /**
     * List of {@link Exam} to check the schedule.
     */
    private final List<Exam> exams;


    /**
     * Constructor for the class
     * @param exams List of {@link Exam} to check the schedule.
     */
    public UnclassifiedExamsConstriction(List<Exam> exams) {
        this.exams = exams;
    }

    @Override
    public void checkConstriction(ConstrictionCounter counter) {
        for (Exam exam: exams) {
            if (! exam.isScheduled()){
                counter.count(this);
            }
        }

    }

    @Override
    public String getConstrictionID() {
        return CONSTRICTION_ID;
    }
}
