package domain.constrictions.types.examDependant;

import domain.constrictions.Constriction;
import domain.entities.Exam;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * User constrictions that can be specified as hard or weak.
 */
public interface HardifiableConstriction extends Constriction {

    /**
     * Makes the constriction hard.
     *
     * <p>
     * This process implies to add it to the related exams, so that the {@link fitnessFunctions.greedyAlgorithm.ChromosomeDecoder},
     * can take them into account while decoding.
     */
    void hardify();

    /**
     * Filters a list of days. Returning another list with days that meet the constriction.
     * @param days The list of possible days that we want to check.
     * @param examToCheck The exam that will be currently in process of scheduling.
     * @return A list of viable days for the constrictions.
     */
    Set<LocalDate> filterViableDays(Set<LocalDate> days, Exam examToCheck);

}
