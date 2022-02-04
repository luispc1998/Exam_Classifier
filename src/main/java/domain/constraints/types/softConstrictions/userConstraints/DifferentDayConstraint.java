package domain.constraints.types.softConstrictions.userConstraints;

import domain.constraints.Constraint;
import domain.constraints.counter.ConstrictionCounter;
import domain.constraints.types.hardConstraints.HardConstraint;
import domain.constraints.types.hardConstraints.hardUserConstrictions.HardifiedConstraint;
import domain.entities.Exam;

/**
 * This states for two exams that they cannot take place on the same day.
 */
public class DifferentDayConstraint extends AbstractUserConstraint {

    /**
     * Constriction with the identifier for this type of {@link Constraint}.
     */
    public final static String CONSTRICTION_ID = "DD";

    /**
     * {@link Exam} that cannot take place on the same date as {@code second}
     */
    private final Exam first;

    /**
     * {@link Exam} that cannot take place on the same date as {@code first}
     */
    private final Exam second;

    /**
     * Default constructor for the class.
     * @param first one of the exams
     * @param second the other exam
     */
    public DifferentDayConstraint(Exam first, Exam second){
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
    public void specificHardify() {
        HardConstraint hConstriction = new HardifiedConstraint(this);
        first.addHardConstriction(hConstriction);
        second.addHardConstriction(hConstriction);
    }

}
