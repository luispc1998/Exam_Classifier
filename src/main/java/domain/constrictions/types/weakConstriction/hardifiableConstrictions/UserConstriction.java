package domain.constrictions.types.weakConstriction.hardifiableConstrictions;

import domain.constrictions.types.hardConstriction.HardConstriction;
import domain.constrictions.types.weakConstriction.WeakConstriction;
import domain.entities.Exam;

import java.time.LocalDate;
import java.util.Set;

/**
 * User constrictions that can be specified as hard or weak.
 */
public interface UserConstriction extends WeakConstriction {

    /**
     * Makes the constriction hard.
     *
     * <p>
     * This process implies to add it to the related exams, so that the {@link fitnessFunctions.greedyAlgorithm.ChromosomeDecoder},
     * can take them into account while decoding.
     */
    void hardify();

    /**
     * Returns the last result of {@code isFulfilled}.
     * @return the last evaluation of {@code isFulfilled}.
     */
    boolean getLastEvaluation();



}
