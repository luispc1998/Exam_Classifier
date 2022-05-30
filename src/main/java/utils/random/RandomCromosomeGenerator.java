package utils.random;

import geneticAlgorithm.Individual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * This groups some functionalities involving random generations.
 */
public class RandomCromosomeGenerator {

    /**
     * Generates a Chromosome given the length.
     * @param length Length of the Chromosome to be created.
     * @param generator Random object to do the shuffling
     * @return a new randomized Chromosome.
     */
    public static List<Integer> generateChromosome(int length, Random generator){


        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            list.add(i);
        }

        Collections.shuffle(list, generator);
        return list;
    }

    /**
     * Generates an individual given the length of its Chromosome.
     * @param length Length of the Chromosome to be created.
     * @param generator Random object to do the shuffling
     * @return an individual with a randomized Chromosome.
     */
    public static Individual generateIndividual(int length, Random generator){
        return new Individual(generateChromosome(length, generator));
    }

}
