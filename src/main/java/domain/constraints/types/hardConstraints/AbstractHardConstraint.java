package domain.constraints.types.hardConstraints;

import domain.entities.Exam;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * This provides a default implementation of the {@code filterViableDays} days method, allowing the inheritors to just
 * implement the necessary constraint logic.
 */
public abstract class AbstractHardConstraint implements HardConstraint {

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
     * States whether a Constraint is fulfilled or not.
     * @return True in case it is fulfilled, False otherwise.
     */
    public abstract boolean isFulfilled();

}
