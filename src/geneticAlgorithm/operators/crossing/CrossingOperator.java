package geneticAlgorithm.operators.crossing;

import geneticAlgorithm.Individual;

public interface CrossingOperator {

    Individual cross(Individual a, Individual b);

}
