package constrictions.counter;

import constrictions.types.DaysBannedConstriction;
import constrictions.types.SameDayConstriction;
import constrictions.types.TimeDisplacementConstriction;

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
    public void count(DaysBannedConstriction daysBannedConstriction) {
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
