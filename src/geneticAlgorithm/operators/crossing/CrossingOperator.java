package geneticAlgorithm.operators.crossing;

import geneticAlgorithm.Individual;

import java.util.List;

public interface CrossingOperator {

    List<Individual> doCrossing(Individual a, Individual b);

}
