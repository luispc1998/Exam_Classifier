package domain.constrictions.types.weakConstriction;

import domain.constrictions.Constriction;
import domain.constrictions.counter.ConstrictionCounter;

/**
 * This type of constriction are evaluated at the end of the individual decoding, and they are the ones that will determine
 * the fitness of a given {@link geneticAlgorithm.Individual} by means of the {@link fitnessFunctions.FitnessFunction}.
 *
 * @see geneticAlgorithm.Individual
 * @see fitnessFunctions.FitnessFunction
 */
public interface WeakConstriction extends Constriction {

    /**
     * Evaluates the constrictions and increments its type counter if necessary.
     * @param counter the {@link ConstrictionCounter} object that is doing the counting.
     */
    void checkConstriction(ConstrictionCounter counter);




}
