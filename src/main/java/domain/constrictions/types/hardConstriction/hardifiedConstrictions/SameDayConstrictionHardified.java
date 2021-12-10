package domain.constrictions.types.hardConstriction.hardifiedConstrictions;

import domain.constrictions.types.hardConstriction.AbstractHardConstriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.DifferentDayConstriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.SameDayConstriction;

/**
 * Hard version of {@link SameDayConstriction}.
 *
 * @see SameDayConstriction
 */
public class SameDayConstrictionHardified extends AbstractHardConstriction {

    /**
     * {@code SameDayConstriction} instance to get the constriction logic by composition.
     */
    private SameDayConstriction sameDayConstriction;

    /**
     * Default constructor for the class.
     * @param sameDayConstriction Object from which the data of the constriction will be obtained by composition.
     */
    public SameDayConstrictionHardified(SameDayConstriction sameDayConstriction) {
        this.sameDayConstriction = sameDayConstriction;
    }

    @Override
    public String getConstrictionID() {
        return sameDayConstriction.getConstrictionID();
    }

    @Override
    public boolean isFulfilled() {
        return sameDayConstriction.isFulfilled();
    }
}
