package domain.constrictions.types;


import domain.constrictions.Constriction;
import domain.constrictions.counter.ConstrictionCounter;
import domain.entities.Exam;

import java.time.Duration;

/**
 * This class represents the constriction by which two exams must have at least X number of days between their dates,
 * being X a natural number.
 */
public class TimeDisplacementConstriction implements Constriction {

    public final static String CONSTRICTION_ID = "TD";

    private Exam first;
    private Exam second;
    private long distanceInDays;

    public TimeDisplacementConstriction(Exam first, Exam second, long distanceInDays) {
        this.first = first;
        this.second = second;
        this.distanceInDays = distanceInDays;
    }


    @Override
    public boolean isFulfilled(ConstrictionCounter counter) {

        long hi =  Duration.between(first.getDate().atStartOfDay(), second.getDate().atStartOfDay()).toDays();

        if (hi < distanceInDays) {
            counter.count(this);
            return false;
        }
        return true;
    }


}
