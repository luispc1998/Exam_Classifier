package domain.constraints.types.softConstraints.userConstraints;

import domain.constraints.counter.ConstraintCounter;
import domain.constraints.types.hardConstraints.HardConstraint;
import domain.constraints.types.hardConstraints.hardUserConstraints.HardifiedUserConstraint;
import domain.entities.Exam;

import java.time.LocalDate;
import java.util.List;

/**
 * This states for an exam an interval of dates in which the exam must be placed.
 */
public class DayIntervalConstraint extends AbstractUserConstraint {

    /**
     * Constraint with the identifier for this type of constraint.
     */
    public final static String CONSTRICTION_ID = "DI";

    /**
     * The dates in which {@code exam} can take place
     */
    private List<LocalDate> validDates;

    /**
     * {@link Exam} that should take place on {@code validDates}
     */
    private final Exam exam;

    /**
     * First date of the interval in which the exam can take place.
     *
     * <p>
     * This attribute keeps the value until results are written.
     */
    private final LocalDate intervalStart;

    /**
     * Last date of the interval in which the exam can take place.
     *
     * <p>
     * This attribute keeps the value until results are written.
     */
    private final LocalDate intervalEnd;

    /**
     * Constructor for the class.
     * @param exam The exam over which the restriction is imposed.
     * @param intervalStart First date of the interval in which the exam can take place.
     * @param intervalEnd Last date of the interval in which the exam can take place.
     * @param calendar Calendar of available dates to place exams.
     */
    public DayIntervalConstraint(Exam exam, LocalDate intervalStart,
                                 LocalDate intervalEnd, List<LocalDate> calendar) {

        if (intervalEnd.isBefore(intervalStart)){
            throw new IllegalArgumentException("Day interval constraint for exam: " + exam.getId() +
                    " found interval ending date to be before than the starting date");
        }

        this.exam = exam;
        this.intervalStart = intervalStart;
        this.intervalEnd = intervalEnd;
    }


    public boolean containedOnInterval(LocalDate testingDate) {
        return (testingDate.isAfter(intervalStart) && testingDate.isBefore(intervalEnd))
                || testingDate.isEqual(intervalStart)
                || testingDate.isEqual(intervalEnd);
    }

    @Override
    public String getConstraintID() {
        return CONSTRICTION_ID;
    }

    @Override
    public boolean isFulfilled() {
        if (exam.getDate() ==null){
            setLastEvaluation(true);
            return true;
        }

        return containedOnInterval(exam.getDate());
        //return validDates.contains(exam.getDate());
    }

    @Override
    public void countMe(ConstraintCounter counter) {
        counter.count(this);
    }

    /**
     * Returns the {@code Exam} that has the constraint.
     * @return The {@code Exam} that has the constraint.
     */
    public Exam getExam() {
        return exam;
    }

    /**
     * Returns the first date of the interval in which the exam can take place.
     * @return the first date of the interval in which the exam can take place.
     */
    public LocalDate getIntervalStart() {
        return intervalStart;
    }

    /**
     * Returns the last date of the interval in which the exam can take place.
     * @return the last date of the interval in which the exam can take place.
     */
    public LocalDate getIntervalEnd() {
        return intervalEnd;
    }

    @Override
    public void specificHardify() {
        HardConstraint hConstraint = new HardifiedUserConstraint(this);
        exam.addHardConstraint(hConstraint);
    }
}
