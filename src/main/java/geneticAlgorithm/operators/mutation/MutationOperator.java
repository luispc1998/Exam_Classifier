package geneticAlgorithm.operators.mutation;

import geneticAlgorithm.Individual;

/**
 * This operator performs slight changes over the new individuals created
 * by the {@link geneticAlgorithm.operators.crossing.CrossingOperator}
 * at {@link geneticAlgorithm.GeneticCore}
 */
public interface MutationOperator {

    /**
     * Method that performs the mutation
     * @param individual The individual to which the mutation must be performed
     * @return A new individual based on the parameter, but with a mutation.
     */
    Individual mutation(Individual individual);
}
