package domain.constraints.types.softConstraints.userConstraints;

import domain.constraints.types.hardConstraints.HardConstraint;
import domain.constraints.types.softConstraints.SoftConstraint;
import greedyAlgorithm.ChromosomeDecoder;

/**
 * User constraints that can be specified as hard or weak.
 */
public interface UserConstraint extends SoftConstraint {

    /**
     * Makes the constraint hard.
     *
     * <p>
     * This process implies the generation of a new instance of {@link HardConstraint},
     * and add it to the related exam or exams, so that the {@link ChromosomeDecoder}
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

    /**
     * This method will contain the constraint logic.
     *
     * <p>
     * This method must never update any state, just provide the result.
     * @return True if the constraint was fulfilled, False otherwise.
     */
    boolean isFulfilled();

    /**
     * Indicates whether the constraint was marked as hard or not.
     * @return True if marked as hard, false otherwise.
     */
    boolean wasHardified();



}
