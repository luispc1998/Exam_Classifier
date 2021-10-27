package domain.constrictions.types.examDependant;

import domain.constrictions.Constriction;
import domain.constrictions.counter.ConstrictionCounter;
import domain.constrictions.types.AbstractConstriction;
import domain.entities.Exam;

public class DifferentDayConstriction extends AbstractConstriction {

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
            setLastEvaluation(false);
            return false;
        }
        setLastEvaluation(true);
        return true;
    }
}
