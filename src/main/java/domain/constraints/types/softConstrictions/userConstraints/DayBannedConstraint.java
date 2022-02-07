package domain.constraints.types.softConstrictions.userConstraints;

import domain.constraints.Constraint;
import domain.constraints.counter.ConstraintCounter;
import domain.constraints.types.hardConstraints.HardConstraint;
import domain.constraints.types.hardConstraints.hardUserConstrictions.HardifiedConstraint;
import domain.entities.Exam;

import java.time.LocalDate;

/**
 * This states for an exam a date in which it cannot be placed.
 */
public class DayBannedConstraint extends AbstractUserConstraint {

    /**
     * Constriction with the identifier for this type of {@link Constraint}.
     */
    public final static String CONSTRICTION_ID = "DB";

    /**
     * The date in which {@code exam} cannot take place.
     */
    private final LocalDate dayBanned;

    /**
     * {@link Exam} that cannot take place on {@code dayBanned}.
     */
    private final Exam exam;


    /**
     * Cosntructor for the class
     * @param exam the exam that would no be able to take place on {@code dayBanned}
     * @param dayBanned the date in which {@code exam} cannot take place.
     */
    public DayBannedConstraint(Exam exam, LocalDate dayBanned) {
        this.dayBanned = dayBanned;
        this.exam = exam;
    }


    @Override
    public boolean isFulfilled() {
        if (exam.getDate() ==null){
            setLastEvaluation(true);
            return true;
        }
        return ! dayBanned.atStartOfDay().equals(exam.getDate().atStartOfDay());
    }

    @Override
    public void countMe(ConstraintCounter counter) {
        counter.count(this);
    }


    @Override
    public String getConstrictionID() {
        return CONSTRICTION_ID;
    }

    /**
     * Returns the {@code Exam} that has the constriction.
     * @return The {@code Exam} that has the constriction.
     */
    public Exam getExam() {
        return exam;
    }

    /**
     * Returns the day in  which the exam cannot take place.
     * @return The day in which {@code exam} cannot take place.
     */
    public LocalDate getDayBanned() {
        return dayBanned;
    }

    @Override
    public void specificHardify() {
        HardConstraint hConstriction = new HardifiedConstraint(this);
        exam.addHardConstriction(hConstriction);
    }

}
