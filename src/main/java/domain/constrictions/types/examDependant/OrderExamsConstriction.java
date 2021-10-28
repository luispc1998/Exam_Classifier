package domain.constrictions.types.examDependant;

import domain.constrictions.Constriction;
import domain.constrictions.counter.ConstrictionCounter;
import domain.constrictions.types.AbstractConstriction;
import domain.entities.Exam;

/**
 * This states for two exams that one of them must be after the other.
 */
public class OrderExamsConstriction extends AbstractConstriction {

    /**
     * Constriction with the identifier for this type of {@link domain.constrictions.Constriction}.
     */
    public final static String CONSTRICTION_ID = "OE";

    /**
     * {@link Exam} that must take place before than {@code second}
     */
    private Exam first;

    /**
     * {@link Exam} that must take place after than {@code first}
     */
    private Exam second;

    /**
     * Constructor of the class.
     * @param first the {@link Exam} that must be first.
     * @param second the {@link Exam} that must be second.
     */
    public OrderExamsConstriction(Exam first, Exam second){
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean isFulfilled(ConstrictionCounter counter) {


        if (first.getDate().isBefore(second.getDate())) {
            counter.count(this);
            setLastEvaluation(false);
            return false;
        }
        setLastEvaluation(true);
        return true;
    }
}
