package domain.constrictions.types.hardConstriction.hardifiedConstrictions;

import domain.constrictions.types.hardConstriction.AbstractHardConstriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.DifferentDayConstriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.TimeDisplacementConstriction;

/**
 * Hard version of {@link TimeDisplacementConstriction}.
 *
 * @see TimeDisplacementConstriction
 */
public class TimeDisplacementConstrictionHardified extends AbstractHardConstriction {


    /**
     * {@code TimeDisplacementConstriction} instance to get the constriction logic by composition.
     */
    private TimeDisplacementConstriction timeDisplacementConstriction;

    /**
     * Default constructor for the class.
     * @param timeDisplacementConstriction Object from which the data of the constriction will be obtained by composition.
     */
    public TimeDisplacementConstrictionHardified(TimeDisplacementConstriction timeDisplacementConstriction) {
        this.timeDisplacementConstriction = timeDisplacementConstriction;
    }

    @Override
    public String getConstrictionID() {
        return timeDisplacementConstriction.getConstrictionID();
    }

    @Override
    public boolean isFulfilled() {
        return timeDisplacementConstriction.isFulfilled();
    }
}
