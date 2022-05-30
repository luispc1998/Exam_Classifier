package geneticAlgorithm.operators.mutation;

import geneticAlgorithm.Individual;
import utils.random.RandomGenerator;

import java.util.List;
import java.util.Random;

/**
 * This Mutation operator performs a swap in the Chromosome of the Individual.
 */
public class MutationSwap implements MutationOperator {

    /**
     * Swaps two of the values in the Individual chromosome.
     * @param individual The individual to which the mutation must be performed.
     * @return A new Individual with the mutated chromosome.
     */
    @Override
    public Individual mutation(Individual individual) {

        if (individual.getChromosome().size() < 2){
            throw new IllegalArgumentException("Cannot use mutation swap for individuals with less that 2 as chromosome size");
        }
        Random generator = RandomGenerator.getGenerator();
        List<Integer> cromosome = individual.getChromosome();

        int cromosomeLength = cromosome.size();

        int pos0 = generator.nextInt(cromosomeLength);
        int pos1 = generator.nextInt(cromosomeLength);
        while (pos1 == pos0){
            pos1 = generator.nextInt(cromosomeLength);
        }

        Integer aux = cromosome.get(pos0);

        cromosome.set(pos0, cromosome.get(pos1));
        cromosome.set(pos1, aux);

        return new Individual(cromosome);
    }
}
