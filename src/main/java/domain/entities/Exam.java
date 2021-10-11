package domain.entities;

import domain.constrictions.Constriction;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class Exam {




    // To be obtained in most of the cases.
    private LocalDate date;
    private LocalTime initialHour;

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

    private Duration duration;



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
        this.initialHour = LocalTime.ofSecondOfDay((long) v * 3600);
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
}
