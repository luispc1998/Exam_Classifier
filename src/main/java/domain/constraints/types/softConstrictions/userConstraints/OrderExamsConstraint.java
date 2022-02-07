package domain.constraints.types.softConstrictions.userConstraints;

import domain.constraints.Constraint;
import domain.constraints.counter.ConstraintCounter;
import domain.constraints.types.hardConstraints.HardConstraint;
import domain.constraints.types.hardConstraints.hardUserConstrictions.HardifiedConstraint;
import domain.entities.Exam;

/**
 * This states for two exams that one of them must be after the other.
 */
public class OrderExamsConstraint extends AbstractUserConstraint {

    /**
     * Constriction with the identifier for this type of {@link Constraint}.
     */
    public final static String CONSTRICTION_ID = "OE";

    /**
     * {@link Exam} that must take place before than {@code second}
     */
    private final Exam first;

    /**
     * {@link Exam} that must take place after than {@code first}
     */
    private final Exam second;

    /**
     * Constructor of the class.
     * @param first the {@link Exam} that must be first.
     * @param second the {@link Exam} that must be second.
     */
    public OrderExamsConstraint(Exam first, Exam second){
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
    public void specificHardify() {
        HardConstraint hConstriction = new HardifiedConstraint(this);
        first.addHardConstriction(hConstriction);
        second.addHardConstriction(hConstriction);
    }

    @Override
    public void countMe(ConstraintCounter counter) {
        counter.count(this);
    }
}
