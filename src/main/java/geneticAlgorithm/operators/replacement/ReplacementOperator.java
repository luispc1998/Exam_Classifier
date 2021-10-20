package geneticAlgorithm.operators.replacement;

import geneticAlgorithm.Individual;
import fitnessFunctions.FitnessFunction;

import java.util.List;

public interface ReplacementOperator {


    List<Individual> doReplacement(List<Individual> prevGeneration, List<Individual> child,
                                   FitnessFunction fitnessFunction);
}
