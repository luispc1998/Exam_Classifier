package domain.constrictions.types.hardConstriction.hardifiedConstrictions;

import domain.constrictions.types.hardConstriction.AbstractHardConstriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.UserConstriction;

public class HardifiedConstriction extends AbstractHardConstriction {

    /**
     * {@code UserConstriction} instance to get the constriction logic by composition.
     */
    private final UserConstriction userConstriction;

    /**
     * Default constructor for the class.
     * @param userConstriction Object from which the data of the constriction will be obtained by composition.
     */
    public HardifiedConstriction(UserConstriction userConstriction) {
        this.userConstriction = userConstriction;
    }

    @Override
    public String getConstrictionID() {
        return userConstriction.getConstrictionID();
    }

    @Override
    public boolean isFulfilled() {
        return userConstriction.isFulfilled();
    }
}
