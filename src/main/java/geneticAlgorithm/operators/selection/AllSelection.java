package geneticAlgorithm.operators.selection;

import geneticAlgorithm.fitnessFunctions.FitnessFunction;
import geneticAlgorithm.Individual;

import java.util.List;

/**
 * This selection operator basically retrieves sequentially all {@code Individual} of the population.
 */
public class AllSelection implements SelectionOperator {

    /**
     * A pointer to the index of the last retrieved {@code Individual}
     */
    public int indexPointer;

    /**
     * Size of the population.
     */
    public final int problemSize;

    /**
     * Constructor for the class
     * @param problemSize Size of the population
     */
    public AllSelection(int problemSize) {
        this.problemSize = problemSize;
        indexPointer = -1;
    }

    /**
     * Retrieves sequentially the individuals of the populations, with each call.
     * @param population The population of individuals.
     * @param fitnessFunction The fitness function of the algorithm.
     * @return The next individual to which {@code indexPointer} was pointing.
     */
    @Override
    public Individual selection(List<Individual> population, FitnessFunction fitnessFunction) {
        indexPointer++;
        preValidation(population.size());
        return population.get(indexPointer);
    }

    /**
     * Checks that there is no inconsistency in the object state.
     * @param size Size of the current population from which we are selecting.
     * @throws IllegalStateException in case that the population size does not match {@code problemSize}.
     */
    private void preValidation(int size) {
        if (problemSize != size) {
            throw new IllegalStateException("AllSelection is configured for problem size: " + problemSize +
                    ", but received: " + size);
        }

        if (indexPointer == problemSize) {
            indexPointer = 0;
        }
    }

}
