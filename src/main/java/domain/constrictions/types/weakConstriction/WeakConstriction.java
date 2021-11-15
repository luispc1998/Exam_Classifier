package domain.constrictions.types.weakConstriction;

import domain.constrictions.Constriction;
import domain.constrictions.counter.ConstrictionCounter;

public interface WeakConstriction extends Constriction {

    /**
     * Evaluates the constrictions and increments its type counter if necessary.
     * @param counter the {@link ConstrictionCounter} object that is doing the counting.
     */
    void checkConstriction(ConstrictionCounter counter);




}
