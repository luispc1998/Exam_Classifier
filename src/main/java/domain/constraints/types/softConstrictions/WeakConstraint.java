package domain.constraints.types.softConstrictions;

import domain.constraints.Constraint;
import domain.constraints.counter.ConstrictionCounter;

/**
 * This type of constriction are evaluated at the end of the individual decoding, and they are the ones that will determine
 * the fitness of a given {@link geneticAlgorithm.Individual} by means of the {@link geneticAlgorithm.fitnessFunctions.FitnessFunction}.
 *
 * @see geneticAlgorithm.Individual
 * @see geneticAlgorithm.fitnessFunctions.FitnessFunction
 */
public interface WeakConstraint extends Constraint {

    /**
     * Evaluates the constrictions and increments its type counter if necessary.
     *
     * <p>
     * This method may update states depending on the result of the evaluation for later checks on the constriction.
     * @param counter the {@link ConstrictionCounter} object that is doing the counting.
     */
    void checkConstriction(ConstrictionCounter counter);




}
