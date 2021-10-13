package domain.constrictions.types;


import domain.constrictions.Constriction;
import domain.constrictions.counter.ConstrictionCounter;
import domain.entities.Exam;

import java.time.LocalDate;

/**
 * This will represent for an exam a list of days in which it cannot be placed
 */
public class DayBannedConstriction implements Constriction {

    public final static String CONSTRICTION_ID = "DB";

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
