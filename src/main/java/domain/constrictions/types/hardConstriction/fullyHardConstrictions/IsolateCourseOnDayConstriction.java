package domain.constrictions.types.hardConstriction.fullyHardConstrictions;

import domain.constrictions.counter.ConstrictionCounter;
import domain.constrictions.types.hardConstriction.AbstractHardConstriction;
import domain.entities.Exam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class IsolateCourseOnDayConstriction extends AbstractHardConstriction {

    public final static String CONSTRICTION_ID = "ICD";

    private static Hashtable<LocalDate, List<Integer>> availabilities;
    
    private Exam exam;

    public IsolateCourseOnDayConstriction(Exam exam) {
        this.exam = exam;
    }

    public static void resetAvailabilities(List<LocalDate> calendarDays, List<Exam> preScheduledExams) {
        availabilities = new Hashtable<>();

        for (LocalDate day: calendarDays) {
            availabilities.put(day, new ArrayList<>());
        }

        for (Exam exam: preScheduledExams) {
            availabilities.get(exam.getDate()).add(exam.getCourse());
        }

    }

    public static void addCourseToDate(LocalDate date, int course){
        availabilities.get(date).add(course);
    }

    @Override
    public boolean isFulfilled() {
        if (exam.getDate() == null) {
            return true;
        }
        else{
            return ! availabilities.get(exam.getDate()).contains(exam.getCourse());
        }

    }

    @Override
    public String getConstrictionID() {
        return null;
    }
}
