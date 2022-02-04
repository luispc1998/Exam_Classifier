package domain.constraints.types.softConstrictions.fullySoftConstraints;

import domain.constraints.Constraint;
import domain.constraints.counter.ConstrictionCounter;
import domain.constraints.types.softConstrictions.WeakConstraint;
import domain.entities.Exam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This represents a constriction that states that the same course exams should take place on different days.
 */
public class SameCourseDifferentDayConstraint implements WeakConstraint {

    /**
     * Constriction with the identifier for this type of {@link Constraint}.
     */
    public final static String CONSTRICTION_ID = "SCDD";

    /**
     * List of {@link Exam} to check the schedule.
     */
    private final List<Exam> exams;

    /**
     * Counter of how many cases were found.
     */
    private int occurrences;

    /**
     * Contructor for the class.
     * @param exams List of {@link Exam} to check the schedule.
     */
    public SameCourseDifferentDayConstraint(List<Exam> exams) {
        this.exams = exams;
        occurrences = -1;
    }

    @Override
    public void checkConstriction(ConstrictionCounter counter) {
        occurrences = 0;
        for (int i = 1; i < 5; i++) {
            occurrences += getExamsForCourse(exams, i);
        }
        counter.count(this);
    }

    /**
     * Returns for a given course how many dates are repeated mapping the exams to their dates.
     * @param exams The schedule of {@link Exam}.
     * @param course The course that we are currently checking
     * @return The number of repeated dates for the given course.
     */
    private int getExamsForCourse(List<Exam> exams, int course) {
        List<LocalDate> courseDates = new ArrayList<>();
        for (Exam exam: exams) {
            if (exam.getCourse() == course){
                courseDates.add(exam.getDate());
            }
        }
        Set<LocalDate> courseDatesSet = new HashSet<>(courseDates);

        return courseDates.size() - courseDatesSet.size();
    }

    /**
     * Returns how many times the constriction was detected over the schedule.
     * @return the number of times the cosntriction was detected
     * @throws IllegalStateException in case {@code checkConstriction} was nos called first.
     */
    public int getOccurrences() {
        if (occurrences == -1) throw new IllegalStateException("It is need to call checkConstriction at least once before calling this method..");
        return occurrences;
    }

    @Override
    public String getConstrictionID() {
        return CONSTRICTION_ID;
    }
}