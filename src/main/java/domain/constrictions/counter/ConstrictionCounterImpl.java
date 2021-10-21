package domain.constrictions.counter;

import domain.constrictions.types.DayBannedConstriction;
import domain.constrictions.types.DifferentDayConstriction;
import domain.constrictions.types.SameDayConstriction;
import domain.constrictions.types.TimeDisplacementConstriction;
import domain.constrictions.types.singles.UnclassifiedExamsConstriction;

public class ConstrictionCounterImpl implements ConstrictionCounter {

    private int timeDisplacementCounter;
    private int daysBannedCounter;
    private int sameDayCounter;
    private int unclassifiedExamsCounter;
    private int differentDayCounter;



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
}
