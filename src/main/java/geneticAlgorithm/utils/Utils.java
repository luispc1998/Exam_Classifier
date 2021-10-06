package geneticAlgorithm.utils;

import geneticAlgorithm.Individual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Utils {


    public static double[] normalizeDoubleArray(double[] numberArray){
        double divisor = 0;
        for (double number: numberArray) {
            divisor += number;
        }

        int arrayLength = numberArray.length;
        double[] normalizedArray = new double[arrayLength];

        if (divisor!=0) {

            for (int i = 0; i < arrayLength; i++) {
                normalizedArray[i] = numberArray[i] / divisor;
            }
        }
        return normalizedArray;


    }

    public static List<Individual> generatePopulationOfSizeFromIndividual(int popSize, Individual individualPrime) {
        List<Individual> population = new ArrayList<>();
        population.add(individualPrime);

        List<Integer> cromosomePrime = individualPrime.getCromosome();
        for (int i = 0; i < popSize-1; i++) {
            List<Integer> cromosomeClone = new ArrayList<>(cromosomePrime);
            Collections.shuffle(cromosomeClone);
            population.add(new Individual(cromosomeClone));
        }
        return population;
    }
}
