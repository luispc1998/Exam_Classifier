package geneticAlgorithm.fitnessFunctions;

import domain.ExamsSchedule;
import domain.constraints.counter.ConstraintCounter;
import domain.constraints.counter.DefaultConstraintCounter;
import domain.constraints.types.softConstraints.SoftConstraint;
import domain.constraints.types.softConstraints.fullySoftConstraints.NumericalComplexityPenalization;
import domain.constraints.types.softConstraints.fullySoftConstraints.RestingIntervalPenalization;
import domain.constraints.types.softConstraints.fullySoftConstraints.SameCourseDifferentDayConstraint;
import domain.constraints.types.softConstraints.fullySoftConstraints.UnscheduledExamsConstraint;
import domain.constraints.types.softConstraints.userConstraints.*;
import geneticAlgorithm.Individual;
import domain.configuration.WeightConfigurer;
import greedyAlgorithm.ChromosomeDecoder;


/**
 * This is a linear fitness function.
 *
 * <p>
 * It will add the times each type of constraint was not fulfilled. It will use a {@code ChromosomeDecoder} for such
 * a purpose. Each type of constraint will also have a weight, that will be a coefficient in the linear function.
 *
 * <p>
 * An example of the behaviour could be. Consider constraints types A and B, and then the set of not satisfied
 * constraints: {a2, a4, a5, b3, b8}. Let Wa and Wb, be the weights of types A and B respectively. Then the linear function
 * would be: Wa * 3 + Wb * 2
 */
public class LinearFitnessFunction implements FitnessFunction {

    /**
     * Link to the {@link ExamsSchedule} instance, where this will check the exam schedule and constraints.
     */
    private final ExamsSchedule examsSchedule;

    /**
     * Instance of {@code ChromosomeDecoder}.
     */
    private final ChromosomeDecoder decoder;


    /**
     * Constructor for the class.
     * @param examsSchedule Instance where the function will check the exam schedule and constraints.
     */
    public LinearFitnessFunction(ExamsSchedule examsSchedule){
        this.decoder = new ChromosomeDecoder(examsSchedule.getConfigurer());
        this.examsSchedule = examsSchedule;
    }

    @Override
    public double apply(Individual a) {

        examsSchedule.resetScheduling();

        // Deconde the Chromosome
        decoder.decode(a, examsSchedule);

        //Count constraints
        ConstraintCounter counter = new DefaultConstraintCounter();
        for (SoftConstraint constraint: examsSchedule.getConstraints()) {
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
        WeightConfigurer wc = examsSchedule.getConfigurer().getWeightConfigurer();

        return counter.getCountOfTimeDisplacementConstraint()
                        * wc.getConstraintWeight(TimeDisplacementConstraint.CONSTRICTION_ID) +
                counter.getCountOfDaysBannedConstraint()
                        * wc.getConstraintWeight(DayBannedConstraint.CONSTRICTION_ID) +
                counter.getCountOfSameDayConstraint()
                        * wc.getConstraintWeight(SameDayConstraint.CONSTRICTION_ID) +
                counter.getCountOfUnscheduledExamsConstraint()
                        * wc.getConstraintWeight(UnscheduledExamsConstraint.CONSTRICTION_ID) +
                counter.getCountOfDifferentDayConstraint()
                        * wc.getConstraintWeight(DifferentDayConstraint.CONSTRICTION_ID) +
                counter.getCountOrderExamsConstraint()
                        * wc.getConstraintWeight(OrderExamsConstraint.CONSTRICTION_ID) +
                counter.getCountSameCourseDifferentDayConstraint()
                        * wc.getConstraintWeight(SameCourseDifferentDayConstraint.CONSTRICTION_ID) +
                counter.getCountRestingIntervalPenalization()
                        * wc.getConstraintWeight(RestingIntervalPenalization.CONSTRICTION_ID) +
                counter.getNumericalComplexityPenalization()
                        * wc.getConstraintWeight(NumericalComplexityPenalization.CONSTRICTION_ID) +
                counter.getCountDayIntervalConstraint()
                        * wc.getConstraintWeight(DayIntervalConstraint.CONSTRICTION_ID);

    }
}
