package geneticAlgorithm.operators.crossing;

import geneticAlgorithm.Individual;
import random.RandomGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OXCrosssingOperator implements CrossingOperator {


    @Override
    public Individual cross(Individual a, Individual b) {

        // Get the cromosomes
        List<Integer> aCromosome = a.getCromosome();
        List<Integer> bCromosome = b.getCromosome();
        List<Integer> newCromosome = new ArrayList<>(aCromosome.size());

        //Generate two random positions
        Random generator = RandomGenerator.getGenerator();
        int cromosomeLength = aCromosome.size();
        int lowerLimit = generator.nextInt(cromosomeLength);
        int upperLimit = generator.nextInt(cromosomeLength);
        
        //Reorder limits
        if (lowerLimit > upperLimit) {
            int aux = lowerLimit;
            lowerLimit = upperLimit;
            upperLimit = aux;
        }


        ArrayList<Integer> usedValues = new ArrayList<>();
        for (int i = lowerLimit; i < upperLimit; i++) {
            newCromosome.set(i, aCromosome.get(i));
            usedValues.add(aCromosome.get(i));
        }


        int currentPos = 0;
        int cIndex = 0;
        while (cIndex < cromosomeLength && currentPos<cromosomeLength) {
            // Check if we must ignore value
            if (usedValues.contains(bCromosome.get(cIndex))){
                cIndex++;
                continue;
            }

            //Check if the pointer in the new cromosome should jump over the bounded limits.
            if (currentPos == lowerLimit)
                currentPos = upperLimit+1;

            if (currentPos == cromosomeLength)
                break;

            //Place number in actual pointer
            newCromosome.set(currentPos, bCromosome.get(cIndex));

            //Move pointers
            cIndex++;
            currentPos++;

        }

        return new Individual(newCromosome);
    }
}
