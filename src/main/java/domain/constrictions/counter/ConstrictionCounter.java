package domain.constrictions.counter;

import domain.constrictions.types.DayBannedConstriction;
import domain.constrictions.types.SameDayConstriction;
import domain.constrictions.types.TimeDisplacementConstriction;

public interface ConstrictionCounter {


    void count(TimeDisplacementConstriction timeDisplacementConstriction);

    void count(DayBannedConstriction daysBannedConstriction);

    void count(SameDayConstriction sameDayConstriction);
}
