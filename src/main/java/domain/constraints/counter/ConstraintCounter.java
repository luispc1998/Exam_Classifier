package domain.constraints.counter;

import domain.constraints.Constraint;
import domain.constraints.types.softConstrictions.fullySoftConstraints.*;
import domain.constraints.types.softConstrictions.userConstraints.*;

/**
 * This counts the times each type of constriction is not fulfilled.
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
     * @param timeDisplacementConstriction the {@link Constraint}
     *                                     whose condition was not fulfilled.
     */
    void count(TimeDisplacementConstraint timeDisplacementConstriction);

    /**
     * Increments the value of the counter for {@link DayBannedConstraint}
     * @param daysBannedConstriction the {@link Constraint}
     *                                     whose condition was not fulfilled.
     */
    void count(DayBannedConstraint daysBannedConstriction);

    /**
     * Increments the value of the counter for {@link SameDayConstraint}
     * @param sameDayConstriction the {@link Constraint}
     *                               whose condition was not fulfilled.
     */
    void count(SameDayConstraint sameDayConstriction);

    /**
     * Increments the value of the counter for {@link UnclassifiedExamsConstraint}
     * @param unclassifiedExamsConstriction the {@link Constraint}
     *                                    whose condition was not fulfilled.
     */
    void count(UnclassifiedExamsConstraint unclassifiedExamsConstriction);

    /**
     * Increments the value of the counter for {@link DifferentDayConstraint}
     * @param differentDayConstriction the {@link Constraint}
     *                                    whose condition was not fulfilled.
     */
    void count(DifferentDayConstraint differentDayConstriction);

    /**
     * Increments the value of the counter for {@link OrderExamsConstraint}
     * @param orderExamsConstriction the {@link Constraint}
     *                                       whose condition was not fulfilled.
     */
    void count(OrderExamsConstraint orderExamsConstriction);

    /**
     * Increments the value of the counter for {@link SameCourseDifferentDayConstraint}
     * @param sameCourseDifferentDayConstriction the {@link Constraint}
     *                                        whose condition was not fulfilled.
     */
    void count(SameCourseDifferentDayConstraint sameCourseDifferentDayConstriction);

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
     * @param dayIntervalConstriction the {@link Constraint}
     *                                       whose condition was not fulfilled.
     */
    void count(DayIntervalConstraint dayIntervalConstriction);

    /**
     * Returns the current counter for {@link TimeDisplacementConstraint}
     * @return the current counter for {@link TimeDisplacementConstraint}
     */
    int getCountOfTimeDisplacementConstriction();

    /**
     * Returns the current counter for {@link DayBannedConstraint}
     * @return the current counter for {@link DayBannedConstraint}
     */
    int getCountOfDaysBannedConstriction();

    /**
     * Returns the current counter for {@link SameDayConstraint}
     * @return the current counter for {@link SameDayConstraint}
     */
    int getCountOfSameDayConstriction();

    /**
     * Returns the current counter for {@link UnclassifiedExamsConstraint}
     * @return the current counter for {@link UnclassifiedExamsConstraint}
     */
    int getCountOfUnclassifiedExamsConstriction();

    /**
     * Returns the current counter for {@link DifferentDayConstraint}
     * @return the current counter for {@link DifferentDayConstraint}
     */
    int getCountOfDifferentDayConstriction();

    /**
     * Returns the current counter for {@link OrderExamsConstraint}
     * @return the current counter for {@link OrderExamsConstraint}
     */
    int getCountOrderExamsConstriction();

    /**
     * Returns the current counter for {@link SameCourseDifferentDayConstraint}
     * @return the current counter for {@link SameCourseDifferentDayConstraint}
     */
    int getCountSameCourseDifferentDayConstriction();

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
    int getDayIntervalConstrictionCounter();

}
