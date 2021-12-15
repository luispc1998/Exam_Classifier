package domain.constrictions.types.weakConstriction.hardifiableConstrictions;

import domain.constrictions.counter.ConstrictionCounter;
import domain.constrictions.types.hardConstriction.HardConstriction;
import domain.constrictions.types.hardConstriction.hardifiedConstrictions.HardifiedConstriction;
import domain.entities.Exam;

/**
 * This states for two exams that one of them must be after the other.
 */
public class OrderExamsConstriction extends AbstractUserConstriction {

    /**
     * Constriction with the identifier for this type of {@link domain.constrictions.Constriction}.
     */
    public final static String CONSTRICTION_ID = "OE";

    /**
     * {@link Exam} that must take place before than {@code second}
     */
    private final Exam first;

    /**
     * {@link Exam} that must take place after than {@code first}
     */
    private final Exam second;

    /**
     * Constructor of the class.
     * @param first the {@link Exam} that must be first.
     * @param second the {@link Exam} that must be second.
     */
    public OrderExamsConstriction(Exam first, Exam second){
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean isFulfilled() {
        if (first.getDate() ==null || second.getDate() ==null) {
            setLastEvaluation(true);
            return true;
        }

        return second.getDate().isAfter(first.getDate());
    }

    @Override
    public String getConstrictionID() {
        return CONSTRICTION_ID;
    }

    /**
     * Returns the exam that must take place first.
     * @return The exam that must take place first.
     */
    public Exam getFirst() {
        return first;
    }

    /**
     * Returns the exam that must take place after {@code first}.
     * @return The exam that must take place after {@code first}.
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
