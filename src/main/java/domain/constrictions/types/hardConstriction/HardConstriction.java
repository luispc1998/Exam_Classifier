package domain.constrictions.types.hardConstriction;

import domain.constrictions.Constriction;
import domain.entities.Exam;

import java.time.LocalDate;
import java.util.Set;

public interface HardConstriction extends Constriction {

    /**
     * Filters a list of days. Returning another list with days that meet the constriction.
     * @param days The list of possible days that we want to check.
     * @param examToCheck The exam that will be currently in process of scheduling.
     * @return A list of viable days for the constrictions.
     */
    Set<LocalDate> filterViableDays(Set<LocalDate> days, Exam examToCheck);


}
