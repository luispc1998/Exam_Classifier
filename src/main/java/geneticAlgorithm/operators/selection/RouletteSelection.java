package geneticAlgorithm.operators.selection;

import fitnessFunctions.FitnessFunction;
import geneticAlgorithm.Individual;
import geneticAlgorithm.utils.Utils;
import random.RandomGenerator;

import java.util.List;

/**
 * This is an implementation of the Roulette Selection algorithm.
 */
public class RouletteSelection implements SelectionOperator {

    /**
     * Maximum number of times this selector should be called.
     */
    private final int maxPairs;


    /**
     * Constructor for the class
     * @param maxPairs Maximum number of times this selector should be called.
     */
    public RouletteSelection(int maxPairs) {
        this.maxPairs = maxPairs;
    }

    /**
     * Implementation of the Roulette Selection algorithm.
     *
     * <p>
     * This algorithm prioritizes the individuals with most fitness value. In our case the values to
     * be used are f(x) = 1 / fitnessValueOfTheIndividual.
     * @param population The population of individuals.
     * @param fitnessFunction The fitness function of the algorithm.
     * @return An individual of the population.
     */
    @Override
    public Individual selection(List<Individual> population, FitnessFunction fitnessFunction) {

        // Default result is last individual
        // (just to avoid problems with rounding errors)
        Individual selected = population.get(population.size() - 1);

        // Determine all of the fitness values
        double[] fValues = new double[population.size()];
        for (int i = 0; i < population.size(); i++) {
            fValues[i] = population.get(i).getFitnessScore(fitnessFunction);


            //Ajuste para selección por ruleta.
            if (fValues[i] == 0){
                fValues[i] = 1000;
            }
            else{
                fValues[i] = 1 / fValues[i];
            }

            //fValues[i] = Math.pow(fValues[i], 1.5);

        }
        // Normalize the fitness values
        fValues = Utils.normalizeDoubleArray(fValues);

        double prob = RandomGenerator.getGenerator().nextDouble();
        double accumulator = 0.0;
        for (int i = 0; i < fValues.length; i++) {
            // Are at last element so assign by default
            // in case there are rounding issues with the normalized values
            accumulator += fValues[i];
            if (prob <= accumulator) {
                selected = population.get(i);
                break;
            }
        }

        //selected.incDescendants(); // Not sure if storing the descendants would be useful.
        return selected;
    }

    @Override
    public void reset(){

    }

    @Override
    public int maxPairs() {
        return maxPairs;
    }


}
