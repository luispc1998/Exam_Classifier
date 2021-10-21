package domain.constrictions.counter;

import domain.constrictions.types.DayBannedConstriction;
import domain.constrictions.types.DifferentDayConstriction;
import domain.constrictions.types.SameDayConstriction;
import domain.constrictions.types.TimeDisplacementConstriction;
import domain.constrictions.types.singles.UnclassifiedExamsConstriction;

public interface ConstrictionCounter {


    void count(TimeDisplacementConstriction timeDisplacementConstriction);
    void count(DayBannedConstriction daysBannedConstriction);
    void count(SameDayConstriction sameDayConstriction);
    void count(UnclassifiedExamsConstriction unclassifiedExamsConstriction);
    void count(DifferentDayConstriction differentDayConstriction);

    int getCountOfTimeDisplacementConstriction();
    int getCountOfDaysBannedConstriction();
    int getCountOfSameDayConstriction();
    int getCountOfUnclassifiedExamsConstriction();
    int getCountOfDifferentDayConstriction();


}
