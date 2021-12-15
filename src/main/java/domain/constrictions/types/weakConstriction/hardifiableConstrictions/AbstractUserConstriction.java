package domain.constrictions.types.weakConstriction.hardifiableConstrictions;

import domain.constrictions.counter.ConstrictionCounter;
import domain.constrictions.types.weakConstriction.WeakConstriction;

/**
 * Default implementation of {@code checkConstriction} method, and implementation of the logic to hold the result of the
 * last evaluation.
 *
 * <p>
 * By having a default implementation of {@code checkConstriction} the inheritors just need to implement the constriction
 * logic, and the call to {@code ConstrictionCounter}, as is requested in the abstract methods {@code isFulfilled} and
 * {@code countMe}.
 */
public abstract class AbstractUserConstriction implements UserConstriction, WeakConstriction {

    /**
     * Stores the value of the last evaluation of isFulfilled.
     * <p>
     * It is initialized to false by default.
     */
    private boolean lastEvaluation;


    @Override
    public void checkConstriction(ConstrictionCounter counter) {
        if (isFulfilled()) {
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
     * @see ConstrictionCounter
     */
    public abstract void countMe(ConstrictionCounter counter);
}
