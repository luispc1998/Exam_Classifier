package geneticAlgorithm.fitnessFunctions;

import domain.DataHandler;
import domain.constraints.counter.ConstraintCounter;
import domain.constraints.counter.DefaultConstraintCounter;
import domain.constraints.types.softConstrictions.SoftConstraints;
import domain.constraints.types.softConstrictions.fullySoftConstraints.*;
import domain.constraints.types.softConstrictions.userConstraints.*;
import geneticAlgorithm.Individual;
import geneticAlgorithm.configuration.WeightConfigurer;
import greedyAlgorithm.ChromosomeDecoder;


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
        decoder.decode(a, dataHandler);

        //Count constrictions
        ConstraintCounter counter = new DefaultConstraintCounter();
        for (SoftConstraints constriction: dataHandler.getConstraints()) {
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
    private double formula(ConstraintCounter counter) {
        WeightConfigurer wc = dataHandler.getConfigurer().getWeightConfigurer();

        return counter.getCountOfTimeDisplacementConstriction()
                        * wc.getConstrictionWeight(TimeDisplacementConstraint.CONSTRICTION_ID) +
                counter.getCountOfDaysBannedConstriction()
                        * wc.getConstrictionWeight(DayBannedConstraint.CONSTRICTION_ID) +
                counter.getCountOfSameDayConstriction()
                        * wc.getConstrictionWeight(SameDayConstraint.CONSTRICTION_ID) +
                counter.getCountOfUnclassifiedExamsConstriction()
                        * wc.getConstrictionWeight(UnclassifiedExamsConstraint.CONSTRICTION_ID) +
                counter.getCountOfDifferentDayConstriction()
                        * wc.getConstrictionWeight(DifferentDayConstraint.CONSTRICTION_ID) +
                counter.getCountOrderExamsConstriction()
                        * wc.getConstrictionWeight(OrderExamsConstraint.CONSTRICTION_ID) +
                counter.getCountSameCourseDifferentDayConstriction()
                        * wc.getConstrictionWeight(SameCourseDifferentDayConstraint.CONSTRICTION_ID) +
                counter.getCountProhibitedIntervalPenalization()
                        * wc.getConstrictionWeight(ProhibitedIntervalPenalization.CONSTRICTION_ID) +
                counter.getNumericalComplexityPenalization()
                        * wc.getConstrictionWeight(NumericalComplexityPenalization.CONSTRICTION_ID) +
                counter.getDayIntervalConstrictionCounter()
                        * wc.getConstrictionWeight(DayIntervalConstraint.CONSTRICTION_ID);

    }
}
