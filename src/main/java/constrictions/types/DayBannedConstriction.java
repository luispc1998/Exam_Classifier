package constrictions.types;


import constrictions.Constriction;
import constrictions.counter.ConstrictionCounter;
import domain.entities.Exam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * This will represent for an exam a list of days in which it cannot be placed
 */
public class DayBannedConstriction implements Constriction {

    private LocalDate dayBanned;
    private Exam exam;


    public DayBannedConstriction(Exam exam, LocalDate dayBanned) {
        this.dayBanned = dayBanned;
        this.exam = exam;
    }

    @Override
    public boolean isFulfilled(ConstrictionCounter counter) {

        if (dayBanned.atStartOfDay().equals(exam.getDate().atStartOfDay())){
            counter.count(this);
            return false;
        }
        return true;
    }
}
