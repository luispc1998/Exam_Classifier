package domain.constrictions.counter;

import domain.constrictions.types.*;
import domain.constrictions.types.singles.UnclassifiedExamsConstriction;

public interface ConstrictionCounter {


    void count(TimeDisplacementConstriction timeDisplacementConstriction);
    void count(DayBannedConstriction daysBannedConstriction);
    void count(SameDayConstriction sameDayConstriction);
    void count(UnclassifiedExamsConstriction unclassifiedExamsConstriction);
    void count(DifferentDayConstriction differentDayConstriction);
    void count(OrderExamsConstriction orderExamsConstriction);

    int getCountOfTimeDisplacementConstriction();
    int getCountOfDaysBannedConstriction();
    int getCountOfSameDayConstriction();
    int getCountOfUnclassifiedExamsConstriction();
    int getCountOfDifferentDayConstriction();
    int getCountOrderExamsConstriction();



}
