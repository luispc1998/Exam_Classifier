package domain.entities;

import java.time.Duration;
import java.time.LocalTime;

/**
 * This class encapsulates a time chink in which {@link domain.entities.Exam} entities can take place.
 */
public class Interval {

    /**
     * Starting hour of the interval.
     */
    private final LocalTime start;

    /**
     * Ending hour of the interval
     */
    private final LocalTime end;

    /**
     * Default constructor for the class.
     * @param start Starting hour of the interval.
     * @param end Ending hour of the interval.
     */
    public Interval(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the starting hour of the interval.
     * @return the starting hour of the interval.
     */
    public LocalTime getStart() {
        return start;
    }

    /**
     * Returns the ending hour of the interval.
     * @return the ending hour of the interval.
     */
    public LocalTime getEnd() {
        return end;
    }

    /**
     * Returns the duration of the interval as the time between of {@code start} and {@code end}.
     * @return The duration of the interval.
     */
    public Duration getDuration() {
        return Duration.between(start, end);
    }
}
