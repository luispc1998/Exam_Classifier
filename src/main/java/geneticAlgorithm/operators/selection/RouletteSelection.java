package geneticAlgorithm.operators.selection;

import geneticAlgorithm.Individual;
import fitnessFunctions.FitnessFunction;
import geneticAlgorithm.utils.Utils;
import random.RandomGenerator;

import java.util.List;

public class RouletteSelection implements SelectionOperator {

    private int maxPairs;


    public RouletteSelection(int i) {
        this.maxPairs = i;
    }

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

    public void reset(){

    }

    @Override
    public int maxPairs() {
        return maxPairs;
    }


}
