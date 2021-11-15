package domain.constrictions.types.weakConstriction.hardifiableConstrictions;

import domain.constrictions.counter.ConstrictionCounter;
import domain.constrictions.types.hardConstriction.hardifiedConstrictions.DifferentDayHardifiedConstriction;
import domain.entities.Exam;

/**
 * This states for two exams that they cannot take place on the same day.
 */
public class DifferentDayConstriction extends AbstractUserConstriction {

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
    public boolean isFulfilled() {
        if (first.getDate() ==null || second.getDate() ==null) {
            setLastEvaluation(true);
            return true;
        }

        return ! first.getDate().equals(second.getDate());
    }

    @Override
    public void countMe(ConstrictionCounter counter) {
        counter.count(this);
    }

    /*
    @Override
    public boolean isFulfilled(ConstrictionCounter counter) {
        // Case that this is hard. The restriction is fulfilled if one of the exams is not placed.
        if (first.getDate() ==null || second.getDate() ==null) {
            setLastEvaluation(true);
            return true;
        }

        if (first.getDate().equals(second.getDate())) {
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
     * Returns {@code first}.
     *
     * <p>
     * There is no order in which the exams must be.
     * @return {@code first}.
     */
    public Exam getFirst() {
        return first;
    }

    /**
     * Returns {@code second}.
     *
     * <p>
     * There is no order in which the exams must be.
     * @return {@code second}.
     */
    public Exam getSecond() {
        return second;
    }

    @Override
    public void hardify() {
        DifferentDayHardifiedConstriction ddhConstriction = new DifferentDayHardifiedConstriction(this);
        first.addHardConstriction(ddhConstriction);
        second.addHardConstriction(ddhConstriction);
    }
}
