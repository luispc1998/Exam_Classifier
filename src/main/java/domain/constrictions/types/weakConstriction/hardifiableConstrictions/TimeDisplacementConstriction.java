package domain.constrictions.types.weakConstriction.hardifiableConstrictions;


import configuration.DateTimeConfigurer;
import domain.constrictions.counter.ConstrictionCounter;
import domain.constrictions.types.hardConstriction.hardifiedConstrictions.TimeDisplacementConstrictionHardified;
import domain.entities.Exam;

import java.time.LocalDate;
import java.util.List;

/**
 * This class represents the constriction by which two exams must have at least X number of days between their dates,
 * being X a natural number.
 *
 * <p>
 * Note that X will be a number of positions in the calendar of {@link LocalDate} in {@link configuration.DateTimeConfigurer}.
 *
 * @see DateTimeConfigurer#getExamDates()
 */
public class TimeDisplacementConstriction extends AbstractUserConstriction {

    /**
     * Constriction with the identifier for this type of {@link domain.constrictions.Constriction}.
     */
    public final static String CONSTRICTION_ID = "TD";

    /**
     * {@link Exam} that must take place before than {@code second}
     */
    private Exam first;

    /**
     * {@link Exam} that must take place after than {@code first}
     */
    private Exam second;

    /**
     * Days that must be between {@code first} and {@code second}.
     */
    private long distanceInDays;

    /**
     * Calendar of available days for the {@code Exam} objects.
     */
    private List<LocalDate> calendar;

    /**
     * Constructor for the class
     * @param first {@link Exam} that must take place before than {@code second}
     * @param second {@link Exam} that must take place after than {@code first}
     * @param distanceInDays Days that must be between {@code first} and {@code second}.
     * @param calendar The calendar of possible dates. It is assumed that it is sorted.
     */
    public TimeDisplacementConstriction(Exam first, Exam second, long distanceInDays, List<LocalDate> calendar) {
        this.first = first;
        this.second = second;
        this.distanceInDays = distanceInDays;
        this.calendar = calendar;
    }

    public static void setClassDescription(String stringCellValue) {

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
    public void countMe(ConstrictionCounter counter) {
        counter.count(this);
    }

    @Override
    public String getConstrictionID() {
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
    public void hardify() {
        TimeDisplacementConstrictionHardified tdhConstriction =
                new TimeDisplacementConstrictionHardified(this);

        first.addHardConstriction(tdhConstriction);
        second.addHardConstriction(tdhConstriction);
    }
}
