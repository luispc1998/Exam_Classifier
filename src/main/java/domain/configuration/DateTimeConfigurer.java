package domain.configuration;

import domain.entities.Exam;
import domain.entities.Interval;
import geneticAlgorithm.output.ExcelWriter;
import logger.ConsoleLogger;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * This is in charge of managing the date and time configurations, such as the calendars,
 * the day starting hour, etc.
 *
 * <p>
 * The information is loaded from a property file and then stored into the class attributes.
 */
public class DateTimeConfigurer {

    /**
     * A list with all the possible dates where the exams can take place.
     */
    private final HashMap<LocalDate, Interval> examDates;

    /**
     * Initial hour for a resting interval where the exams cannot start.
     */
    private LocalTime restingIntervalInitialHour;

    /**
     * Ending hour for a resting interval where the exams cannot start.
     */
    private LocalTime restingIntervalEndingHour;

    /**
     * Default time to be added to the exam duration in order to compute its finishing hour.
     */
    private Duration defaultExamExtraMinutes;

    /**
     * States if the default extra time functionality is enabled.
     */
    private boolean defaultExtraTimeEnabled;

    /**
     * States if exams whose modality is "Entrega" provoke collisions on placement.
     */
    private boolean deliveryCollisionEnabled;

    /**
     * String id used in the exams at the Excel to identify deliveries on the Modality field.
     */
    private String deliveryIdentifier;

    /**
     * Constructor for the class,
     * @param dateTimeFilepath filepath to property files where the date and time configurations are stored.
     * @param inputDataFilepath filepath to the input Excel, where the exams, constraints and calendar are provided.
     */
    public DateTimeConfigurer(String dateTimeFilepath, String inputDataFilepath) {
        examDates = ExcelWriter.parseDates(inputDataFilepath);
        parseTimeConfigurations(dateTimeFilepath);
    }

    /**
     * Parses the configurations from the property file,
     * @param dateTimeFilepath filepath to the properties file where the date and time configurations are stored.
     */
    private void parseTimeConfigurations(String dateTimeFilepath) {

        InputStream configStream;
        Properties fileProperties = new Properties();
        try {
            configStream = new FileInputStream(dateTimeFilepath);
            fileProperties.load(configStream);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Could not find dates and times configuration file");
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not parse properties in dates and times configuration file");
        }
        try {
            this.restingIntervalInitialHour = LocalTime.parse(Utils.nullFilter(fileProperties.getProperty("beginningRestingIntervalHour")));
            this.restingIntervalEndingHour = LocalTime.parse(Utils.nullFilter(fileProperties.getProperty("endRestingIntervalHour")));
            this.defaultExamExtraMinutes = Duration.ofMinutes(Long.parseLong(Utils.nullFilter(fileProperties.getProperty("defaultExtraTimeMinutes"))));
            this.defaultExtraTimeEnabled = Boolean.parseBoolean(Utils.nullFilter(fileProperties.getProperty("defaultExtraTimeEnabled")));
            this.deliveryCollisionEnabled = Boolean.parseBoolean(Utils.nullFilter(fileProperties.getProperty("deliveryCollisionEnabled")));
            this.deliveryIdentifier = Utils.nullFilter(fileProperties.getProperty("deliveryIdentifier"));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Could not parse property/ies in Date and time configuration file due to date/time format problems.");
        } catch (NullPointerException e) {
            String[] neededProperties = {"beginningRestingIntervalHour", "endRestingIntervalHour", "defaultExtraTimeMinutes", "mutationProb",
                    "defaultExtraTimeEnabled", "deliveryCollisionEnabled", "deliveryIdentifier"};
            throw new IllegalArgumentException("Missing properties in date and time configuration file.\n" +
                    "The following properties are mandatory: " + Arrays.toString(neededProperties));
        }
    }





    /**
     * Checks whether a given hour is in the resting interval or not.
     * @param currentHour The hour to be checked.
     * @return true in case it is contained in the resting interval, false otherwise.
     */
    public boolean isHourInRestingInterval(LocalTime currentHour) {

        return (currentHour.isAfter(restingIntervalInitialHour) &&
                currentHour.isBefore(restingIntervalEndingHour))
                || currentHour.equals(restingIntervalInitialHour);
    }

