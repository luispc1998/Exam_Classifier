package domain.constrictions.types.hardConstriction.hardifiedConstrictions;

import domain.constrictions.types.hardConstriction.AbstractHardConstriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.OrderExamsConstriction;

public class OrderExamsHardifiedConstriction extends AbstractHardConstriction {

    private OrderExamsConstriction orderExamsConstriction;

    public OrderExamsHardifiedConstriction(OrderExamsConstriction orderExamsConstriction) {
        super();
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
