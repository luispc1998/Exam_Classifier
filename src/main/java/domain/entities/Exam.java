package domain.entities;

import domain.constrictions.Constriction;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Exam {






    //Optional
    private Duration extraTime;


    private int course;
    private int sem;
    private String code;
    private String acronim;
    private String subject;
    private int order;
    private String contentType;
    private String modality;

    private int alumnos;

    private Duration duration;

    // To be obtained in most of the cases.
    private LocalDate date;
    private LocalTime initialHour;


    public Exam(int course, int sem, String code,
                String acronim, String subject, int order,
                String contentType, String modalidad, double duration) {

        this.course = course;
        this.code = code;
        this.acronim = acronim;
        this.subject = subject;
        this.order = order;
        this.contentType = contentType;
        this.modality = modalidad;
        this.duration = Duration.ofMinutes(transformDuration(duration));
    }



    private long transformDuration(double duration) {
        return (long) (duration * 24 * 60);
    }


    public Duration getDuration() {
        return duration;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getInitialHour() {
        return initialHour;
    }

    public Duration getExtraTime() {
        return extraTime;
    }


    public void setDate(Date dateCellValue) {
        this.date = dateCellValue.toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    @Override
    public Exam clone() {
        return new Exam(course, sem, code, acronim, subject, order, contentType, modality, duration.toMinutes());
    }

    public void setHour(double v) {
        this.initialHour = LocalTime.ofSecondOfDay((long) (v * 3600 * 24));
    }

    public String getCode() { return this.code; }

    public boolean isScheduled() {
        return getDate()!=null && getInitialHour() != null;
    }

    public void resetScheduling() {
        this.date = null;
        this.initialHour=null;
    }

    public LocalTime getFinishingHour() {
        return initialHour.plus(duration);
    }

    public boolean willCollideWith(LocalDate currentDate, LocalTime currentHour, Duration duration) {
        if (isScheduled()){
            LocalTime endingCurrentTime= currentHour.plus(duration);
            return getDate().atStartOfDay().equals(currentDate.atStartOfDay()) &&
                    ( currentHour.isAfter(initialHour) && currentHour.isBefore(getFinishingHour()) ||
                            endingCurrentTime.isAfter(initialHour) && endingCurrentTime.isBefore(getFinishingHour()));
        }
        else{
            return false;
        }

    }

    public void scheduleFor(LocalDate currentDate, LocalTime currentHour) {
        this.date = currentDate;
        this.initialHour = currentHour;
    }

    public String getWeekDayString(){
        return date.getDayOfWeek().getDisplayName(TextStyle.FULL,
                Locale.getDefault());
    }

    public double getInitialHourExcel() {
        return this.initialHour.toSecondOfDay() /3600 / 24;
    }
    public double getEndinglHourExcel() {
        return getFinishingHour().toSecondOfDay() /3600 / 24;
    }

    public Object[] getAttributes() {

        Object[] attributes = new Object[15];

        attributes[0] = course;
        attributes[1] = sem;
        attributes[2] = code;
        attributes[3] = acronim;
        attributes[4] = subject;
        attributes[5] = order;
        attributes[6] = contentType;
        attributes[7] = modality;
        attributes[8] = sem;
        attributes[9] = duration;
        attributes[10] = date;
        attributes[11] = getWeekDayString();
        attributes[12] = getInitialHourExcel();
        attributes[13] = getEndinglHourExcel();
        attributes[14] = sem;


        return attributes;
    }
}
