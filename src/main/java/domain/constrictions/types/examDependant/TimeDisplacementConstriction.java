package domain.constrictions.types.examDependant;


import configuration.DateTimeConfigurer;
import domain.constrictions.Constriction;
import domain.constrictions.counter.ConstrictionCounter;
import domain.constrictions.types.AbstractConstriction;
import domain.entities.Exam;

import java.time.Duration;
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
public class TimeDisplacementConstriction extends AbstractConstriction {

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

    // TODO change behaviour to use the calendar positions.
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


    @Override
    public boolean isFulfilled(ConstrictionCounter counter) {
        if (first.getDate() ==null || second.getDate() ==null) {
            setLastEvaluation(false);
            return false;
        }

        LocalDate limitDate = first.getDate().plusDays(distanceInDays);

        if(limitDate.isBefore(second.getDate()) || limitDate.equals(second.getDate())){
            counter.count(this);
            setLastEvaluation(false);
            return false;
        }
        setLastEvaluation(true);
        return true;

/* Old implementation
        long hi =  Duration.between(first.getDate().atStartOfDay(), second.getDate().atStartOfDay()).toDays();

        if (hi < distanceInDays) {
            counter.count(this);
            return false;
        }
        return true;

 */


    }


}
