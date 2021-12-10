package domain.constrictions.types.hardConstriction.hardifiedConstrictions;

import domain.constrictions.types.hardConstriction.AbstractHardConstriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.DayBannedConstriction;

/**
 * Hard version of {@link DayBannedConstriction}.
 *
 * @see DayBannedConstriction
 */
public class DayBannedConstrictionHardified extends AbstractHardConstriction {

    /**
     * {@code DayBannedConstriction} instance to get the constriction logic by composition.
     */
    private DayBannedConstriction dayBannedConstriction;

    /**
     * Default constructor for the class.
     * @param dayBannedConstriction Object from which the data of the constriction will be obtained by composition.
     */
    public DayBannedConstrictionHardified(DayBannedConstriction dayBannedConstriction) {
        this.dayBannedConstriction = dayBannedConstriction;
    }


    @Override
    public String getConstrictionID() {
        return dayBannedConstriction.getConstrictionID();
    }

    @Override
    public boolean isFulfilled() {
        return dayBannedConstriction.isFulfilled();
    }

}
