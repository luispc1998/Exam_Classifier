package constrictions.counter;

import constrictions.types.DayBannedConstriction;
import constrictions.types.SameDayConstriction;
import constrictions.types.TimeDisplacementConstriction;

public interface ConstrictionCounter {


    void count(TimeDisplacementConstriction timeDisplacementConstriction);

    void count(DayBannedConstriction daysBannedConstriction);

    void count(SameDayConstriction sameDayConstriction);
}
