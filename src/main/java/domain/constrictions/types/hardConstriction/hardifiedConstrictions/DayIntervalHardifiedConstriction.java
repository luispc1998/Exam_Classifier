package domain.constrictions.types.hardConstriction.hardifiedConstrictions;

import domain.constrictions.types.hardConstriction.AbstractHardConstriction;
import domain.constrictions.types.hardConstriction.HardConstriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.DayBannedConstriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.DayIntervalConstriction;
import domain.entities.Exam;

import java.time.LocalDate;
import java.util.Set;

/**
 * Hard version of {@link DayIntervalConstriction}.
 *
 * @see DayIntervalConstriction
 */
public class DayIntervalHardifiedConstriction extends AbstractHardConstriction {

    /**
     * {@code dayIntervalConstriction} instance to get the constriction logic by composition.
     */
    private DayIntervalConstriction dayIntervalConstriction;

    /**
     * Default constructor for the class.
     * @param dayIntervalConstriction Object from which the data of the constriction will be obtained by composition.
     */
    public DayIntervalHardifiedConstriction(DayIntervalConstriction dayIntervalConstriction) {
        this.dayIntervalConstriction = dayIntervalConstriction;
    }


    @Override
    public String getConstrictionID() {
        return dayIntervalConstriction.getConstrictionID();
    }

    @Override
    public boolean isFulfilled() {
        return dayIntervalConstriction.isFulfilled();
    }


}
