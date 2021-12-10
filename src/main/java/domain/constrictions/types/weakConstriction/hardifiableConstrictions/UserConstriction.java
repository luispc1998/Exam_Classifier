package domain.constrictions.types.weakConstriction.hardifiableConstrictions;

import domain.constrictions.types.weakConstriction.WeakConstriction;

/**
 * User constrictions that can be specified as hard or weak.
 */
public interface UserConstriction extends WeakConstriction {

    /**
     * Makes the constriction hard.
     *
     * <p>
     * This process implies the generation of a new instance of {@link domain.constrictions.types.hardConstriction.HardConstriction},
     * and add it to the related exam or exams, so that the {@link fitnessFunctions.greedyAlgorithm.ChromosomeDecoder}
     * can take them into account while decoding.
     *
     * <p>
     * The creation of the new object is done by composition so the current calling object will be passed as a parameter
     * to the new object.
     */
    void hardify();

    /**
     * Returns the last result of {@code isFulfilled}.
     * @return the last evaluation of {@code isFulfilled}.
     */
    boolean getLastEvaluation();



}
