package geneticAlgorithm.operators.replacement;

import geneticAlgorithm.Individual;
import fitnessFunctions.FitnessFunction;

import java.util.List;

public interface ReplacementOperator {


    List<Individual> doReplacement(Individual father, Individual mother, List<Individual> child,
                                   FitnessFunction fitnessFunction);
}
