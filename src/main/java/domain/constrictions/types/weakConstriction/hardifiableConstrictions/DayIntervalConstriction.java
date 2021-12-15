package domain.constrictions.types.weakConstriction.hardifiableConstrictions;

import domain.constrictions.counter.ConstrictionCounter;
import domain.constrictions.types.hardConstriction.HardConstriction;
import domain.constrictions.types.hardConstriction.hardifiedConstrictions.HardifiedConstriction;
import domain.entities.Exam;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class DayIntervalConstriction extends AbstractUserConstriction {

    /**
     * Constriction with the identifier for this type of {@link domain.constrictions.Constriction}.
     */
    public final static String CONSTRICTION_ID = "DI";

    /**
     * The dates in which {@code exam} can take place
     */
    private List<LocalDate> validDates;

    /**
     * {@link Exam} that should take place on {@code validDates}
     */
    private Exam exam;

    /**
     * First date of the interval in which the exam can take place.
     *
     * <p>
     * This attribute keeps the value until results are written.
     */
    private LocalDate intervalStart;

    /**
     * Last date of the interval in which the exam can take place.
     *
     * <p>
     * This attribute keeps the value until results are written.
     */
    private LocalDate intervalEnd;

    /**
     * Constructor for the class.
     * @param exam The exam over which the restriction is imposed.
     * @param intervalStart First date of the interval in which the exam can take place.
     * @param intervalEnd Last date of the interval in which the exam can take place.
     * @param calendar Calendar of available dates to place exams.
     */
    public DayIntervalConstriction(Exam exam, LocalDate intervalStart,
                                   LocalDate intervalEnd, List<LocalDate> calendar) {

        if (intervalEnd.isBefore(intervalStart)){
            throw new IllegalArgumentException("Day interval constriction for exam: " + exam.getId() +
                    " found interval ending date to be before than the starting date");
        }

        this.exam = exam;
        this.intervalStart = intervalStart;
        this.intervalEnd = intervalEnd;
        validDates = calendar.stream().filter((d) -> containedOnInterval(intervalStart, intervalEnd, d))
                .collect(Collectors.toList());
    }

    public boolean containedOnInterval(LocalDate intervalStart, LocalDate intervalEnd, LocalDate testingDate) {
        return (testingDate.isAfter(intervalStart) && testingDate.isBefore(intervalEnd))
                || testingDate.isEqual(intervalStart)
                || testingDate.isEqual(intervalEnd);
    }

    @Override
    public String getConstrictionID() {
        return CONSTRICTION_ID;
    }

    @Override
    public boolean isFulfilled() {
        return validDates.contains(exam.getDate());
    }

    @Override
    public void countMe(ConstrictionCounter counter) {
        counter.count(this);
    }

    /**
     * Returns the {@code Exam} that has the constriction.
     * @return The {@code Exam} that has the constriction.
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
    public void hardify() {
        HardConstriction hConstriction = new HardifiedConstriction(this);
        exam.addHardConstriction(hConstriction);
    }
}
