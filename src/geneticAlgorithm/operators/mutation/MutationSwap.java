package geneticAlgorithm.operators.mutation;

import geneticAlgorithm.Individual;
import random.RandomGenerator;

import java.util.List;
import java.util.Random;

public class MutationSwap implements MutationOperator {

    @Override
    public Individual mutation(Individual scheduling) {

        Random generator = RandomGenerator.getGenerator();
        List<Integer> cromosome = scheduling.getCromosome();

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
