package domain.constraints.types.softConstraints.userConstraints;


import domain.constraints.Constraint;
import domain.constraints.counter.ConstraintCounter;
import domain.constraints.types.hardConstraints.HardConstraint;
import domain.constraints.types.hardConstraints.hardUserConstraints.HardifiedConstraint;
import domain.entities.Exam;
import geneticAlgorithm.configuration.DateTimeConfigurer;

import java.time.LocalDate;
import java.util.List;

/**
 * This class represents the constraint by which two exams must have at least X number of days between their dates,
 * being X a natural number.
 *
 * <p>
 * Note that X will be a number of positions in the calendar of {@link LocalDate} in {@link geneticAlgorithm.configuration.DateTimeConfigurer}.
 *
 * @see DateTimeConfigurer#getExamDates()
 */
public class TimeDisplacementConstraint extends AbstractUserConstraint {

    /**
     * Constraint with the identifier for this type of {@link Constraint}.
     */
    public final static String CONSTRICTION_ID = "TD";

    /**
     * {@link Exam} that must take place before than {@code second}
     */
    private final Exam first;

    /**
     * {@link Exam} that must take place after than {@code first}
     */
    private final Exam second;

    /**
     * Days that must be between {@code first} and {@code second}.
     */
    private final long distanceInDays;

    /**
     * Calendar of available days for the {@code Exam} objects.
     */
    private final List<LocalDate> calendar;

    /**
     * Constructor for the class
     * @param first {@link Exam} that must take place before than {@code second}
     * @param second {@link Exam} that must take place after than {@code first}
     * @param distanceInDays Days that must be between {@code first} and {@code second}.
     * @param calendar The calendar of possible dates. It is assumed that it is sorted.
     */
    public TimeDisplacementConstraint(Exam first, Exam second, long distanceInDays, List<LocalDate> calendar) {
        this.first = first;
        this.second = second;
        this.distanceInDays = distanceInDays;
        this.calendar = calendar;
    }


    public boolean isFulfilled() {
        // Case that this is hard. The restriction is fulfilled if one of the exams is not placed.
        if (first.getDate() ==null || second.getDate() ==null) {
            setLastEvaluation(true);
            return true;
        }

        int index = Math.abs(calendar.indexOf(second.getDate()) - calendar.indexOf(first.getDate()));

        return index >= distanceInDays;
    }

    @Override
    public void countMe(ConstraintCounter counter) {
        counter.count(this);
    }

    @Override
    public String getConstraintID() {
        return CONSTRICTION_ID;
    }

    /**
     * Returns the exam that must take place first.
     * @return The exam that must take place first.
     */
    public Exam getFirst() {
        return first;
    }

    /**
     * Returns the exam that must take place after {@code first}.
     * @return The exam that must take place after {@code first}.
     */
    public Exam getSecond() {
        return second;
    }

    /**
     * Distance in calendar of {@code second} with respect to {@code first}.
     * @return Distance in calendar of {@code second} with respect to {@code first}.
     */
    public long getDistanceInDays() {
        return distanceInDays;
    }

    @Override
    public void specificHardify() {
        HardConstraint hConstraint = new HardifiedConstraint(this);
        first.addHardConstraint(hConstraint);
        second.addHardConstraint(hConstraint);
    }
}
