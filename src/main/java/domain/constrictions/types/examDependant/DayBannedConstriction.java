package domain.constrictions.types.examDependant;

import domain.constrictions.counter.ConstrictionCounter;
import domain.constrictions.types.AbstractConstriction;
import domain.entities.Exam;

import java.time.LocalDate;

/**
 * This states for an exam a date in which it cannot be placed.
 */
public class DayBannedConstriction extends AbstractConstriction {

    /**
     * Constriction with the identifier for this type of {@link domain.constrictions.Constriction}.
     */
    public final static String CONSTRICTION_ID = "DB";

    /**
     * The date in which {@code exam} cannot take place.
     */
    private LocalDate dayBanned;

    /**
     * {@link Exam} that cannot take place on {@code dayBanned}.
     */
    private Exam exam;


    /**
     * Cosntructor for the class
     * @param exam the exam that would no be able to take place on {@code dayBanned}
     * @param dayBanned the date in which {@code exam} cannot take place.
     */
    public DayBannedConstriction(Exam exam, LocalDate dayBanned) {
        this.dayBanned = dayBanned;
        this.exam = exam;
    }

    @Override
    public boolean isFulfilled(ConstrictionCounter counter) {
        if (exam.getDate() ==null){
            setLastEvaluation(false);
            return false;
        }
        if (dayBanned.atStartOfDay().equals(exam.getDate().atStartOfDay())){
            counter.count(this);
            setLastEvaluation(false);
            return false;
        }
        setLastEvaluation(true);
        return true;
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
}
