package domain.constrictions.types.hardConstriction.fullyHardConstrictions;

import domain.constrictions.types.hardConstriction.AbstractHardConstriction;
import domain.entities.Exam;

import java.time.LocalDate;
import java.util.*;

/**
 * This {@link domain.constrictions.Constriction} is a {@link domain.constrictions.types.hardConstriction.HardConstriction}
 * ensures that there is no more than one exam of each course on a day.
 *
 * <p>
 * If no other possibility can be found, this restriction can be relaxed to "no more than one exam of a course an semester
 * for a day". For instance, if the condition was not relaxed there could be just an exam on a day of course 2, but if
 * it was found that there is no other possibility for the given exam than that given day, this hard constriction will also
 * check of the semester of the scheduled same course exam is the same as the exam that we want to classify.
 */
public class IsolateCourseOnDayConstriction extends AbstractHardConstriction {

    /**
     * Constriction id of this type of {@code Constriction}.
     */
    public final static String CONSTRICTION_ID = "ICD";

    /**
     * Data structure containing the current state of classified exams to compare the courses when scheduling new exams.
     *
     * <p>
     * The key is a day of the calendar, whereas the value is a list containing the courses of the exams currently scheduled
     * for that day.
     */
    private static Hashtable<LocalDate, List<Integer>> availabilities;

    /**
     * Data structure containing the current state of classified exams to compare the courses and semesters
     * when scheduling new exams.
     *
     * <p>
     * The key is a day of the calendar, whereas the value is a list containing the exans currently scheduled
     * for that day.
     */
    private static Hashtable<LocalDate, List<Exam>> availabilitiesRelaxed;

    /**
     * The exam to which this {@code HardConstriction} instance is linked.
     */
    private final Exam exam;

    /**
     * Default constructor for the class.
     * @param exam The exam to which this {@code HardConstriction} instance is linked.
     */
    public IsolateCourseOnDayConstriction(Exam exam) {
        this.exam = exam;
    }

    /**
     * Sets the data structures {@code availabilities} and {@code availabilitiesRelaxed} to their default state.
     * @param calendarDays The list of days in which exams can be places.
     * @param preScheduledExams The list of pre scheduled exams.
     */
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

    /**
     * Adds the data of an exam to the data structures when it was scheduled.
     * @param date The day in which the exam was scheduled.
     * @param examToAdd The exam that was scheduled.
     */
    public static void addCourseToDate(LocalDate date, Exam examToAdd){
        availabilities.get(date).add(examToAdd.getCourse());
        availabilitiesRelaxed.get(date).add(examToAdd);
    }

    /**
     * Removes the data of an exam to the data structures when it was unscheduled.
     * @param date The day in which the exam was unscheduled.
     * @param examToRemove The exam that was unscheduled.
     */
    public static void removeCourseFromDate(LocalDate date, Exam examToRemove) {
        availabilities.get(date).remove(Integer.valueOf(examToRemove.getCourse()));
        availabilitiesRelaxed.get(date).remove(examToRemove);
    }

    @Override
    public boolean isFulfilled() {
        if (exam.getDate() != null) {
            for (Exam ex: availabilitiesRelaxed.get(exam.getDate())) {
                if (ex.getCourse() == exam.getCourse() && ! ex.getRoundPartners().contains(exam.getId())){
                    return false;
                }
            }
            //return ! availabilities.get(exam.getDate()).contains(exam.getCourse());
        }
        return true;

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

    /**
     * Gives the set of days that this hard constriction allows the exam to be placed in.
     * @param days The current set of available days.
     * @param examToCheck The exam that is evaluated.
     * @return A subset of {@code days} in whichthe exam can be placed.
     */
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

    /**
     * Relaxed version of {@code filterViableDays} in which the semester is also checked.
     * @return True if there is no other exam with same course and semester. False otherwise.
     */
    private boolean isFulfilledRelaxed() {
        if (exam.getDate() != null) {
            for (Exam examScheduled : availabilitiesRelaxed.get(exam.getDate())) {
                if (examScheduled.getCourse() == exam.getCourse() &&
                        examScheduled.getSemester() == exam.getSemester()) {
                    return false;
                }
            }
        }
        return true;
    }


}