    /**
     * Returns the ending hour of the resting interval.
     * @return the ending hour of the resting interval.
     */
    public LocalTime getFinishingHourRestingInterval() {
        return restingIntervalEndingHour;
    }

    /**
     * Checks whether a given hour meets the criteria to be an ending.
     * <p>
     * This implies both not to end after the day ending hour, but also not to start
     * before the day initial hour.
     * <p>
     * @param endingHour Hour to be checked valid.
     * @return True if is a valid hour, false otherwise.
     */
    public boolean isValidEndingHourFor(LocalDate day, LocalTime endingHour) {
        return (getDayEndingHour(day).isAfter(endingHour) ||
                getDayEndingHour(day).equals(endingHour)) &&

                getDayInitialHour(day).isBefore(endingHour) &&
                ! getDayInitialHour(day).equals(endingHour)
                ;
    }

    /**
     * Returns the list of possible dates for the exams with their interval time
     * @return the list of possible exam dates with their interval time.
     */
    public HashMap<LocalDate, Interval> getExamDatesWithTimes() {
        return examDates;
    }

    /**
     * Returns the list of possible dates for the exams with their interval time
     * @return the list of possible exam dates with their interval time.
     */
    public List<LocalDate> getExamDates() {
        List<LocalDate> dates = new ArrayList<>(examDates.keySet());
        dates.sort(LocalDate::compareTo);
        return dates;
    }

    /**
     * Returns the day starting hour for the given day.
     * @param day Day whose ending hour we want to know.
     * @return the day stating hour.
     */
    public LocalTime getDayInitialHour(LocalDate day) {
        return examDates.get(day).getStart();
    }

    /**
     * Returns the hour at which all exams must have finished for the given day.
     * @param day Day whose ending hour we want to know.
     * @return The {@code LocalTime} at which all exams must have finished.
     */
    public LocalTime getDayEndingHour(LocalDate day) {
        return examDates.get(day).getEnd();
    }

    /**
     * Returns the initial hour of the resting interval.
     * @return The {@code LocalTime} at which the resting interval ends.
     */
    public LocalTime getRestingIntervalInitialHour() {
        return restingIntervalInitialHour;
    }

    /**
     * Returns the default extra time for the exams if Extra time is enabled.
     * @return an extra Duration for the exams if Extra time is enables, 0 otherwise.
     */
    public Duration getDefaultExamExtraMinutes() {
        if (defaultExtraTimeEnabled) {
            return defaultExamExtraMinutes;
        }
        return Duration.ZERO;
    }

    /**
     * Returns the time intervals at which exams can be placed.
     *
     * <p>
     * Note that the current implementation considers just a resting interval, in case that there were more,
     * then this method should return every possible time interval.
     *
     * @return A list with the time intervals at which exams can be placed.
     * @see Interval
     */
    public List<Interval> getValidIntervals(LocalDate day){
        List<Interval>  result = new ArrayList<>();

        if (getDayInitialHour(day).isBefore(getRestingIntervalInitialHour())){
            result.add(new Interval(getDayInitialHour(day), getRestingIntervalInitialHour()));
            result.add(new Interval(getFinishingHourRestingInterval(), getDayEndingHour(day)));
            return result;
        }

        // In the resting interval?
        if (getDayInitialHour(day).isBefore(getFinishingHourRestingInterval())) {
            result.add(new Interval(getFinishingHourRestingInterval(), getDayEndingHour(day)));
            return result;
        }


        result.add(new Interval(getDayInitialHour(day), getDayEndingHour(day)));
        return result;
    }

    /**
     * Checks if an exam can collide with other exams.
     * @param exam The exam to be checked
     * @return True if the exam can collide with other exams, false otherwise.
     */
    public boolean areCollisionsEnabledFor(Exam exam) {
        if (! deliveryCollisionEnabled){
            return ! deliveryIdentifier.equalsIgnoreCase(exam.getModality());
        }
        return true;
    }
}
