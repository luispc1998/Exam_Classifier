package domain.constrictions.types.singles;

import domain.constrictions.Constriction;
import domain.constrictions.counter.ConstrictionCounter;
import domain.entities.Exam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SameCourseDifferentDayConstriction implements Constriction {

    public final static String CONSTRICTION_ID = "SCDD";

    private List<Exam> exams;

    private int occurrences;

    public SameCourseDifferentDayConstriction(List<Exam> exams) {
        this.exams = exams;
    }

    @Override
    public boolean isFulfilled(ConstrictionCounter counter) {

        for (int i = 1; i < 5; i++) {
            occurrences += getExamsForCourse(exams, i);
        }
        counter.count(this);

        boolean result = occurrences == 0;
        occurrences = 0;

        return result;
    }

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

    public int getOccurrences() {
        return occurrences;
    }
}
