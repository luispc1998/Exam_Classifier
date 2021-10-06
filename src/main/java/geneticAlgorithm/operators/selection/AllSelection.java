package geneticAlgorithm.operators.selection;

import geneticAlgorithm.Individual;
import fitnessFunctions.FitnessFunction;

import java.util.List;

public class AllSelection implements SelectionOperator {

    public int indexPointer;
    public int problemSize;

    public AllSelection(int problemSize) {
        this.problemSize = problemSize;
        reset();
    }

    @Override
    public Individual selection(List<Individual> population, FitnessFunction fitnessFunction) {
        indexPointer++;
        preValidation(population.size());
        return population.get(indexPointer);
    }

    private void preValidation(int size) {
        if (problemSize != size) {
            throw new IllegalStateException("AllSelection is configured for problem size: " + problemSize +
                    ", but received: " + size);
        }
        if (indexPointer >= problemSize){
            throw new IllegalStateException("AllSelection operator index pointer (" + indexPointer
                    + ") exceeded problem size (" + problemSize + ").");
        }
    }

    public void reset() {
        indexPointer = -1;
    }

    @Override
    public int maxPairs() {
        return problemSize/2;
    }


}
