package domain.constrictions;

import domain.constrictions.counter.ConstrictionCounter;

public interface Constriction {


    boolean isFulfilled(ConstrictionCounter counter);


}
