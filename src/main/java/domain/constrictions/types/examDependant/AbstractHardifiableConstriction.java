package domain.constrictions.types.examDependant;

import domain.constrictions.counter.ConstrictionCounterImpl;
import domain.constrictions.types.AbstractConstriction;
import domain.entities.Exam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractHardifiableConstriction extends AbstractConstriction
                                                        implements HardifiableConstriction {

    @Override
    public Set<LocalDate> filterViableDays(Set<LocalDate> days, Exam examToCheck) {
        Set<LocalDate> validDates = new HashSet<>();
        for (LocalDate day: days) {
            LocalDate prevDate = examToCheck.getDate();
            examToCheck.setDate(day);
            if (isFulfilled(new ConstrictionCounterImpl())){
                validDates.add(day);
            }
            examToCheck.setDate(prevDate);
        }
        return validDates;
    }

}
