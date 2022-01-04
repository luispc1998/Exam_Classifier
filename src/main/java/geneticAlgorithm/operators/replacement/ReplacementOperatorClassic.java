package geneticAlgorithm.operators.replacement;

import geneticAlgorithm.fitnessFunctions.FitnessFunction;
import geneticAlgorithm.Individual;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This implementation takes mainly the children, but will consider an specified on constriction number of elite individuals
 * from the previous one, that will substitute the worst fitness individuals in the new one.
 */
public class ReplacementOperatorClassic implements ReplacementOperator {

    /**
     * Number of individuals that will be directly passed from the previous generation to the following one
     * (based on the highest fitness).
     */
    private int eliteNumber;

    /**
     * Default constructor for the class.
     * @param eliteNumber Number of elite individuals to be directly transfered to the new generation.
     */
    public ReplacementOperatorClassic(int eliteNumber) {
        this.eliteNumber = eliteNumber;
    }

    @Override
    public List<Individual> doReplacement(List<Individual> prevGeneration, List<Individual> children,
                                          FitnessFunction fitnessFunction) {


        prevGeneration.sort(Comparator.comparingDouble(c -> c.getFitnessScore(fitnessFunction)));
        children.sort(Comparator.comparingDouble(c -> c.getFitnessScore(fitnessFunction)));


        int limit = Math.min(eliteNumber, prevGeneration.size());
        for (int i = 0; i < limit; i++) {
           children.set(children.size()-1-i, prevGeneration.get(i));
        }

        return children;
    }



}
