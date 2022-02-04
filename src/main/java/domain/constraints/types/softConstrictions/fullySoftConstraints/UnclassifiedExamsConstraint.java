package domain.constraints.types.softConstrictions.fullySoftConstraints;

import domain.constraints.Constraint;
import domain.constraints.counter.ConstrictionCounter;
import domain.constraints.types.softConstrictions.WeakConstraint;
import domain.entities.Exam;

import java.util.List;

/**
 * This {@link Constraint} penalizes schedules by the number of unclassified {@link Exam}.
 */
public class UnclassifiedExamsConstraint implements WeakConstraint {

    /**
     * Constriction with the identifier for this type of {@link Constraint}.
     */
    public final static String CONSTRICTION_ID = "UE";

    /**
     * List of {@link Exam} to check the schedule.
     */
    private final List<Exam> exams;


    /**
     * Constructor for the class
     * @param exams List of {@link Exam} to check the schedule.
     */
    public UnclassifiedExamsConstraint(List<Exam> exams) {
        this.exams = exams;
    }

    @Override
    public void checkConstriction(ConstrictionCounter counter) {
        for (Exam exam: exams) {
            if (! exam.isScheduled()){
                counter.count(this);
            }
        }

    }

    @Override
    public String getConstrictionID() {
        return CONSTRICTION_ID;
    }
}
