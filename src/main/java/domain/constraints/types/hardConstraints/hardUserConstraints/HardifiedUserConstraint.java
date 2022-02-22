package domain.constraints.types.hardConstraints.hardUserConstraints;

import domain.constraints.types.hardConstraints.AbstractHardConstraint;
import domain.constraints.types.softConstraints.userConstraints.UserConstraint;

public class HardifiedUserConstraint extends AbstractHardConstraint {

    /**
     * {@code UserConstraint} instance to get the constraint logic by composition.
     */
    private final UserConstraint userConstraint;

    /**
     * Default constructor for the class.
     * @param userConstraint Object from which the data of the constraint will be obtained by composition.
     */
    public HardifiedUserConstraint(UserConstraint userConstraint) {
        this.userConstraint = userConstraint;
    }

    @Override
    public String getConstraintID() {
        return userConstraint.getConstraintID();
    }

    @Override
    public boolean isFulfilled() {
        return userConstraint.isFulfilled();
    }
}
