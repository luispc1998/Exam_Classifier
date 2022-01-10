package geneticAlgorithm.operators.selection;

import geneticAlgorithm.Individual;
import geneticAlgorithm.fitnessFunctions.FitnessFunction;

import java.util.List;

/**
 * Operator to select which or how many individuals are going to be selected to do the crossing
 * on {@link geneticAlgorithm.GeneticCore}
 *
 * @see geneticAlgorithm.GeneticCore
 */
public interface SelectionOperator {

    /**
     * Selects from the population an individual considering the fitness fuction.
     * @param population The population of individuals.
     * @param fitnessFunction The fitness function of the algorithm.
     * @return An individual from the population
     */
    Individual selection(List<Individual> population, FitnessFunction fitnessFunction);

}
