package domain.constraints.counter;

import domain.constraints.types.softConstraints.fullySoftConstraints.*;
import domain.constraints.types.softConstraints.userConstraints.*;

/**
 * This counts the times each type of constraint is not fulfilled.
 * <p>
 * Having the exact number of occurrences means that new {@link geneticAlgorithm.fitnessFunctions.FitnessFunction} implementations
 * can have any kind of function that it is needed.
 * <p>
 * To implement this functionality, Visitor design pattern was followed.
 *
 */
public interface ConstraintCounter {

    /**
     * Increments the value of the counter for {@link TimeDisplacementConstraint}
     * @param timeDisplacementConstraint the constraint
     *                                     whose condition was not fulfilled.
     */
    void count(TimeDisplacementConstraint timeDisplacementConstraint);

    /**
     * Increments the value of the counter for {@link DayBannedConstraint}
     * @param daysBannedConstraint the constraint
     *                                     whose condition was not fulfilled.
     */
    void count(DayBannedConstraint daysBannedConstraint);

    /**
     * Increments the value of the counter for {@link SameDayConstraint}
     * @param sameDayConstraint the constraint
     *                               whose condition was not fulfilled.
     */
    void count(SameDayConstraint sameDayConstraint);

    /**
     * Increments the value of the counter for {@link UnscheduledExamsConstraint}
     * @param unscheduledExamsConstraint the constraint
     *                                    whose condition was not fulfilled.
     */
    void count(UnscheduledExamsConstraint unscheduledExamsConstraint);

    /**
     * Increments the value of the counter for {@link DifferentDayConstraint}
     * @param differentDayConstraint the constraint
     *                                    whose condition was not fulfilled.
     */
    void count(DifferentDayConstraint differentDayConstraint);

    /**
     * Increments the value of the counter for {@link OrderExamsConstraint}
     * @param orderExamsConstraint the constraint
     *                                       whose condition was not fulfilled.
     */
    void count(OrderExamsConstraint orderExamsConstraint);

    /**
     * Increments the value of the counter for {@link SameCourseDifferentDayConstraint}
     * @param sameCourseDifferentDayConstraint the constraint
     *                                        whose condition was not fulfilled.
     */
    void count(SameCourseDifferentDayConstraint sameCourseDifferentDayConstraint);

    /**
     * Increments the value of the counter for {@link RestingIntervalPenalization}
     * @param restingIntervalPenalization the constraint
     *                                        whose condition was not fulfilled.
     */
    void count(RestingIntervalPenalization restingIntervalPenalization);

    /**
     * Increments the value of the counter for {@link UnbalancedDaysPenalization}
     * @param unbalancedDaysPenalization the constraint
     *                                        whose condition was not fulfilled.
     */
    void count(UnbalancedDaysPenalization unbalancedDaysPenalization);

    /**
     * Increments the value of the counter for {@link UnbalancedDaysPenalization}
     * @param numericalComplexityPenalization the constraint
     *                                        whose condition was not fulfilled.
     */
    void count(NumericalComplexityPenalization numericalComplexityPenalization);

    /**
     * Increments the value of the counter for {@link DayIntervalConstraint}
     * @param dayIntervalConstraint the constraint
     *                                       whose condition was not fulfilled.
     */
    void count(DayIntervalConstraint dayIntervalConstraint);

    /**
     * Returns the current counter for {@link TimeDisplacementConstraint}
     * @return the current counter for {@link TimeDisplacementConstraint}
     */
    int getCountOfTimeDisplacementConstraint();

    /**
     * Returns the current counter for {@link DayBannedConstraint}
     * @return the current counter for {@link DayBannedConstraint}
     */
    int getCountOfDaysBannedConstraint();

    /**
     * Returns the current counter for {@link SameDayConstraint}
     * @return the current counter for {@link SameDayConstraint}
     */
    int getCountOfSameDayConstraint();

    /**
     * Returns the current counter for {@link UnscheduledExamsConstraint}
     * @return the current counter for {@link UnscheduledExamsConstraint}
     */
    int getCountOfUnscheduledExamsConstraint();

    /**
     * Returns the current counter for {@link DifferentDayConstraint}
     * @return the current counter for {@link DifferentDayConstraint}
     */
    int getCountOfDifferentDayConstraint();

    /**
     * Returns the current counter for {@link OrderExamsConstraint}
     * @return the current counter for {@link OrderExamsConstraint}
     */
    int getCountOrderExamsConstraint();

    /**
     * Returns the current counter for {@link SameCourseDifferentDayConstraint}
     * @return the current counter for {@link SameCourseDifferentDayConstraint}
     */
    int getCountSameCourseDifferentDayConstraint();

    /**
     * Returns the current counter for {@link RestingIntervalPenalization}
     * @return the current counter for {@link RestingIntervalPenalization}
     */
    long getCountRestingIntervalPenalization();

    /**
     * Returns the current counter for {@link NumericalComplexityPenalization}
     * @return the current counter for {@link NumericalComplexityPenalization}
     */
    double getNumericalComplexityPenalization();

    /**
     * Returns the current counter for {@link DayIntervalConstraint}
     * @return the current counter for {@link DayIntervalConstraint}
     */
    int getCountDayIntervalConstraint();

}
