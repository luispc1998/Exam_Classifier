package domain.constraints.types.softConstraints.fullySoftConstraints;

import domain.constraints.counter.ConstraintCounter;
import domain.constraints.types.softConstraints.SoftConstraint;
import domain.entities.Exam;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This tries to spread in the calendar exams of the same complexity.
 *
 * <p>
 * Consider two exams of complexity 10, A and B. This
 * weak constraint will penalize the fact that these two exams are near in the calendar, the nearer the more penalization.
 * This makes that the algorithm spread the exams along the calendar days.
 */
public class NumericalComplexityPenalization implements SoftConstraint {

    /**
     * Constraint id of this type of {@code Constraint}.
     */
    public final static String CONSTRICTION_ID = "NCP";


    /**
     * List of {@link Exam} to check the schedule.
     */
    private final List<Exam> exams;

    /**
     * Value of the penalization.
     */
    private double accumulator;
    /**
     * Constructor for the class
     * @param exams List of {@link Exam} to check the schedule.
     */
    public NumericalComplexityPenalization(List<Exam> exams) {
        this.exams = exams;
    }

    @Override
    public String getConstraintID() {
        return CONSTRICTION_ID;
    }

    @Override
    public void checkConstraint(ConstraintCounter counter) {
        HashMap<Integer, List<Exam>> examsOrderedByComplexity = retrieveExamsByNC();

        accumulator = 0;
        for (Map.Entry<Integer, List<Exam>> entry: examsOrderedByComplexity.entrySet()) {
            List<Exam> currentList = entry.getValue();
            for (int i = 0; i < currentList.size(); i++) {
                for (int j = i; j < currentList.size(); j++) {
                    accumulator += accumulate(entry.getKey(), currentList.get(i), currentList.get(j));
                }
            }
        }

        counter.count(this);

    }

    /**
     * Returns the value of the penalization
     * @return the value of the penalization
     */
    public double getAccumulator() {
        return accumulator;
    }

    /**
     * Provides the penalization value (fitness value) corresponding to the two exams passed
     * as parameter considering the complexity.
     *
     * @param complexity Complexity of the exams.
     * @param exam First exam.
     * @param exam1 Second exam.
     * @return Value of the penalization for the fitness corresponding to the closeness of the provided exams schedule.
     */
    private double accumulate(Integer complexity, Exam exam, Exam exam1) {
        if (exam.getDate() == null || exam1.getDate() == null) {
            return 0;
        }
        double distanceDays = Duration.between(exam.getDate().atStartOfDay(), exam1.getDate().atStartOfDay()).abs().toDays();
        if (distanceDays == 0) {
            return Math.pow(complexity,2);
        }
        else{
            return complexity/distanceDays;
        }
    }

    /**
     * Classifies the exams by their numerical complexity.
     *
     * <p>
     * Numerical complexities of 0 are ignored.
     *
     * @return A HashMap with the complexities as keys, and a list of the exams with such complexity as value.
     */
    private HashMap<Integer, List<Exam>> retrieveExamsByNC() {
        Set<Integer> cnValues = exams.stream().map(Exam::getCn).collect(Collectors.toSet());
        cnValues.remove(0);
        HashMap<Integer, List<Exam>> result = new HashMap<>();
        for (Integer cnValue: cnValues) {
            result.put(cnValue, exams.stream().filter((ex) -> ex.getCn() == cnValue).collect(Collectors.toList()));
        }
        return result;
    }
}
