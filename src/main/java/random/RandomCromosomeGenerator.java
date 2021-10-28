package random;

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
     * Generates a cromosome given the length.
     * @param length Length of the cromosome to be created.
     * @param generator Random object to do the shuffling
     * @return a new randomized cromosome.
     */
    public static List<Integer> generateCromosome(int length, Random generator){


        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            list.add(i);
        }

        Collections.shuffle(list, generator);
        return list;
    }

    /**
     * Generates an individual given the length of its cromosome.
     * @param length Length of the cromosome to be created.
     * @param generator Random object to do the shuffling
     * @return an individual with a randomized cromosome.
     */
    public static Individual generateIndividual(int length, Random generator){
        return new Individual(generateCromosome(length, generator));
    }

}
