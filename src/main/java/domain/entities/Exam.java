package domain.entities;

import domain.constrictions.types.hardConstriction.HardConstriction;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

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

    /**
     * Extra time to be added at the end of the exam.
     */
    private Duration extraTime;

    /**
     * Course of the subject to which the exams belongs.
     */
    private Integer course;

    /**
     * The seminar of the Exam.
     */
    private Integer sem;

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
    private Integer order;

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
    private Integer alumnos;

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
    private final int cn;

    /**
     * Unique integer among the exams that identifies it.
     */
    private final int id;


    /**
     * List of {@code HardConstriction} that must be considered when scheduling the exam.
     */
    private final List<HardConstriction> hardConstrictions;

    /**
     * Round identifier to indicate if the round the exam belongs to.
     */
    private String roundId;
    /**
     * Round partners ids of the exam if any.
     */
    private List<Integer> roundPartners;


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
     * @param roundId Round identifier to indicate if the round the exam belongs to.
     */
    public Exam(int course, int sem, String code,
                 String acronym, String subject, Integer order,
                 String contentType, String modalidad, Integer alumnos,
                 int cn, int id, String roundId) {

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
        this.hardConstrictions = new ArrayList<>();
        this.roundPartners = new ArrayList<>();
        this.roundId = roundId;
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
     * @param roundId Round identifier to indicate if the round the exam belongs to.
     */
    public Exam(int course, int sem, String code,
                String acronym, String subject, Integer order,
                String contentType, String modalidad, Integer alumnos, double duration,
                int cn, int id, String roundId) {

        this(course, sem, code, acronym, subject, order, contentType, modalidad, alumnos, cn, id, roundId);
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
     * @param roundId Round identifier to indicate if the round the exam belongs to.
     */
    public Exam(int course, int sem, String code,
                String acronym, String subject, Integer order,
                String contentType, String modalidad, Integer alumnos, long duration,
                LocalDate date, LocalTime initialHour, Duration extraTime,
                int cn, int id, String roundId) {

        this(course, sem, code, acronym, subject, order, contentType, modalidad, alumnos, cn, id, roundId);
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
     * Transforms a duration to the Excel format
     * @return the duration in Excel format
     * @param duration the duration object to be transformed to excel format.
     */
    private double transformToDuration(Duration duration) {
        return duration.toMinutes() / 60.0 / 24.0;
        //return (long) (duration * 24 * 60);
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
    public void setDateFromExcel(Date dateCellValue) {
        this.date = dateCellValue.toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    /**
     * Clones the current instance to a new one in the same state.
     * @return a new instance in the same state as this.
     */
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Exam clone() {
        return new Exam(course, sem, code, acronym, subject, order, contentType, modality, alumnos,
                duration.toMinutes(), date, initialHour, extraTime, cn, id, roundId);
    }

    /**
     * Set {@code initialHour} to the new value. Transforms the Excel hour format into {@link LocalTime}
     * @param excelHour Initial hour for the exam in excel hour format.
     */
    public void setHourFromExcel(double excelHour) {
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

    /**
     * Returns the finishing hour without considering the extra time.
     *
     * <p>
     * This is the hour that we will be written on the final schedule file.
     * @return The finishing hour without considering the extra time.
     */
    public LocalTime getFinishingHourWithoutExtraTime() {
        if (initialHour == null){
            return null;
        }
        return getInitialHour().plus(getDuration());
    }

    /**
     * Checks wether the input parameters for an exam will provoke a collision with this.
     * @param currentDate The date of the new scheduled exam.
     * @param currentHour The initialHour of the new scheduled exam.
     * @param chunkOfTime Time needed for the exam.
     * @return true if there will be a collision, false otherwise.
     */
    public boolean willCollideWith(LocalDate currentDate, LocalTime currentHour, Duration chunkOfTime) {
        if (isScheduled()){
            LocalTime endingCurrentTime = currentHour.plus(chunkOfTime);
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


    /**
     * Returns an array with all the data of the object to be written in the excel.
     * @return an array with all the data of the object to be written in the excel.
     */
    public Object[] getAttributes() {

        Object[] attributes = new Object[18];

        attributes[0] = course;
        attributes[1] = sem;
        attributes[2] = code;
        attributes[3] = acronym;
        attributes[4] = subject;
        attributes[5] = order;
        attributes[6] = contentType;
        attributes[7] = modality;
        attributes[8] = alumnos;
        attributes[9] =  transformToDuration(duration);
        attributes[10] = date;
        attributes[11] = getWeekDayString();
        attributes[12] = getInitialHour() == null ? null : getInitialHour();
        attributes[13] = getFinishingHourWithoutExtraTime() == null ? null : getFinishingHourWithoutExtraTime();
        attributes[14] = getExtraTime() == null ? -1 : transformToDuration(extraTime);
        attributes[15] = cn;
        attributes[16] = id;
        attributes[17] = roundId;

        return attributes;
    }

    /**
     * Returns the course of the exam.
     * @return the course of the exam.
     */
    public Integer getCourse() {
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

    /**
     * Checks whether the exam takes place on the provided day or not.
     * @param day The day in which we are checking if the exam is placed.
     * @param interval The hour interval in which we are checking if the exam is placed.
     * @return true if the exam takes place at {@code day}, false otherwise.
     */
    public boolean takesPlaceOn(LocalDate day, Interval interval) {
        return isScheduled() && getDate().atStartOfDay().equals(day.atStartOfDay()) &&
                (getInitialHour().equals(interval.getStart()) || getInitialHour().isAfter(interval.getStart()));
    }

    /**
     * Returns the minimum space needed in the timetable for the exam.
     * @return The total time consumed by the exam in the timetable.
     */
    public Duration getChunkOfTime(){
        return getDuration().plus(getExtraTime());
    }

    /**
     * Sets the initial hour of the exam to the one passed as parameter.
     * @param newInitialhour New value for the {@code initialHour}
     */
    public void setInitialHour(LocalTime newInitialhour) {
        this.initialHour = newInitialhour;
    }

    /**
     * Sets the date of the exam to a specific one.
     * @param newDate New date value of the exam.
     */
    public void setDate(LocalDate newDate){
        this.date = newDate;
    }


    /**
     * Adds a hard constriction related with this exam that must be considered when scheduling it.
     * @param hardConstriction The hard constriction to be considered.
     */
    public void addHardConstriction(HardConstriction hardConstriction) {
        this.hardConstrictions.add(hardConstriction);
    }

    /**
     * Provides the set of days in which the exam can be placed according to its {@code HardConstriction}.
     * @param days The set of available days to place the exam in.
     * @return A subset of {@code days} where the exam can be placed.
     */
    public Set<LocalDate> getViableDays(Set<LocalDate> days) {
        for (HardConstriction hardConstriction: hardConstrictions) {
            days = hardConstriction.filterViableDays(days, this);
        }
        return days;
    }

    /**
     * Returns the semester of the exam.
     * @return the semester of the exam.
     */
    public int getSemester() {
        return sem;
    }

    /**
     * Returns the round partners ids of the exam.
     * @return the round partners ids of the exam.
     */
    public List<Integer> getRoundPartners() {
        return roundPartners;
    }

    /**
     * Adds the round data to the exam..
     * @param round The round in which the exam is.
     */
    public void addRound(List<Exam> round) {
        List<Exam> roundPartners = new ArrayList<>(round);
        roundPartners.remove(this);
        this.roundPartners = roundPartners.stream().map(Exam::getId).collect(Collectors.toList());
    }

    /**
     * Returns {@code cn}  value.
     * @return {@code cn}  value,
     */
    public int getCn() {
        return cn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exam exam = (Exam) o;
        return id == exam.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Provides an String by which the exam can be easily recognized.
     * @return an String by which the exam can be easily recognized.
     */
    public String getTextualIdentifier() {
        return acronym + "-" + contentType;
    }


    /**
     * Sets the value of the roundId of the exam.
     * @param roundId New value of the roundId of the exam.
     */
    public void setRoundId(String roundId) {
        this.roundId = roundId;
    }

    /**
     * Returns the round if of the exam.
     * @return The round if of the exam.
     */
    public String getRoundId() {
        return roundId;
    }

    /**
     * Checks if a given scheduled exam collides with this exam.
     *
     * <p>
     * This method uses the other implementation of {@code willCollideWith}, the other method whose signature
     * only differs in the parameters.
     *
     * @param ex The exam thay may collide.
     * @return True in case both exams collide, false otherwise.
     */
    public boolean willCollideWith(Exam ex) {
        return willCollideWith(ex.getDate(), ex.getInitialHour(), ex.getChunkOfTime());
    }
}
