package domain.constraints.counter;

import domain.constraints.Constraint;
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
     * @param timeDisplacementConstraint the {@link Constraint}
     *                                     whose condition was not fulfilled.
     */
    void count(TimeDisplacementConstraint timeDisplacementConstraint);

    /**
     * Increments the value of the counter for {@link DayBannedConstraint}
     * @param daysBannedConstraint the {@link Constraint}
     *                                     whose condition was not fulfilled.
     */
    void count(DayBannedConstraint daysBannedConstraint);

    /**
     * Increments the value of the counter for {@link SameDayConstraint}
     * @param sameDayConstraint the {@link Constraint}
     *                               whose condition was not fulfilled.
     */
    void count(SameDayConstraint sameDayConstraint);

    /**
     * Increments the value of the counter for {@link UnclassifiedExamsConstraint}
     * @param unclassifiedExamsConstraint the {@link Constraint}
     *                                    whose condition was not fulfilled.
     */
    void count(UnclassifiedExamsConstraint unclassifiedExamsConstraint);

    /**
     * Increments the value of the counter for {@link DifferentDayConstraint}
     * @param differentDayConstraint the {@link Constraint}
     *                                    whose condition was not fulfilled.
     */
    void count(DifferentDayConstraint differentDayConstraint);

    /**
     * Increments the value of the counter for {@link OrderExamsConstraint}
     * @param orderExamsConstraint the {@link Constraint}
     *                                       whose condition was not fulfilled.
     */
    void count(OrderExamsConstraint orderExamsConstraint);

    /**
     * Increments the value of the counter for {@link SameCourseDifferentDayConstraint}
     * @param sameCourseDifferentDayConstraint the {@link Constraint}
     *                                        whose condition was not fulfilled.
     */
    void count(SameCourseDifferentDayConstraint sameCourseDifferentDayConstraint);

    /**
     * Increments the value of the counter for {@link ProhibitedIntervalPenalization}
     * @param prohibitedIntervalPenalization the {@link Constraint}
     *                                        whose condition was not fulfilled.
     */
    void count(ProhibitedIntervalPenalization prohibitedIntervalPenalization);

    /**
     * Increments the value of the counter for {@link UnbalancedDaysPenalization}
     * @param unbalancedDaysPenalization the {@link Constraint}
     *                                        whose condition was not fulfilled.
     */
    void count(UnbalancedDaysPenalization unbalancedDaysPenalization);

    /**
     * Increments the value of the counter for {@link UnbalancedDaysPenalization}
     * @param numericalComplexityPenalization the {@link Constraint}
     *                                        whose condition was not fulfilled.
     */
    void count(NumericalComplexityPenalization numericalComplexityPenalization);

    /**
     * Increments the value of the counter for {@link DayIntervalConstraint}
     * @param dayIntervalConstraint the {@link domain.constraints.Constraint}
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
     * Returns the current counter for {@link UnclassifiedExamsConstraint}
     * @return the current counter for {@link UnclassifiedExamsConstraint}
     */
    int getCountOfUnclassifiedExamsConstraint();

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
     * Returns the current counter for {@link ProhibitedIntervalPenalization}
     * @return the current counter for {@link ProhibitedIntervalPenalization}
     */
    long getCountProhibitedIntervalPenalization();


    /**
     * Returns the current counter for {@link UnbalancedDaysPenalization}
     * @return the current counter for {@link UnbalancedDaysPenalization}
     */
    long getCountUnbalancedDaysPenalization();

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
