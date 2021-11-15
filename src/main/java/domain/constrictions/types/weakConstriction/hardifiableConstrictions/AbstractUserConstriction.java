package domain.constrictions.types.weakConstriction.hardifiableConstrictions;

import domain.constrictions.counter.ConstrictionCounter;
import domain.constrictions.types.weakConstriction.WeakConstriction;

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

    protected void setLastEvaluation(boolean value) {
        lastEvaluation = value;
    }


    @Override
    public boolean getLastEvaluation() {
        return lastEvaluation;
    }

    public abstract boolean isFulfilled();
    public abstract void countMe(ConstrictionCounter counter);
}
