package geneticAlgorithm.fitnessFunctions.greedyAlgorithm;

import domain.DataHandler;
import domain.constrictions.counter.ConstrictionCounter;
import domain.constrictions.counter.DefaultConstrictionCounter;
import domain.constrictions.types.weakConstriction.WeakConstriction;
import domain.constrictions.types.weakConstriction.fullyWeakConstrictions.*;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.*;
import geneticAlgorithm.Individual;
import geneticAlgorithm.configuration.WeightConfigurer;
import geneticAlgorithm.fitnessFunctions.FitnessFunction;


/**
 * This is a linear fitness function.
 *
 * <p>
 * It will add the times each type of constriction was not fulfilled. It will use a {@code CromosomeDecoder} for such
 * a purpose. Each type of constriction will also have a weight, that will be a coefficient in the linear function.
 *
 * <p>
 * An example of the behaviour could be. Consider constrictions types A and B, and then the set of not satisfied
 * constrictions: {a2, a4, a5, b3, b8}. Let Wa and Wb, be the weights of types A and B respectively. Then the linear function
 * would be: Wa * 3 + Wb * 2
 */
public class LinearFitnessFunction implements FitnessFunction {

    /**
     * Link to the {@link DataHandler} instance, where this will check the exam schedule and constrictions.
     */
    private final DataHandler dataHandler;

    /**
     * Instance of {@code CromosomeDecoder}
     */
    private final ChromosomeDecoder decoder;


    /**
     * Constructor for the class
     * @param dataHandler Instance where the function will check the exam schedule and constrictions.
     */
    public LinearFitnessFunction(DataHandler dataHandler){
        this.decoder = new ChromosomeDecoder(dataHandler.getConfigurer());
        this.dataHandler = dataHandler;
    }

    @Override
    public double apply(Individual a) {

        dataHandler.resetScheduling();

        // Deconde the cromosome
        decoder.decodeNew(a, dataHandler);

        //Count constrictions
        ConstrictionCounter counter = new DefaultConstrictionCounter();
        for (WeakConstriction constriction: dataHandler.getConstrictions()) {
                constriction.checkConstriction(counter);
        }

        //Do the formula.
        return formula(counter);
    }

    /**
     * Linear formula for the fitness function.
     * @param counter Object with the count of how many times each constriction type was ons satisfied.
     * @return the final fitness value.
     */
    private double formula(ConstrictionCounter counter) {
        WeightConfigurer wc = dataHandler.getConfigurer().getWeightConfigurer();

        return counter.getCountOfTimeDisplacementConstriction()
                        * wc.getConstrictionWeight(TimeDisplacementConstriction.CONSTRICTION_ID) +
                counter.getCountOfDaysBannedConstriction()
                        * wc.getConstrictionWeight(DayBannedConstriction.CONSTRICTION_ID) +
                counter.getCountOfSameDayConstriction()
                        * wc.getConstrictionWeight(SameDayConstriction.CONSTRICTION_ID) +
                counter.getCountOfUnclassifiedExamsConstriction()
                        * wc.getConstrictionWeight(UnclassifiedExamsConstriction.CONSTRICTION_ID) +
                counter.getCountOfDifferentDayConstriction()
                        * wc.getConstrictionWeight(DifferentDayConstriction.CONSTRICTION_ID) +
                counter.getCountOrderExamsConstriction()
                        * wc.getConstrictionWeight(OrderExamsConstriction.CONSTRICTION_ID) +
                counter.getCountSameCourseDifferentDayConstriction()
                        * wc.getConstrictionWeight(SameCourseDifferentDayConstriction.CONSTRICTION_ID) +
                counter.getCountProhibitedIntervalPenalization()
                        * wc.getConstrictionWeight(ProhibitedIntervalPenalization.CONSTRICTION_ID) +
                counter.getCountUnbalancedDaysPenalization()
                        * wc.getConstrictionWeight(UnbalancedDaysPenalization.CONSTRICTION_ID) +
                counter.getNumericalComplexityPenalization()
                        * wc.getConstrictionWeight(NumericalComplexityPenalization.CONSTRICTION_ID) +
                counter.getDayIntervalConstrictionCounter()
                        * wc.getConstrictionWeight(DayIntervalConstriction.CONSTRICTION_ID);

    }
}
