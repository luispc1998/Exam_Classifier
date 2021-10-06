package domain.constrictions.counter;

import domain.constrictions.types.DayBannedConstriction;
import domain.constrictions.types.SameDayConstriction;
import domain.constrictions.types.TimeDisplacementConstriction;

public class ConstrictionCounterImpl implements ConstrictionCounter {

    private int timeDisplacementCounter;
    private int daysBannedCounter;
    private int sameDayCounter;

    public ConstrictionCounterImpl() {
        timeDisplacementCounter = 0;
        daysBannedCounter = 0;
        sameDayCounter = 0;
    }

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




    public int getTimeDisplacementCounter() {
        return timeDisplacementCounter;
    }

    public int getDaysBannedCounter() {
        return daysBannedCounter;
    }

    public int getSameDayCounter() {
        return sameDayCounter;
    }
}
