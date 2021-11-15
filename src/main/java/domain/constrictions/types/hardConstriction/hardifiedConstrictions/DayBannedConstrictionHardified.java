package domain.constrictions.types.hardConstriction.hardifiedConstrictions;

import domain.constrictions.types.hardConstriction.AbstractHardConstriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.DayBannedConstriction;

public class DayBannedConstrictionHardified extends AbstractHardConstriction {

    private DayBannedConstriction dayBannedConstriction;

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
