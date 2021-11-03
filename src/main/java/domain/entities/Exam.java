package domain.entities;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;

/**
 * This represents a task to be scheduled. Given the domain, it is called Exam.
 *
 * <p>
 * The class itself contains all the data that an Exam in our domain can have, but most of it will not be used
 * in the application.
 * <p>
 * The most relevant is the code that refers to the exam schedule being such:
 * {@code date}, {@code initialHour}, {@code duration}, {@code extraTime} and {@code getFinishingHour}
 *
 * <p>
 * Other data such as {@code id} and {@code cn} is needed to check for constrictions.
 */
public class Exam {


    //Optional
    /**
     * Extra time to be added at the end of the exam.
     */
    private Duration extraTime;

    /**
     * Course of the subject to which the exams belongs.
     */
    private int course;

    /**
     * The seminar of the Exam.
     */
    private int sem;

    /**
     * The code of the subject of the exam.
     */
    private String code;

    /**
     * The acronym of the subject of the exam.
     */
    private String acronym;

    /**
     * The subject of the exam.
     */
    private String subject;

    /**
     * The order of the exam.
     */
    private int order;

    /**
     * The type of content of the exam. Could be: "Práctico, Teórico, Trabajo"
     */
    private String contentType;

    /**
     * The type of exam. Could be: "Entrega, Presencial, No Presencial"
     */
    private String modality;

    /**
     * The number of students enrolled for the exam.
     */
    private int alumnos;

    /**
     * The duration of the exam.
     */
    private Duration duration;

    // To be obtained in most of the cases.

    /**
     * Date in which the exam will take place
     */
    private LocalDate date;

    /**
     * Time at which the exam will start.
     */
    private LocalTime initialHour;

    /**
     * Integer representing the complexity of the exam.
     */
    private int cn;

    /**
     * Unique integer among the exams that identifies it.
     */
    private int id;

    /**
     * Constructor for the class with initial no scheduling information
     * @param course Course of the subject to which the exams belongs.
     * @param sem The seminar of the Exam.
     * @param code The code of the subject of the exam.
     * @param acronym The acronym of the subject of the exam.
     * @param subject The subject of the exam.
     * @param order The order of the exam.
     * @param contentType The type of content of the exam.
     * @param modalidad The type of exam.
     * @param alumnos The number of students enrolled for the exam.
     * @param cn Integer representing the complexity of the exam.
     * @param id Unique integer among the exams that identifies it.
     */
    private Exam(int course, int sem, String code,
                 String acronym, String subject, int order,
                 String contentType, String modalidad, int alumnos,
                 int cn, int id) {

        this.course = course;
        this.sem = sem;
        this.code = code;
        this.acronym = acronym;
        this.subject = subject;
        this.order = order;
        this.contentType = contentType;
        this.modality = modalidad;
        this.alumnos = alumnos;
        this.cn = cn;
        this.id = id;
        this.extraTime = Duration.ofMinutes(0);
    }

    /**
     * Constructor for the class, but also specifying the duration of the exam.
     * @param course Course of the subject to which the exams belongs.
     * @param sem The seminar of the Exam.
     * @param code The code of the subject of the exam.
     * @param acronym The acronym of the subject of the exam.
     * @param subject The subject of the exam.
     * @param order The order of the exam.
     * @param contentType The type of content of the exam.
     * @param modalidad The type of exam.
     * @param alumnos The number of students enrolled for the exam.
     * @param duration The duration of the exam.
     * @param cn Integer representing the complexity of the exam.
     * @param id Unique integer among the exams that identifies it.
     */
    public Exam(int course, int sem, String code,
                String acronym, String subject, int order,
                String contentType, String modalidad, int alumnos, double duration,
                int cn, int id) {

        this(course, sem, code, acronym, subject, order, contentType, modalidad, alumnos, cn, id);
        this.course = course;
        this.sem = sem;
        this.code = code;
        this.acronym = acronym;
        this.subject = subject;
        this.order = order;
        this.contentType = contentType;
        this.modality = modalidad;
        this.duration = Duration.ofMinutes(transformDuration(duration));

    }

