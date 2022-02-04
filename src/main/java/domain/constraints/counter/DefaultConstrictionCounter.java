package domain.constraints.counter;

import domain.constraints.types.softConstrictions.fullySoftConstraints.*;
import domain.constraints.types.softConstrictions.userConstraints.*;

/**
 * This is a default implementation for {@code ConstrictionCounter}.
 */
public class DefaultConstrictionCounter implements ConstrictionCounter {

    private int timeDisplacementCounter;
    private int daysBannedCounter;
    private int sameDayCounter;
    private int unclassifiedExamsCounter;
    private int differentDayCounter;
    private int orderExamsCounter;
    private int sameCourseDifferentDayCounter;
    private long prohibitedIntervalPenalizationCounter;
    private long unbalancedDaysPenalizationCounter;
    private double numericalComplexityPenalizationCounter;
    private int dayIntervalConstrictionCounter;

    @Override
    public void count(TimeDisplacementConstraint timeDisplacementConstriction) {
        timeDisplacementCounter++;
    }

    @Override
    public void count(DayBannedConstraint dayBannedConstriction) {
        daysBannedCounter++;
    }

    @Override
    public void count(SameDayConstraint sameDayConstriction) {
        sameDayCounter++;
    }

    @Override
    public void count(UnclassifiedExamsConstraint unclassifiedExamsConstriction) {
        unclassifiedExamsCounter++;
    }

    @Override
    public void count(DifferentDayConstraint differentDayConstriction) {
        differentDayCounter++;
    }

    @Override
    public void count(OrderExamsConstraint orderExamsConstriction) {
        orderExamsCounter++;
    }

    @Override
    public void count(SameCourseDifferentDayConstraint sameCourseDifferentDayConstriction) {
        sameCourseDifferentDayCounter = sameCourseDifferentDayConstriction.getOccurrences();
    }

    @Override
    public void count(ProhibitedIntervalPenalization prohibitedIntervalPenalization) {
        prohibitedIntervalPenalizationCounter = prohibitedIntervalPenalization.getMinutes();
    }

    @Override
    public void count(UnbalancedDaysPenalization unbalancedDaysPenalization) {
        unbalancedDaysPenalizationCounter = unbalancedDaysPenalization.getMinutes();
    }

    @Override
    public void count(NumericalComplexityPenalization numericalComplexityPenalization) {
        numericalComplexityPenalizationCounter = numericalComplexityPenalization.getAccumulator();
    }

    @Override
    public void count(DayIntervalConstraint dayIntervalConstriction) {
        dayIntervalConstrictionCounter++;
    }

    @Override
    public int getCountOfTimeDisplacementConstriction() {
        return timeDisplacementCounter;
    }

    @Override
    public int getCountOfDaysBannedConstriction() {
        return daysBannedCounter;
    }

    @Override
    public int getCountOfSameDayConstriction() {
        return sameDayCounter;
    }

    @Override
    public int getCountOfUnclassifiedExamsConstriction() {
        return unclassifiedExamsCounter;
    }

    @Override
    public int getCountOfDifferentDayConstriction() {
        return differentDayCounter;
    }

    @Override
    public int getCountOrderExamsConstriction() {
        return orderExamsCounter;
    }

    @Override
    public int getCountSameCourseDifferentDayConstriction() {
        return sameCourseDifferentDayCounter;
    }

    @Override
    public long getCountProhibitedIntervalPenalization() {
        return prohibitedIntervalPenalizationCounter;
    }

    @Override
    public long getCountUnbalancedDaysPenalization() {
        return unbalancedDaysPenalizationCounter;
    }

    @Override
    public double getNumericalComplexityPenalization() {
        return numericalComplexityPenalizationCounter;
    }

    @Override
    public int getDayIntervalConstrictionCounter() {
        return dayIntervalConstrictionCounter;
    }
}
