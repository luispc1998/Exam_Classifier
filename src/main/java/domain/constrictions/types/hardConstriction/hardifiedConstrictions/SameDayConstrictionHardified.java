package domain.constrictions.types.hardConstriction.hardifiedConstrictions;

import domain.constrictions.types.hardConstriction.AbstractHardConstriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.SameDayConstriction;

public class SameDayConstrictionHardified extends AbstractHardConstriction {

    private SameDayConstriction sameDayConstriction;
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
