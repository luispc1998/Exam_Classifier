package domain.constrictions.counter;

import domain.constrictions.types.weakConstriction.fullyWeakConstrictions.*;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.*;

/**
 * This counts the times each type of constriction is not fulfilled.
 * <p>
 * Having the exact number of occurrences means that new {@link fitnessFunctions.FitnessFunction} implementations
 * can have any kind of function that it is needed.
 * <p>
 * To implement this functionality, Visitor design pattern was followed.
 *
 */
public interface ConstrictionCounter {

    /**
     * Increments the value of the counter for {@link TimeDisplacementConstriction}
     * @param timeDisplacementConstriction the {@link domain.constrictions.Constriction}
     *                                     whose condition was not fulfilled.
     */
    void count(TimeDisplacementConstriction timeDisplacementConstriction);

    /**
     * Increments the value of the counter for {@link DayBannedConstriction}
     * @param daysBannedConstriction the {@link domain.constrictions.Constriction}
     *                                     whose condition was not fulfilled.
     */
    void count(DayBannedConstriction daysBannedConstriction);

    /**
     * Increments the value of the counter for {@link SameDayConstriction}
     * @param sameDayConstriction the {@link domain.constrictions.Constriction}
     *                               whose condition was not fulfilled.
     */
    void count(SameDayConstriction sameDayConstriction);

    /**
     * Increments the value of the counter for {@link UnclassifiedExamsConstriction}
     * @param unclassifiedExamsConstriction the {@link domain.constrictions.Constriction}
     *                                    whose condition was not fulfilled.
     */
    void count(UnclassifiedExamsConstriction unclassifiedExamsConstriction);

    /**
     * Increments the value of the counter for {@link DifferentDayConstriction}
     * @param differentDayConstriction the {@link domain.constrictions.Constriction}
     *                                    whose condition was not fulfilled.
     */
    void count(DifferentDayConstriction differentDayConstriction);

    /**
     * Increments the value of the counter for {@link OrderExamsConstriction}
     * @param orderExamsConstriction the {@link domain.constrictions.Constriction}
     *                                       whose condition was not fulfilled.
     */
    void count(OrderExamsConstriction orderExamsConstriction);

    /**
     * Increments the value of the counter for {@link SameCourseDifferentDayConstriction}
     * @param sameCourseDifferentDayConstriction the {@link domain.constrictions.Constriction}
     *                                        whose condition was not fulfilled.
     */
    void count(SameCourseDifferentDayConstriction sameCourseDifferentDayConstriction);

    /**
     * Increments the value of the counter for {@link ProhibitedIntervalPenalization}
     * @param prohibitedIntervalPenalization the {@link domain.constrictions.Constriction}
     *                                        whose condition was not fulfilled.
     */
    void count(ProhibitedIntervalPenalization prohibitedIntervalPenalization);

    /**
     * Increments the value of the counter for {@link UnbalancedDaysPenalization}
     * @param unbalancedDaysPenalization the {@link domain.constrictions.Constriction}
     *                                        whose condition was not fulfilled.
     */
    void count(UnbalancedDaysPenalization unbalancedDaysPenalization);

    /**
     * Increments the value of the counter for {@link UnbalancedDaysPenalization}
     * @param numericalComplexityPenalization the {@link domain.constrictions.Constriction}
     *                                        whose condition was not fulfilled.
     */
    void count(NumericalComplexityPenalization numericalComplexityPenalization);

    /**
     * Increments the value of the counter for {@link DayIntervalConstriction}
     * @param dayIntervalConstriction the {@link domain.constrictions.Constriction}
     *                                       whose condition was not fulfilled.
     */
    void count(DayIntervalConstriction dayIntervalConstriction);

    /**
     * Returns the current counter for {@link TimeDisplacementConstriction}
     * @return the current counter for {@link TimeDisplacementConstriction}
     */
    int getCountOfTimeDisplacementConstriction();

    /**
     * Returns the current counter for {@link DayBannedConstriction}
     * @return the current counter for {@link DayBannedConstriction}
     */
    int getCountOfDaysBannedConstriction();

    /**
     * Returns the current counter for {@link SameDayConstriction}
     * @return the current counter for {@link SameDayConstriction}
     */
    int getCountOfSameDayConstriction();

    /**
     * Returns the current counter for {@link UnclassifiedExamsConstriction}
     * @return the current counter for {@link UnclassifiedExamsConstriction}
     */
    int getCountOfUnclassifiedExamsConstriction();

    /**
     * Returns the current counter for {@link DifferentDayConstriction}
     * @return the current counter for {@link DifferentDayConstriction}
     */
    int getCountOfDifferentDayConstriction();

    /**
     * Returns the current counter for {@link OrderExamsConstriction}
     * @return the current counter for {@link OrderExamsConstriction}
     */
    int getCountOrderExamsConstriction();

    /**
     * Returns the current counter for {@link SameCourseDifferentDayConstriction}
     * @return the current counter for {@link SameCourseDifferentDayConstriction}
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
     * Returns the current counter for {@link DayIntervalConstriction}
     * @return the current counter for {@link DayIntervalConstriction}
     */
    int getDayIntervalConstrictionCounter();

}
