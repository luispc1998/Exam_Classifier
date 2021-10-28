package fitnessFunctions;

import geneticAlgorithm.Individual;

/**
 * This is the fitness function that will grade how good a {@link Individual} is.
 */
public interface FitnessFunction {

    /**
     * Apply the correponding function to a given individual.
     * @param a the individual to which the fuction will be applied
     * @return the fitness value for such an individual.
     */
    double apply(Individual a);
}
