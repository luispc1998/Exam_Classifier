package domain.constrictions.types.weakConstriction.fullyWeakConstrictions;

import domain.constrictions.counter.ConstrictionCounter;
import domain.constrictions.types.weakConstriction.WeakConstriction;
import domain.entities.Exam;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This tries to spread in the calendar exams of the same complexity.
 *
 * <p>
 * Consider two exams of complexity 10, such as could be "Cálculo" and "Diseño de Lenguajes de Programación". This
 * weak constriction will penalize the fact that these two exams are near in the calendar, the nearer the more penalization.
 * This makes that the algorithm solutions tend to be more spreaded in terms of complexity.
 */
public class NumericalComplexityPenalization implements WeakConstriction {

    /**
     * Constriction id of this type of {@code Constriction}.
     */
    public final static String CONSTRICTION_ID = "NCP";


    /**
     * List of {@link Exam} to check the schedule.
     */
    private List<Exam> exams;

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
    public String getConstrictionID() {
        return CONSTRICTION_ID;
    }

    @Override
    public void checkConstriction(ConstrictionCounter counter) {
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

    private double accumulate(Integer key, Exam exam, Exam exam1) {
        if (exam.getDate() == null || exam1.getDate() == null) {
            return 0;
        }
        double distanceDays = Duration.between(exam.getDate().atStartOfDay(), exam1.getDate().atStartOfDay()).abs().toDays();
        if (distanceDays == 0) {
            return Math.pow(key,2);
        }
        else{
            return key/distanceDays;
        }
    }

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
