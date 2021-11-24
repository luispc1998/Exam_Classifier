package geneticAlgorithm.operators.replacement;

import fitnessFunctions.FitnessFunction;
import geneticAlgorithm.Individual;

import java.util.List;

/**
 * This operator selects among the old individuals and the new ones which of
 * them must make it to the next generation.
 */
public interface ReplacementOperator {

    /**
     * Selects which {@code Individual} among the old and new ones will go on to the next generation.
     * @param prevGeneration Individuals of the previous generation.
     * @param child Individuals created by crossing the individuals in {@code prevGeneration}.
     * @param fitnessFunction The fitness function of the algorithm.
     * @return The new generation of Individuals.
     */
    List<Individual> doReplacement(List<Individual> prevGeneration, List<Individual> child,
                                   FitnessFunction fitnessFunction);
}
