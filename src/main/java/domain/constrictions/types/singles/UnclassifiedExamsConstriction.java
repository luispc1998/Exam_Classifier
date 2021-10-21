package domain.constrictions.types.singles;

import domain.constrictions.Constriction;
import domain.constrictions.counter.ConstrictionCounter;
import domain.entities.Exam;

import java.util.List;

public class UnclassifiedExamsConstriction implements Constriction {

    public final static String CONSTRICTION_ID = "UE";

    private List<Exam> exams;


    public UnclassifiedExamsConstriction(List<Exam> exams) {
        this.exams = exams;
    }

    @Override
    public boolean isFulfilled(ConstrictionCounter counter) {
        for (Exam exam: exams) {
            if (! exam.isScheduled()){
                counter.count(this);
            }
        }
        return false;
    }
}