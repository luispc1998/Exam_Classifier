package geneticAlgorithm.operators.replacement;

import geneticAlgorithm.Individual;
import geneticAlgorithm.fitnessFunctions.FitnessFunction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This implementation mixes in one list the actual and following generation, orders it by fitness, and takes the
 * individuals with higher fitness.
 */
public class ReplacementOperatorFitnessBiased implements ReplacementOperator {


    @Override
    public List<Individual> doReplacement(List<Individual> prevGeneration, List<Individual> childs,
                                          FitnessFunction fitnessFunction) {

        List<Individual> tmp = new ArrayList<>();
        tmp.addAll(childs);
        tmp.addAll(prevGeneration);
        tmp.sort(Comparator.comparingDouble(c -> c.getFitnessScore(fitnessFunction)));

        return tmp.subList(0, prevGeneration.size());
    }



}
