package domain.constrictions.types.hardConstriction.hardifiedConstrictions;

import domain.constrictions.types.hardConstriction.AbstractHardConstriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.TimeDisplacementConstriction;

public class TimeDisplacementConstrictionHardified extends AbstractHardConstriction {



    private TimeDisplacementConstriction timeDisplacementConstriction;

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
