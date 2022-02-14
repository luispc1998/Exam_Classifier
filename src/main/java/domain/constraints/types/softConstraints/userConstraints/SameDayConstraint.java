package domain.constraints.types.softConstraints.userConstraints;


import domain.constraints.Constraint;
import domain.constraints.counter.ConstraintCounter;
import domain.constraints.types.hardConstraints.HardConstraint;
import domain.constraints.types.hardConstraints.hardUserConstraints.HardifiedConstraint;
import domain.entities.Exam;

/**
 * This will represent for a list of exams, that they must take place on the same day.
 */
public class SameDayConstraint extends AbstractUserConstraint {

    /**
     * Constraint with the identifier for this type of {@link Constraint}.
     */
    public final static String CONSTRICTION_ID= "SD";

     /**
     * {@link Exam} that must take place on the same date as {@code second}
     */
    private final Exam first;

    /**
     * {@link Exam} that must take place on the same date as {@code first}
     */
    private final Exam second;

    /**
     * Default constructor for the class.
     * @param first one of the exams
     * @param second the other exam
     */
    public SameDayConstraint(Exam first, Exam second){
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean isFulfilled() {
        // Case that this is hard. The restriction is fulfilled if one of the exams is not placed.
        if (first.getDate() ==null || second.getDate() ==null) {
            setLastEvaluation(true);
            return true;
        }

        return first.getDate().equals(second.getDate());
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
        HardConstraint hConstraint = new HardifiedConstraint(this);
        first.addHardConstraint(hConstraint);
        second.addHardConstraint(hConstraint);
    }

    @Override
    public void countMe(ConstraintCounter counter) {
        counter.count(this);
    }

}
