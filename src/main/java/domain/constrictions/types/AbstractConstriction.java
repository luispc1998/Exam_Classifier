package domain.constrictions.types;

import domain.constrictions.Constriction;

public abstract class AbstractConstriction implements Constriction {

    private boolean lastEvaluation;

    protected void setLastEvaluation(boolean value) {
        lastEvaluation = value;
    }

    @Override
    public boolean getLastEvaluation() {
        return false;
    }
}
