package geneticAlgorithm.fitnessFunctions;

import domain.DataHandler;
import domain.constraints.counter.ConstraintCounter;
import domain.constraints.counter.DefaultConstraintCounter;
import domain.constraints.types.softConstraints.SoftConstraints;
import domain.constraints.types.softConstraints.fullySoftConstraints.*;
import domain.constraints.types.softConstraints.userConstraints.*;
import geneticAlgorithm.Individual;
import geneticAlgorithm.configuration.WeightConfigurer;
import greedyAlgorithm.ChromosomeDecoder;


/**
 * This is a linear fitness function.
 *
 * <p>
 * It will add the times each type of constraint was not fulfilled. It will use a {@code CromosomeDecoder} for such
 * a purpose. Each type of constraint will also have a weight, that will be a coefficient in the linear function.
 *
 * <p>
 * An example of the behaviour could be. Consider constraints types A and B, and then the set of not satisfied
 * constraints: {a2, a4, a5, b3, b8}. Let Wa and Wb, be the weights of types A and B respectively. Then the linear function
 * would be: Wa * 3 + Wb * 2
 */
public class LinearFitnessFunction implements FitnessFunction {

    /**
     * Link to the {@link DataHandler} instance, where this will check the exam schedule and constraints.
     */
    private final DataHandler dataHandler;

    /**
     * Instance of {@code CromosomeDecoder}
     */
    private final ChromosomeDecoder decoder;


    /**
     * Constructor for the class
     * @param dataHandler Instance where the function will check the exam schedule and constraints.
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

        //Count constraints
        ConstraintCounter counter = new DefaultConstraintCounter();
        for (SoftConstraints constraint: dataHandler.getConstraints()) {
                constraint.checkConstraint(counter);
        }

        //Do the formula.
        return formula(counter);
    }

    /**
     * Linear formula for the fitness function.
     * @param counter Object with the count of how many times each constraint type was ons satisfied.
     * @return the final fitness value.
     */
    private double formula(ConstraintCounter counter) {
        WeightConfigurer wc = dataHandler.getConfigurer().getWeightConfigurer();

        return counter.getCountOfTimeDisplacementConstraint()
                        * wc.getConstraintWeight(TimeDisplacementConstraint.CONSTRICTION_ID) +
                counter.getCountOfDaysBannedConstraint()
                        * wc.getConstraintWeight(DayBannedConstraint.CONSTRICTION_ID) +
                counter.getCountOfSameDayConstraint()
                        * wc.getConstraintWeight(SameDayConstraint.CONSTRICTION_ID) +
                counter.getCountOfUnclassifiedExamsConstraint()
                        * wc.getConstraintWeight(UnclassifiedExamsConstraint.CONSTRICTION_ID) +
                counter.getCountOfDifferentDayConstraint()
                        * wc.getConstraintWeight(DifferentDayConstraint.CONSTRICTION_ID) +
                counter.getCountOrderExamsConstraint()
                        * wc.getConstraintWeight(OrderExamsConstraint.CONSTRICTION_ID) +
                counter.getCountSameCourseDifferentDayConstraint()
                        * wc.getConstraintWeight(SameCourseDifferentDayConstraint.CONSTRICTION_ID) +
                counter.getCountProhibitedIntervalPenalization()
                        * wc.getConstraintWeight(ProhibitedIntervalPenalization.CONSTRICTION_ID) +
                counter.getNumericalComplexityPenalization()
                        * wc.getConstraintWeight(NumericalComplexityPenalization.CONSTRICTION_ID) +
                counter.getCountDayIntervalConstraint()
                        * wc.getConstraintWeight(DayIntervalConstraint.CONSTRICTION_ID);

    }
}
