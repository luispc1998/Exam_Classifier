package domain.constrictions.types.singles;

import domain.constrictions.Constriction;
import domain.constrictions.counter.ConstrictionCounter;
import domain.constrictions.types.AbstractConstriction;
import domain.entities.Exam;

import java.util.List;

/**
 * This {@link Constriction} penalizes schedules by the number of unclassified {@link Exam}.
 */
public class UnclassifiedExamsConstriction extends AbstractConstriction {

    /**
     * Constriction with the identifier for this type of {@link domain.constrictions.Constriction}.
     */
    public final static String CONSTRICTION_ID = "UE";

    /**
     * List of {@link Exam} to check the schedule.
     */
    private List<Exam> exams;


    /**
     * Constructor for the class
     * @param exams List of {@link Exam} to check the schedule.
     */
    public UnclassifiedExamsConstriction(List<Exam> exams) {
        this.exams = exams;
    }

    @Override
    public boolean isFulfilled(ConstrictionCounter counter) {
        setLastEvaluation(true);
        for (Exam exam: exams) {
            if (! exam.isScheduled()){
                counter.count(this);
                setLastEvaluation(false);
            }
        }

        return getLastEvaluation();
    }
}
