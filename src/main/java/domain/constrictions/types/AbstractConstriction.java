package domain.constrictions.types;

import domain.constrictions.Constriction;

/**
 * This implements the logic to handle {@code lastEvaluation}.
 *
 * <p>
 * This logic will be needed afterwards to obtain which constrictions were fulfilled
 * and output them to a file.
 */
public abstract class AbstractConstriction implements Constriction {



    /**
     * Stores the value of the last evaluation of isFulfilled.
     * <p>
     * It is initialized to false by default.
     */
    private boolean lastEvaluation;


    protected void setLastEvaluation(boolean value) {
        lastEvaluation = value;
    }


    @Override
    public boolean getLastEvaluation() {
        return lastEvaluation;
    }


}
