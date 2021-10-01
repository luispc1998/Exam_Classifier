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
public class DaysBannedConstriction implements Constriction {

    private List<LocalDate> daysBanned;
    private Exam exam;


    public DaysBannedConstriction(Exam exam, List<LocalDate> daysBanned) {
        this.daysBanned = new ArrayList<>(daysBanned);
        this.exam = exam;
    }

    @Override
    public boolean isFulfilled(ConstrictionCounter counter) {

        if (daysBanned.contains(exam.getDate())){
            counter.count(this);
            return false;
        }
        return true;
    }
}
