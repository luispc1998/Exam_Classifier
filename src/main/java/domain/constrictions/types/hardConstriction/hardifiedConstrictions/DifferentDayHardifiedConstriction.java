package domain.constrictions.types.hardConstriction.hardifiedConstrictions;

import domain.constrictions.types.hardConstriction.AbstractHardConstriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.DifferentDayConstriction;

public class DifferentDayHardifiedConstriction extends AbstractHardConstriction {

    private DifferentDayConstriction differentDayConstriction;

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
