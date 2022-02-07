package domain.constraints.types.softConstrictions.userConstraints;

import domain.constraints.counter.ConstraintCounter;
import domain.constraints.types.softConstrictions.SoftConstraints;

/**
 * Default implementation of {@code checkConstriction} method, and implementation of the logic to hold the result of the
 * last evaluation.
 *
 * <p>
 * By having a default implementation of {@code checkConstriction} the inheritors just need to implement the constriction
 * logic, and the call to {@code ConstrictionCounter}, as is requested in the abstract methods {@code isFulfilled} and
 * {@code countMe}.
 */
public abstract class AbstractUserConstraint implements UserConstraint, SoftConstraints {

    /**
     * Stores the value of the last evaluation of isFulfilled.
     * <p>
     * It is initialized to false by default.
     */
    private boolean lastEvaluation;

    /**
     * Indicates if theh current constriction was marked as hard.
     */
    private boolean hardified;

    /**
     * States that the constriction was hardified.
     */
    protected void setHardified() {
        if (hardified){
            throw new IllegalStateException("Cannot hardify same Constriction twice");
        }
        hardified = true;
    }
    @Override
    public boolean wasHardified(){
        return hardified;
    }


    @Override
    public void checkConstriction(ConstraintCounter counter) {
        if (wasHardified() || isFulfilled()) { // condition order is important, don't swap these!
            setLastEvaluation(true);
        }
        else {
            countMe(counter);
            setLastEvaluation(false);
        }

    }

    /**
     * Sets the value of {@code lastEvaluation} to the provided one.
     * @param value New value for {@code lastEvaluation}
     */
    protected void setLastEvaluation(boolean value) {
        lastEvaluation = value;
    }


    @Override
    public boolean getLastEvaluation() {
        return lastEvaluation;
    }


    /**
     * Do the necessary logic and calls the {@code ConstrictionCounter}.
     * @param counter The counter that the constriction will call to provide the data.
     *
     * @see ConstraintCounter
     */
    public abstract void countMe(ConstraintCounter counter);


    @Override
    public void hardify() {
        setHardified();
        specificHardify();
    }

    public abstract void specificHardify();
}
