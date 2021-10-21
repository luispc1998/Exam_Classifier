package domain.constrictions.types;

import domain.constrictions.Constriction;
import domain.constrictions.counter.ConstrictionCounter;
import domain.entities.Exam;

public class DifferentDayConstriction implements Constriction {

    public final static String CONSTRICTION_ID = "DD";

    private Exam first;
    private Exam second;

    public DifferentDayConstriction(Exam first, Exam second){
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean isFulfilled(ConstrictionCounter counter) {


        if (first.getDate().equals(second.getDate())) {
            counter.count(this);
            return false;
        }
        return true;
    }
}
