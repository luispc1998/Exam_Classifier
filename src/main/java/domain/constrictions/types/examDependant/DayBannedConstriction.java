package domain.constrictions.types.examDependant;


import domain.constrictions.Constriction;
import domain.constrictions.counter.ConstrictionCounter;
import domain.constrictions.types.AbstractConstriction;
import domain.entities.Exam;

import java.time.LocalDate;

/**
 * This will represent for an exam a list of days in which it cannot be placed
 */
public class DayBannedConstriction extends AbstractConstriction {

    public final static String CONSTRICTION_ID = "DB";

    private LocalDate dayBanned;
    private Exam exam;


    public DayBannedConstriction(Exam exam, LocalDate dayBanned) {
        this.dayBanned = dayBanned;
        this.exam = exam;
    }

    @Override
    public boolean isFulfilled(ConstrictionCounter counter) {
        if (exam.getDate() ==null){
            setLastEvaluation(false);
            return false;
        }
        if (dayBanned.atStartOfDay().equals(exam.getDate().atStartOfDay())){
            counter.count(this);
            setLastEvaluation(false);
            return false;
        }
        setLastEvaluation(true);
        return true;
    }


}