    /**
     * Constructor for the class used for cloning instances.
     * @param course Course of the subject to which the exams belongs.
     * @param sem The seminar of the Exam.
     * @param code The code of the subject of the exam.
     * @param acronym The acronym of the subject of the exam.
     * @param subject The subject of the exam.
     * @param order The order of the exam.
     * @param contentType The type of content of the exam.
     * @param modalidad The type of exam.
     * @param alumnos The number of students enrolled for the exam.
     * @param duration The duration of the exam.
     * @param date Date in which the exam will take place
     * @param initialHour Time at which the exam will start.
     * @param extraTime Extra time to be added at the end of the exam.
     * @param cn Integer representing the complexity of the exam.
     * @param id Unique integer among the exams that identifies it.
     */
    public Exam(int course, int sem, String code,
                String acronym, String subject, int order,
                String contentType, String modalidad, int alumnos, long duration,
                LocalDate date, LocalTime initialHour, Duration extraTime,
                int cn, int id) {

        this(course, sem, code, acronym, subject, order, contentType, modalidad, alumnos, cn, id);
        this.alumnos = alumnos;
        this.date = date;
        this.initialHour = initialHour;
        this.extraTime = extraTime;
        this.duration = Duration.ofMinutes(duration);
    }


    /**
     * Transforms a duration from the Excel format
     * @param duration Duration in excel format
     * @return the duration in minutes
     */
    private long transformDuration(double duration) {
        return (long) (duration * 24 * 60);
    }

    /**
     * Returns the duration of the exam.
     * @return the duration of the exam.
     */
    public Duration getDuration() {
        return Duration.from(duration);
    }

    /**
     * Returns the date of the exam.
     * @return the date of the exam.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns the initial hour of the exam.
     * @return the initial hour of the exam. Null if it was not scheduled.
     */
    public LocalTime getInitialHour() {
        if (initialHour == null){
            return null;
        }
        return LocalTime.ofSecondOfDay(initialHour.toSecondOfDay());
    }

    /**
     * Returns the extra time for the exam.
     * @return the extra time for the exam
     */
    public Duration getExtraTime() {
        return extraTime;
    }

