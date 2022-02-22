package domain.constraints.types.softConstraints;

import domain.constraints.counter.ConstraintCounter;

/**
 * This type of constraint are evaluated at the end of the individual decoding, and they are the ones that will determine
 * the fitness of a given {@link geneticAlgorithm.Individual} by means of the {@link geneticAlgorithm.fitnessFunctions.FitnessFunction}.
 *
 * @see geneticAlgorithm.Individual
 * @see geneticAlgorithm.fitnessFunctions.FitnessFunction
 */
public interface SoftConstraint {

    /**
     * Evaluates the constrains and increments its type counter if necessary.
     *
     * <p>
     * This method may update states depending on the result of the evaluation for later checks on the constraint.
     * @param counter the {@link ConstraintCounter} object that is doing the counting.
     */
    void checkConstraint(ConstraintCounter counter);

    /**
     * Returns the string id for the type of {@code Constraint}.
     * @return The String id for the type of {@code Constraint}.
     */
    String getConstraintID();


}
