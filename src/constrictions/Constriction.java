package constrictions;

import constrictions.counter.ConstrictionCounter;

public interface Constriction {


    boolean isFulfilled(ConstrictionCounter counter);


}
