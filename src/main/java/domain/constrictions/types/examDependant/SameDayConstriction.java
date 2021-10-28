package domain.constrictions.types.examDependant;


import domain.constrictions.Constriction;
import domain.constrictions.counter.ConstrictionCounter;
import domain.constrictions.types.AbstractConstriction;
import domain.entities.Exam;

import java.util.ArrayList;
import java.util.List;

/**
 * This will represent for a list of exams, that they must take place on the same day.
 */
public class SameDayConstriction extends AbstractConstriction {

    public final static String CONSTRICTION_ID= "SD";

    List<Exam> exams;

    public SameDayConstriction(List<Exam> exams) {
        this.exams = new ArrayList<>(exams);
    }



    @Override
    public boolean isFulfilled(ConstrictionCounter counter) {
        for (Exam exam: exams) {
            if (exam.getDate() == null) {
                setLastEvaluation(false);
                return false;
            }
            if (! exam.getDate().atStartOfDay().equals(exams.get(0).getDate().atStartOfDay())){
                counter.count(this);
                setLastEvaluation(false);
                return false; // If multiple counts would be desired this would be at the end;
            }
        }
        setLastEvaluation(true);
        return true;
    }


}