package domain.constrictions.types.weakConstriction.hardifiableConstrictions;


import domain.constrictions.counter.ConstrictionCounter;
import domain.constrictions.types.hardConstriction.HardConstriction;
import domain.constrictions.types.hardConstriction.hardifiedConstrictions.HardifiedConstriction;
import domain.entities.Exam;

/**
 * This will represent for a list of exams, that they must take place on the same day.
 */
public class SameDayConstriction extends AbstractUserConstriction {

    /**
     * Constriction with the identifier for this type of {@link domain.constrictions.Constriction}.
     */
    public final static String CONSTRICTION_ID= "SD";

     /**
     * {@link Exam} that must take place on the same date as {@code second}
     */
    private final Exam first;

    /**
     * {@link Exam} that must take place on the same date as {@code first}
     */
    private final Exam second;

    /**
     * Default constructor for the class.
     * @param first one of the exams
     * @param second the other exam
     */
    public SameDayConstriction(Exam first, Exam second){
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean isFulfilled() {
        // Case that this is hard. The restriction is fulfilled if one of the exams is not placed.
        if (first.getDate() ==null || second.getDate() ==null) {
            setLastEvaluation(true);
            return true;
        }

        return first.getDate().equals(second.getDate());
    }

    @Override
    public String getConstrictionID() {
        return CONSTRICTION_ID;
    }

    /**
     * Returns {@code first}.
     *
     * <p>
     * There is no order in which the exams must be.
     * @return {@code first}.
     */
    public Exam getFirst() {
        return first;
    }

    /**
     * Returns {@code second}.
     *
     * <p>
     * There is no order in which the exams must be.
     * @return {@code second}.
     */
    public Exam getSecond() {
        return second;
    }

    @Override
    public void hardify() {
        HardConstriction hConstriction = new HardifiedConstriction(this);
        first.addHardConstriction(hConstriction);
        second.addHardConstriction(hConstriction);
    }

    @Override
    public void countMe(ConstrictionCounter counter) {
        counter.count(this);
    }

}
