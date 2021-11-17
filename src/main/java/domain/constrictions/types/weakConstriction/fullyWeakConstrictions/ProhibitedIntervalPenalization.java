package domain.constrictions.types.weakConstriction.fullyWeakConstrictions;

import configuration.Configurer;
import domain.constrictions.counter.ConstrictionCounter;
import domain.constrictions.types.weakConstriction.WeakConstriction;
import domain.entities.Exam;

import java.time.Duration;
import java.util.List;

public class ProhibitedIntervalPenalization implements WeakConstriction {


    public final static String CONSTRICTION_ID = "PIP";


    /**
     * List of {@link Exam} to check the schedule.
     */
    private List<Exam> exams;

    /**
     * Data Handler configurer.
     */
    private Configurer configurer;

    /**
     * Amount of time that we have used from the prohibited interval.
     */
    private long minutes;

    /**
     * Constructor for the class
     * @param exams List of {@link Exam} to check the schedule.
     */
    public ProhibitedIntervalPenalization(List<Exam> exams, Configurer configurer) {
        this.exams = exams;
        this.configurer = configurer;
        this.minutes = -1;
    }


    @Override
    public String getConstrictionID() {
        return CONSTRICTION_ID;
    }

    @Override
    public void checkConstriction(ConstrictionCounter counter) {
        minutes = 0;
        for (Exam exam: exams) {
            if (! exam.isScheduled()) { continue;}

            if (checkProhibitedInterval(exam)) {
                minutes += Duration.between(configurer.getDateTimeConfigurer().getProhibitedIntervalInitialHour(),
                        exam.getFinisingHourWithoutExtraTime()).toMinutes();
            }
            else{
                if (exam.getInitialHour().isBefore(configurer.getDateTimeConfigurer().getProhibitedIntervalInitialHour())
                && (exam.getFinisingHourWithoutExtraTime().equals(configurer.getDateTimeConfigurer().getFinishingHourProhibitedInterval())
                || exam.getFinisingHourWithoutExtraTime().isAfter(configurer.getDateTimeConfigurer().getFinishingHourProhibitedInterval()))) {
                    minutes += Duration.between(configurer.getDateTimeConfigurer().getProhibitedIntervalInitialHour(),
                            configurer.getDateTimeConfigurer().getFinishingHourProhibitedInterval()).toMinutes();
                }
            }
        }

        counter.count(this);


    }

    /**
     *
     * @param exam
     * @return
     */
    private boolean checkProhibitedInterval(Exam exam) {
        return configurer.getDateTimeConfigurer().isHourInProhibitedInterval(exam.getFinishingHour());
    }

    public long getMinutes() {
        if (minutes == -1) throw new IllegalStateException("It is need to call checkConstriction at least once before calling this method..");
        return minutes;
    }
}
