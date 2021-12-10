package domain.constrictions.types.hardConstriction.hardifiedConstrictions;

import domain.constrictions.types.hardConstriction.AbstractHardConstriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.DifferentDayConstriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.OrderExamsConstriction;

/**
 * Hard version of {@link OrderExamsConstriction}.
 *
 * @see OrderExamsConstriction
 */
public class OrderExamsHardifiedConstriction extends AbstractHardConstriction {

    /**
     * {@code OrderExamsConstriction} instance to get the constriction logic by composition.
     */
    private OrderExamsConstriction orderExamsConstriction;

    /**
     * Default constructor for the class.
     * @param orderExamsConstriction Object from which the data of the constriction will be obtained by composition.
     */
    public OrderExamsHardifiedConstriction(OrderExamsConstriction orderExamsConstriction) {
        this.orderExamsConstriction = orderExamsConstriction;
    }

    @Override
    public String getConstrictionID() {
        return orderExamsConstriction.getConstrictionID();
    }

    @Override
    public boolean isFulfilled() {
        return orderExamsConstriction.isFulfilled();
    }
}
