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

    /**
     * Constriction with the identifier for this type of {@link domain.constrictions.Constriction}.
     */
    public final static String CONSTRICTION_ID= "SD";

    /**
     * List of {@link Exam} that must take place on the same day.
     */
    List<Exam> exams;

    /**
     * Constructor for the class
     * @param exams the list of {@link Exam} that must take place on the same day.
     */
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

    @Override
    public String getConstrictionID() {
        return CONSTRICTION_ID;
    }

    /**
     * Returns {@code first}.
     *
     * <p>
     * There is no order in which the exams must be.
     * @return {@code first}.
     */
    public Exam getFirst() {
        return exams.get(0);
    }

    /**
     * Returns {@code second}.
     *
     * <p>
     * There is no order in which the exams must be.
     * @return {@code second}.
     */
    public Exam getSecond() {
        return exams.get(1);
    }
}
