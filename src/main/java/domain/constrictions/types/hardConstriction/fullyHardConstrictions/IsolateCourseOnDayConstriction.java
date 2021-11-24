package domain.constrictions.types.hardConstriction.fullyHardConstrictions;

import domain.constrictions.types.hardConstriction.AbstractHardConstriction;
import domain.entities.Exam;

import java.time.LocalDate;
import java.util.*;

public class IsolateCourseOnDayConstriction extends AbstractHardConstriction {

    public final static String CONSTRICTION_ID = "ICD";

    private static Hashtable<LocalDate, List<Integer>> availabilities;
    private static Hashtable<LocalDate, List<Exam>> availabilitiesRelaxed;

    private Exam exam;

    public IsolateCourseOnDayConstriction(Exam exam) {
        this.exam = exam;
    }

    public static void resetAvailabilities(List<LocalDate> calendarDays, List<Exam> preScheduledExams) {
        availabilities = new Hashtable<>();
        availabilitiesRelaxed = new Hashtable<>();

        for (LocalDate day: calendarDays) {
            availabilities.put(day, new ArrayList<>());
            availabilitiesRelaxed.put(day, new ArrayList<>());
        }

        for (Exam exam: preScheduledExams) {
            availabilities.get(exam.getDate()).add(exam.getCourse());
            availabilitiesRelaxed.get(exam.getDate()).add(exam);
        }

    }

    public static void addCourseToDate(LocalDate date, Exam examToAdd){
        availabilities.get(date).add(examToAdd.getCourse());
        availabilitiesRelaxed.get(date).add(examToAdd);
    }

    public static void removeCourseFromDate(LocalDate currentDate, Exam examToRemove) {
        availabilities.get(currentDate).remove(Integer.valueOf(examToRemove.getCourse()));
        availabilitiesRelaxed.get(currentDate).remove(examToRemove);
    }

    @Override
    public boolean isFulfilled() {
        if (exam.getDate() == null) {
            return true;
        }
        else{
            for (Exam ex: availabilitiesRelaxed.get(exam.getDate())) {
                if (ex.getCourse() == exam.getCourse() && ! ex.getRoundPartners().contains(exam.getId())){
                    return false;
                }
            }
            return true;
            //return ! availabilities.get(exam.getDate()).contains(exam.getCourse());
        }

    }

    @Override
    public String getConstrictionID() {
        return null;
    }

    @Override
    public Set<LocalDate> filterViableDays(Set<LocalDate> days, Exam examToCheck) {
        Set<LocalDate> validDates = super.filterViableDays(days, examToCheck);

        if (days.size()>0 && validDates.size() == 0){
            return filterViableDaysRelaxed(days, examToCheck);
        }
        return validDates;
    }

    private Set<LocalDate> filterViableDaysRelaxed(Set<LocalDate> days, Exam examToCheck) {
        Set<LocalDate> validDates = new HashSet<>();
        for (LocalDate day: days) {
            LocalDate prevDate = examToCheck.getDate();
            examToCheck.setDate(day);
            if (isFulfilledRelaxed()){
                validDates.add(day);
            }
            examToCheck.setDate(prevDate);
        }
        return validDates;

    }

    private boolean isFulfilledRelaxed() {
        if (exam.getDate() == null) {
            return true;
        }
        else{
            for(Exam examScheduled : availabilitiesRelaxed.get(exam.getDate())) {
                if (examScheduled.getCourse() == exam.getCourse() &&
                examScheduled.getSemester() == exam.getSemester()){
                    return false;
                }
            }
            return true;
        }
    }


}
