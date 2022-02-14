package domain.constraints.types.softConstraints;

import domain.constraints.Constraint;
import domain.constraints.counter.ConstraintCounter;

/**
 * This type of constraint are evaluated at the end of the individual decoding, and they are the ones that will determine
 * the fitness of a given {@link geneticAlgorithm.Individual} by means of the {@link geneticAlgorithm.fitnessFunctions.FitnessFunction}.
 *
 * @see geneticAlgorithm.Individual
 * @see geneticAlgorithm.fitnessFunctions.FitnessFunction
 */
public interface SoftConstraints extends Constraint {

    /**
     * Evaluates the constrains and increments its type counter if necessary.
     *
     * <p>
     * This method may update states depending on the result of the evaluation for later checks on the constraint.
     * @param counter the {@link ConstraintCounter} object that is doing the counting.
     */
    void checkConstraint(ConstraintCounter counter);




}
