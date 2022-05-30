package domain.constraints.types.softConstraints.userConstraints;

import domain.constraints.counter.ConstraintCounter;
import domain.constraints.types.hardConstraints.HardConstraint;
import domain.constraints.types.hardConstraints.hardUserConstraints.HardifiedUserConstraint;
import domain.entities.Exam;

/**
 * This states for two exams that they cannot take place on the same day.
 */
public class DifferentDayConstraint extends AbstractUserConstraint {

    /**
     * Constraint with the identifier for this type of constraint.
     */
    public final static String CONSTRICTION_ID = "DD";

    /**
     * {@link Exam} that cannot take place on the same date as {@code second}.
     */
    private final Exam first;

    /**
     * {@link Exam} that cannot take place on the same date as {@code first}.
     */
    private final Exam second;

    /**
     * Default constructor for the class.
     * @param first one of the exams.
     * @param second the other exam.
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
    public void countMe(ConstraintCounter counter) {
        counter.count(this);
    }


    @Override
    public String getConstraintID() {
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
        HardConstraint hConstraint = new HardifiedUserConstraint(this);
        first.addHardConstraint(hConstraint);
        second.addHardConstraint(hConstraint);
    }

}
