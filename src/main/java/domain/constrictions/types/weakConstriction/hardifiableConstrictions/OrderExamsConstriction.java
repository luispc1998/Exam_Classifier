package domain.constrictions.types.weakConstriction.hardifiableConstrictions;

import domain.constrictions.counter.ConstrictionCounter;
import domain.constrictions.types.hardConstriction.hardifiedConstrictions.OrderExamsHardifiedConstriction;
import domain.entities.Exam;

/**
 * This states for two exams that one of them must be after the other.
 */
public class OrderExamsConstriction extends AbstractUserConstriction {

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
    public boolean isFulfilled() {
        if (first.getDate() ==null || second.getDate() ==null) {
            setLastEvaluation(true);
            return true;
        }

        return second.getDate().isAfter(first.getDate());
    }

    /*
    @Override
    public boolean isFulfilled(ConstrictionCounter counter) {
        // Case that this is hard. The restriction is fulfilled if one of the exams is not placed.
        if (first.getDate() ==null || second.getDate() ==null) {
            setLastEvaluation(true);
            return true;
        }

        if (first.getDate().isBefore(second.getDate()) || first.getDate().isEqual(second.getDate())) {
            counter.count(this);
            setLastEvaluation(false);
            return false;
        }
        setLastEvaluation(true);
        return true;
    }
     */

    @Override
    public String getConstrictionID() {
        return CONSTRICTION_ID;
    }

    /**
     * Returns the exam that must take place first.
     * @return The exam that must take place first.
     */
    public Exam getFirst() {
        return first;
    }

    /**
     * Returns the exam that must take place after {@code first}.
     * @return The exam that must take place after {@code first}.
     */
    public Exam getSecond() {
        return second;
    }

    @Override
    public void hardify() {
        OrderExamsHardifiedConstriction oehConstriction = new OrderExamsHardifiedConstriction(this);
        first.addHardConstriction(oehConstriction);
        second.addHardConstriction(oehConstriction);
    }

    @Override
    public void countMe(ConstrictionCounter counter) {
        counter.count(this);
    }
}
