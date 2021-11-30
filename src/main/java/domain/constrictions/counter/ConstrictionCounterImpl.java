package domain.constrictions.counter;

import domain.constrictions.types.weakConstriction.fullyWeakConstrictions.*;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.*;

public class ConstrictionCounterImpl implements ConstrictionCounter {

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

    @Override
    public void count(TimeDisplacementConstriction timeDisplacementConstriction) {
        timeDisplacementCounter++;
    }

    @Override
    public void count(DayBannedConstriction dayBannedConstriction) {
        daysBannedCounter++;
    }

    @Override
    public void count(SameDayConstriction sameDayConstriction) {
        sameDayCounter++;
    }

    @Override
    public void count(UnclassifiedExamsConstriction unclassifiedExamsConstriction) {
        unclassifiedExamsCounter++;
    }

    @Override
    public void count(DifferentDayConstriction differentDayConstriction) {
        differentDayCounter++;
    }

    @Override
    public void count(OrderExamsConstriction orderExamsConstriction) {
        orderExamsCounter++;
    }

    @Override
    public void count(SameCourseDifferentDayConstriction sameCourseDifferentDayConstriction) {
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
}
