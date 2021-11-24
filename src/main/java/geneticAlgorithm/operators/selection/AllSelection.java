package geneticAlgorithm.operators.selection;

import fitnessFunctions.FitnessFunction;
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
    public int problemSize;

    /**
     * Constructor for the class
     * @param problemSize Size of the population
     */
    public AllSelection(int problemSize) {
        this.problemSize = problemSize;
        reset();
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

    private void preValidation(int size) {
        if (problemSize != size) {
            throw new IllegalStateException("AllSelection is configured for problem size: " + problemSize +
                    ", but received: " + size);
        }
        if (indexPointer >= problemSize){
            throw new IllegalStateException("AllSelection operator index pointer (" + indexPointer
                    + ") exceeded problem size (" + problemSize + ").");
        }
    }

    public void reset() {
        indexPointer = -1;
    }

    @Override
    public int maxPairs() {
        return problemSize/2;
    }


}
