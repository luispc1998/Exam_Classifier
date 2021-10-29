package geneticAlgorithm.operators.crossing;

import geneticAlgorithm.Individual;

import java.util.List;

/**
 * This operator creates a new {@code Individual} based on two other ones.
 */
public interface CrossingOperator {

    /**
     * Method to perform the crossing of the two parameter individuals.
     *
     * <p>
     * Note that there exists that both individuals are in fact the same.
     * @param a First individual.
     * @param b Second individual.
     * @return Returns a set of {@code Individual} based on the crossing of the parameters.
     */
    List<Individual> doCrossing(Individual a, Individual b);

}
