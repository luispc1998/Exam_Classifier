package domain.constraints.types.softConstraints.fullySoftConstraints;

import domain.constraints.counter.ConstraintCounter;
import domain.constraints.types.softConstraints.SoftConstraint;
import domain.entities.Exam;
import domain.configuration.Configurer;

import java.time.Duration;
import java.util.List;

/**
 * This class penalizes the schedules in which the resting interval is occupied by exams.
 *
 * <p>
 * By using it the algorithm prioritizes schedules/solutions which have less high duration exams on the mornings, leaving
 * them for the afternoon. It's specially effective when having not too much hard constraints.
 */
public class RestingIntervalPenalization implements SoftConstraint {

    /**
     * Constraint id of this type of {@code Constraint}.
     */
    public final static String CONSTRICTION_ID = "RIP";


    /**
     * List of {@link Exam} to check the schedule.
     */
    private final List<Exam> exams;

    /**
     * Data Handler configurer.
     */
    private final Configurer configurer;

    /**
     * Amount of time that we have used from the resting interval.
     */
    private long minutes;

    private Duration dur;

    /**
     * Constructor for the class
     * @param exams List of {@link Exam} to check the schedule.
     */
    public RestingIntervalPenalization(List<Exam> exams, Configurer configurer) {
        this.exams = exams;
        this.configurer = configurer;
        this.minutes = -1;
    }


    @Override
    public String getConstraintID() {
        return CONSTRICTION_ID;
    }

    @Override
    public void checkConstraint(ConstraintCounter counter) {
        minutes = 0;
        dur = Duration.ZERO;
        for (Exam exam: exams) {
            if (! exam.isScheduled()) { continue;}

            if (checkProhibitedInterval(exam)) {
                dur = dur.plus(Duration.between(configurer.getDateTimeConfigurer().getRestingIntervalInitialHour(),
                        exam.getFinishingHourWithoutExtraTime()));
            }
            else{

                if (exam.getInitialHour().isBefore(configurer.getDateTimeConfigurer().getRestingIntervalInitialHour())
                && (exam.getFinishingHourWithoutExtraTime().equals(configurer.getDateTimeConfigurer().getFinishingHourRestingInterval())
                || exam.getFinishingHourWithoutExtraTime().isAfter(configurer.getDateTimeConfigurer().getFinishingHourRestingInterval()))) {

                    dur = dur.plus(Duration.between(configurer.getDateTimeConfigurer().getRestingIntervalInitialHour(),
                            configurer.getDateTimeConfigurer().getFinishingHourRestingInterval()));

                }
            }
        }

        counter.count(this);


    }

    /**
     * Checks if an exam's ending hour is in the resting interval
     * @param exam The exam whose finishing hour will be checked
     * @return True if the exam ends on the resting interval. False otherwise.
     */
    private boolean checkProhibitedInterval(Exam exam) {
        return configurer.getDateTimeConfigurer().isHourInRestingInterval(exam.getFinishingHourWithoutExtraTime());
    }

    /**
     * Returns the number of minutes used in the resting interval by all the exams.
     * @return the number of minutes used in the resting interval by all the exams.
     */
    public long getMinutes() {
        if (minutes == -1) throw new IllegalStateException("It is need to call checkConstraint at least once before calling this method..");
        return dur.toMinutes();
    }
}
