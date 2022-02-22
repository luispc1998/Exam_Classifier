package domain.constraints.counter;

import domain.constraints.types.softConstraints.fullySoftConstraints.*;
import domain.constraints.types.softConstraints.userConstraints.*;

/**
 * This is a default implementation for {@code ConstraintCounter}.
 */
public class DefaultConstraintCounter implements ConstraintCounter {

    private int timeDisplacementCounter;
    private int daysBannedCounter;
    private int sameDayCounter;
    private int unclassifiedExamsCounter;
    private int differentDayCounter;
    private int orderExamsCounter;
    private int sameCourseDifferentDayCounter;
    private long restingIntervalPenalizationCounter;
    private long unbalancedDaysPenalizationCounter;
    private double numericalComplexityPenalizationCounter;
    private int dayIntervalCounter;

    @Override
    public void count(TimeDisplacementConstraint timeDisplacementConstraint) {
        timeDisplacementCounter++;
    }

    @Override
    public void count(DayBannedConstraint daysBannedConstraint) {
        daysBannedCounter++;
    }

    @Override
    public void count(SameDayConstraint sameDayConstraint) {
        sameDayCounter++;
    }

    @Override
    public void count(UnscheduledExamsConstraint unscheduledExamsConstraint) {
        unclassifiedExamsCounter++;
    }

    @Override
    public void count(DifferentDayConstraint differentDayConstraint) {
        differentDayCounter++;
    }

    @Override
    public void count(OrderExamsConstraint orderExamsConstraint) {
        orderExamsCounter++;
    }

    @Override
    public void count(SameCourseDifferentDayConstraint sameCourseDifferentDayConstraint) {
        sameCourseDifferentDayCounter = sameCourseDifferentDayConstraint.getOccurrences();
    }

    @Override
    public void count(RestingIntervalPenalization restingIntervalPenalization) {
        restingIntervalPenalizationCounter = restingIntervalPenalization.getMinutes();
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
    public void count(DayIntervalConstraint dayIntervalConstraint) {
        dayIntervalCounter++;
    }

    @Override
    public int getCountOfTimeDisplacementConstraint() {
        return timeDisplacementCounter;
    }

    @Override
    public int getCountOfDaysBannedConstraint() {
        return daysBannedCounter;
    }

    @Override
    public int getCountOfSameDayConstraint() {
        return sameDayCounter;
    }

    @Override
    public int getCountOfUnscheduledExamsConstraint() {
        return unclassifiedExamsCounter;
    }

    @Override
    public int getCountOfDifferentDayConstraint() {
        return differentDayCounter;
    }

    @Override
    public int getCountOrderExamsConstraint() {
        return orderExamsCounter;
    }

    @Override
    public int getCountSameCourseDifferentDayConstraint() {
        return sameCourseDifferentDayCounter;
    }

    @Override
    public long getCountRestingIntervalPenalization() {
        return restingIntervalPenalizationCounter;
    }

    @Override
    public double getNumericalComplexityPenalization() {
        return numericalComplexityPenalizationCounter;
    }

    @Override
    public int getCountDayIntervalConstraint() {
        return dayIntervalCounter;
    }
}
