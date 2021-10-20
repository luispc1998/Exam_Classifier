package domain.constrictions.types;


import domain.constrictions.Constriction;
import domain.constrictions.counter.ConstrictionCounter;
import domain.entities.Exam;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

/**
 * This class represents the constriction by which two exams must have at least X number of days between their dates,
 * being X a natural number.
 */
public class TimeDisplacementConstriction implements Constriction {

    public final static String CONSTRICTION_ID = "TD";

    private Exam first;
    private Exam second;
    private long distanceInDays;
    private List<LocalDate> calendar;

    public TimeDisplacementConstriction(Exam first, Exam second, long distanceInDays, List<LocalDate> calendar) {
        this.first = first;
        this.second = second;
        this.distanceInDays = distanceInDays;
        this.calendar = calendar;
    }


    @Override
    public boolean isFulfilled(ConstrictionCounter counter) {
        if (first.getDate() ==null || second.getDate() ==null) {
            return false;
        }

        LocalDate limitDate = first.getDate().plusDays(distanceInDays);

        if(limitDate.isBefore(second.getDate()) || limitDate.equals(second.getDate())){
            counter.count(this);
            return false;
        }
        return true;

/* Old implementation
        long hi =  Duration.between(first.getDate().atStartOfDay(), second.getDate().atStartOfDay()).toDays();

        if (hi < distanceInDays) {
            counter.count(this);
            return false;
        }
        return true;

 */


    }


}
