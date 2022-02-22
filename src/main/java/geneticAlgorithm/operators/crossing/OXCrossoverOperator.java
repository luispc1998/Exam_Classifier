package geneticAlgorithm.operators.crossing;

import geneticAlgorithm.Individual;
import utils.random.RandomGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This operators implements de OX Crossing algorithm to reproduce {@code Individual}.
 */
public class OXCrossoverOperator implements CrossingOperator {

    //For testing
    private Random generator;


    /**
     * Default Constructor for the class;
     */
    public OXCrossoverOperator(){}

    /**
     * Constructor specifiying the random object used to get the points for
     * the crossing at the crossing method.
     * @param generatorWithSeed A {@code Random} object.
     */
    public OXCrossoverOperator(Random generatorWithSeed) {
        this.generator = generatorWithSeed;
    }

    /**
     * Method with the logic to perform the OX Crossing algorithm
     * @param a First individual.
     * @param b Second individual.
     * @return A List of individuals, the result of crossing a and b one or multiple times.
     */
    @Override
    public List<Individual> crossover(Individual a, Individual b) {
        List<Individual> childs = new ArrayList<>();
        childs.add(cross(a, b));

        return childs;
    }

    /**
     * Method with the logic to perform the OX Crossing algorithm
     * @param a First individual.
     * @param b Second individual.
     * @return A new Individual, the result of crossing a and b.
     */
    private Individual cross(Individual a, Individual b) {

        // Get the cromosomes
        List<Integer> aCromosome = a.getChromosome();
        List<Integer> bCromosome = b.getChromosome();
        List<Integer> newCromosome = new ArrayList<>(aCromosome.size());
        for (int i = 0; i < aCromosome.size(); i++) {
            newCromosome.add(0);
        }
        //Generate two random positions
        Random generator = chooseGenerator();
        int chromosomeLength = aCromosome.size();
        int lowerLimit = generator.nextInt(chromosomeLength);
        int upperLimit = generator.nextInt(chromosomeLength);
        
        //Reorder limits
        if (lowerLimit > upperLimit) {
            int aux = lowerLimit;
            lowerLimit = upperLimit;
            upperLimit = aux;
        }


        ArrayList<Integer> usedValues = new ArrayList<>();
        for (int i = lowerLimit; i <= upperLimit; i++) {
            newCromosome.set(i, aCromosome.get(i));
            usedValues.add(aCromosome.get(i));
        }


        int currentPos = 0;
        int cIndex = 0;
        while (cIndex < chromosomeLength && currentPos<chromosomeLength) {
            // Check if we must ignore value
            if (usedValues.contains(bCromosome.get(cIndex))){
                cIndex++;
                continue;
            }

            //Check if the pointer in the new cromosome should jump over the bounded limits.
            if (currentPos == lowerLimit)
                currentPos = upperLimit+1;

            if (currentPos == chromosomeLength)
                break;

            //Place number in actual pointer
            newCromosome.set(currentPos, bCromosome.get(cIndex));

            //Move pointers
            cIndex++;
            currentPos++;

        }

        return new Individual(newCromosome);
    }

    /**
     * Provides a generator.
     *
     * <p>
     * This mehtod is useful in case of using a seed.
     * @return A random object.
     */
    public Random chooseGenerator() {
        if (this.generator == null){
            return RandomGenerator.getGenerator();
        }
        return this.generator;

    }


}
