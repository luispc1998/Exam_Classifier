package domain.entities;

import constrictions.Constriction;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Exam {


    private Duration duration;
    private boolean inPerson;
    private String name;

    private List<Constriction> constrictions;

    // To be obtained in most of the cases.
    private LocalDate date;
    private LocalTime initialHour;

    //Optional
    private Duration extraTime;



    public Exam(Duration duration, String name, List<Constriction> constrictions) {
        this.inPerson=false;
        this.name = name;
        this.duration = Duration.from(duration);
        this.constrictions = new ArrayList<>(constrictions);
    }

    public Exam(Duration duration, String name, List<Constriction> constrictions, boolean inPerson) {
        this(duration, name, constrictions);
        this.inPerson = inPerson;
    }

    public Exam(Duration duration, String name, List<Constriction> constrictions,
                boolean inPerson, Duration extraTime) {
        this(duration, name, constrictions, inPerson);
        this.extraTime = Duration.from(extraTime);
    }


    public Duration getDuration() {
        return duration;
    }

    public boolean isInPerson() {
        return inPerson;
    }

    public String getName() {
        return name;
    }

    public List<Constriction> getConstrictions() {
        return constrictions;
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
}
