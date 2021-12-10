package domain.constrictions.types.hardConstriction;

import domain.entities.Exam;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * This provides a default implementation of the {@code filterViableDays} days method, allowing the inheritors to just
 * implement the necessary constriction logic.
 */
public abstract class AbstractHardConstriction implements HardConstriction {

    @Override
    public Set<LocalDate> filterViableDays(Set<LocalDate> days, Exam examToCheck) {
        Set<LocalDate> validDates = new HashSet<>();
        for (LocalDate day: days) {
            LocalDate prevDate = examToCheck.getDate();
            examToCheck.setDate(day);
            if (isFulfilled()){
                validDates.add(day);
            }
            examToCheck.setDate(prevDate);
        }
        return validDates;
    }

    /**
     * States whether a Constriction is fulfilled or not.
     * @return True in case it is fulfilled, False otherwise.
     */
    public abstract boolean isFulfilled();

}
