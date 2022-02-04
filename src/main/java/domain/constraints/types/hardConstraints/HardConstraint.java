package domain.constraints.types.hardConstraints;

import domain.constraints.Constraint;
import domain.entities.Exam;

import java.time.LocalDate;
import java.util.Set;

/**
 * Type of constrictions that must be fulfilled at all cost and that could imply leaving unscheduled exams at the end of
 * the execution.
 *
 * <p>
 * This constriction instances are linked to an exam and have a direct impact in which days of the calendar the exam can
 * be placed. {@code filterViableDays} will be called for all the {@code HardConstriction} of an {@code Exam} with the set of
 * available days (initially the whole calendar). The output of the first call will be the input of the second call, and
 * so far so forth. Finally the set of days in which the hard constrictions of the exam allow it to be placed will be obtained.
 */
public interface HardConstraint extends Constraint {

    /**
     * Filters a list of days. Returning another list with days that meet the constriction.
     * @param days The list of possible days that we want to check.
     * @param examToCheck The exam that will be currently in process of scheduling.
     * @return A list of viable days for the constrictions.
     */
    Set<LocalDate> filterViableDays(Set<LocalDate> days, Exam examToCheck);


}
