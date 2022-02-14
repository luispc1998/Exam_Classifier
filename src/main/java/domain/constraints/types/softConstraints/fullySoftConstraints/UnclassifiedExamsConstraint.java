package domain.constraints.types.softConstraints.fullySoftConstraints;

import domain.constraints.Constraint;
import domain.constraints.counter.ConstraintCounter;
import domain.constraints.types.softConstraints.SoftConstraints;
import domain.entities.Exam;

import java.util.List;

/**
 * This {@link Constraint} penalizes schedules by the number of unclassified {@link Exam}.
 */
public class UnclassifiedExamsConstraint implements SoftConstraints {

    /**
     * Constraint with the identifier for this type of {@link Constraint}.
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
    public void checkConstraint(ConstraintCounter counter) {
        for (Exam exam: exams) {
            if (! exam.isScheduled()){
                counter.count(this);
            }
        }

    }

    @Override
    public String getConstraintID() {
        return CONSTRICTION_ID;
    }
}
