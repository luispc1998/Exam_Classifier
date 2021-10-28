package domain.constrictions.types.examDependant;

import domain.constrictions.Constriction;
import domain.constrictions.counter.ConstrictionCounter;
import domain.constrictions.types.AbstractConstriction;
import domain.entities.Exam;

/**
 * This states for two exams that they cannot take place on the same day.
 */
public class DifferentDayConstriction extends AbstractConstriction {

    /**
     * Constriction with the identifier for this type of {@link domain.constrictions.Constriction}.
     */
    public final static String CONSTRICTION_ID = "DD";

    /**
     * {@link Exam} that cannot take place on the same date as {@code second}
     */
    private Exam first;

    /**
     * {@link Exam} that cannot take place on the same date as {@code first}
     */
    private Exam second;

    /**
     * Default constructor for the class.
     * @param first one of the exams
     * @param second the other exam
     */
    public DifferentDayConstriction(Exam first, Exam second){
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean isFulfilled(ConstrictionCounter counter) {

        if (first.getDate().equals(second.getDate())) {
            counter.count(this);
            setLastEvaluation(false);
            return false;
        }
        setLastEvaluation(true);
        return true;
    }
}
