package constrictions.counter;

import constrictions.types.DaysBannedConstriction;
import constrictions.types.SameDayConstriction;
import constrictions.types.TimeDisplacementConstriction;

public interface ConstrictionCounter {


    void count(TimeDisplacementConstriction timeDisplacementConstriction);

    void count(DaysBannedConstriction daysBannedConstriction);

    void count(SameDayConstriction sameDayConstriction);
}