    /**
     * Sets {@code date} to the new value
     * @param dateCellValue new value for {@code date}
     */
    public void setDate(Date dateCellValue) {
        this.date = dateCellValue.toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    /**
     * Clones the current instance to a new one in the same state.
     * @return a new instance in the same state as this.
     */
    @Override
    public Exam clone() {
        return new Exam(course, sem, code, acronym, subject, order, contentType, modality, alumnos,
                duration.toMinutes(), date, initialHour, extraTime, cn, id);
    }

    /**
     * Set {@code initialHour} to the new value. Transforms the Excel hour format into {@link LocalTime}
     * @param excelHour Initial hour for the exam in excel hour format.
     */
    public void setHour(double excelHour) {
        this.initialHour = LocalTime.ofSecondOfDay((long) (excelHour * 3600 * 24));
    }


    /**
     * Returns the code of the exam.
     * @return the code of the exam.
     */
    public String getCode() { return this.code; }

    /**
     * Checks whether this is currently scheduled or not.
     *
     * <p>
     * An instance is considered "scheduled" if it has both a date and a starting hour.
     *
     * @return true if this is scheduled, false otherwise.
     */
    public boolean isScheduled() {
        return getDate()!=null && getInitialHour() != null;
    }

    /**
     * Cleans the scheduling of the instance
     */
    public void resetScheduling() {
        this.date = null;
        this.initialHour=null;
    }

    /**
     * Gets the hour when this will end.
     * @return the finishing hour of this. Null if this was not scheduled.
     */
    public LocalTime getFinishingHour() {
        if (initialHour == null){
            return null;
        }
        return getInitialHour().plus(getDuration()).plus(getExtraTime());
    }

    private LocalTime getFinisingHourWithoutExtraTime() {
        if (initialHour == null){
            return null;
        }
        return getInitialHour().plus(getDuration());
    }

    /**
     * Checks wether the input parameters for an exam will provoke a collision with this.
     * @param currentDate The date of the new scheduled exam.
     * @param currentHour The initialHour of the new scheduled exam.
     * @param examDuration The duration of the new scheduled exam.
     * @param extraTime The extraTime of the new scheduled exam.
     * @return true if there will be a collision, false otherwise.
     */
    public boolean willCollideWith(LocalDate currentDate, LocalTime currentHour, Duration examDuration, Duration extraTime) {
        if (isScheduled()){
            LocalTime endingCurrentTime= currentHour.plus(examDuration).plus(extraTime);
            return getDate().atStartOfDay().equals(currentDate.atStartOfDay()) &&
                    ( currentHour.isAfter(initialHour) && currentHour.isBefore(getFinishingHour()) ||
                            endingCurrentTime.isAfter(initialHour) && endingCurrentTime.isBefore(getFinishingHour()));
        }
        else{
            return false;
        }

    }

    /**
     * Schedules this for the provided date and time.
     * @param currentDate the new date for the exam.
     * @param startingHour the new initial hour for the exam.
     */
    public void scheduleFor(LocalDate currentDate, LocalTime startingHour) {
        this.date = currentDate;
        this.initialHour = startingHour;
    }

    /**
     * Gets the week day as a String for the current date.
     * @return the string of the week day correponding to the exam date. Null if this was not scheduled.
     */
    public String getWeekDayString(){
        if (date == null) {
            return null;
        }
        return date.getDayOfWeek().getDisplayName(TextStyle.FULL,
                Locale.getDefault());
    }

    /*
    public double getInitialHourExcel() {
        return this.initialHour.toSecondOfDay() /3600 / 24;
    }
    public double getEndinglHourExcel() {
        return getFinishingHour().toSecondOfDay() /3600 / 24;
    }
     */

    /**
     * Returns an array with all the data of the object to be written in the excel.
     * @return an array with all the data of the object to be written in the excel.
     */
    public Object[] getAttributes() {

        Object[] attributes = new Object[17];

        long s = duration.toSeconds();

        attributes[0] = course;
        attributes[1] = sem;
        attributes[2] = code;
        attributes[3] = acronym;
        attributes[4] = subject;
        attributes[5] = order;
        attributes[6] = contentType;
        attributes[7] = modality;
        attributes[8] = alumnos;
        attributes[9] = String.format("%d:%02d:%02d", s / 3600, (s % 3600) / 60, (s % 60));
        attributes[10] = date;
        attributes[11] = getWeekDayString();
        attributes[12] = getInitialHour() == null ? "" : formatStringForHour((long) getInitialHour().toSecondOfDay());
        attributes[13] = getFinisingHourWithoutExtraTime() == null ? "" : formatStringForHour((long) getFinisingHourWithoutExtraTime().toSecondOfDay());
        attributes[14] = getExtraTime() == null ? "" : formatStringForHour(getExtraTime().toSeconds());
        attributes[15] = cn;
        attributes[16] = id;

        return attributes;
    }

    /**
     * Formats a given hour, provided in seconds to the format of hour:minute:seconds.
     * @param seconds Integer representing seconds to be transformed.
     * @return an String of format hh:mm:ss equivalent to the provided {@code seconds}
     */
    private String formatStringForHour(Long seconds) {
        return String.format("%d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, (seconds % 60));
    }

    /**
     * Returns the course of the exam.
     * @return the course of the exam.
     */
    public int getCourse() {
        return course;
    }

    /**
     * Returns the id of the exam.
     * @return The id of the exam.
     */
    public int getId() { return id;
    }

    /**
     * Sets an Excel extra time to the  object
     * @param excelExtraTime The extra time in excel format.
     */
    public void setExtraTimeFromExcel(double excelExtraTime) {
        this.extraTime = Duration.ofMinutes(transformDuration(excelExtraTime));
    }

    /**
     * Sets extra time to the object
     * @param extraTime The extra time.
     */
    public void setExtraTime(Duration extraTime) {
        this.extraTime = Duration.from(extraTime);
    }
}
