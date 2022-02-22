package domain.constraints.types.softConstraints.fullySoftConstraints;

import domain.constraints.counter.ConstraintCounter;
import domain.constraints.types.softConstraints.SoftConstraint;
import domain.entities.Exam;

import java.util.List;

/**
 * This {SoftConstraint} penalizes schedules by the number of unclassified {@link Exam}.
 */
public class UnscheduledExamsConstraint implements SoftConstraint {

    /**
     * Constraint with the identifier for this type of constraint.
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
    public UnscheduledExamsConstraint(List<Exam> exams) {
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
