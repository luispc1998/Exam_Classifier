package domain.constrictions.types.hardConstriction.hardifiedConstrictions;

import domain.constrictions.types.hardConstriction.AbstractHardConstriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.DayBannedConstriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.DifferentDayConstriction;

/**
 * Hard version of {@link DifferentDayConstriction}.
 *
 * @see DifferentDayConstriction
 */
public class DifferentDayHardifiedConstriction extends AbstractHardConstriction {

    /**
     * {@code DifferentDayConstriction} instance to get the constriction logic by composition.
     */
    private DifferentDayConstriction differentDayConstriction;

    /**
     * Default constructor for the class.
     * @param differentDayConstriction Object from which the data of the constriction will be obtained by composition.
     */
    public DifferentDayHardifiedConstriction(DifferentDayConstriction differentDayConstriction) {
        this.differentDayConstriction = differentDayConstriction;
    }

    @Override
    public String getConstrictionID() {
        return differentDayConstriction.getConstrictionID();
    }

    @Override
    public boolean isFulfilled() {
        return differentDayConstriction.isFulfilled();
    }
}
