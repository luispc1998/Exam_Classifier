package domain.constrictions;

import domain.constrictions.counter.ConstrictionCounter;

/**
 * This represents a condition or circumstance that the exam scheduling must fulfill.
 */
public interface Constriction {


    /**
     * States whether a Constriction is fulfilled or not.
     * @param counter the {@link ConstrictionCounter} object that is doing the counting.
     * @return true in case it is fulfilled, false otherwise.
     */
    boolean isFulfilled(ConstrictionCounter counter);

    /**
     * Returns the last result of {@code isFulfilled}
     * @return the last evaluation of {@code isFulfilled}
     */
    boolean getLastEvaluation();

}
