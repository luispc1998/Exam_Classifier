package domain.constraints.types.hardConstraints.hardUserConstrictions;

import domain.constraints.types.hardConstraints.AbstractHardConstraint;
import domain.constraints.types.softConstrictions.userConstraints.UserConstraint;

public class HardifiedConstraint extends AbstractHardConstraint {

    /**
     * {@code UserConstriction} instance to get the constriction logic by composition.
     */
    private final UserConstraint userConstriction;

    /**
     * Default constructor for the class.
     * @param userConstriction Object from which the data of the constriction will be obtained by composition.
     */
    public HardifiedConstraint(UserConstraint userConstriction) {
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
