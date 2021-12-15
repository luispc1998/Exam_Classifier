package configuration;

import domain.entities.Interval;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.ConsoleLogger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
    private List<LocalDate> examDates;

    /**
     * First starting hour which an exam can have. It is not possible for a exam to start before this hour.
     */
    private LocalTime dayInitialHour;

    /**
     * Final ending which an exam can have. It is not possible for a exam to end after this hour.
     */
    private LocalTime dayEndingHour;

    /**
     * Initial hour for a prohibited interval where the exams cannot start.
     */
    private LocalTime prohibitedIntervalInitialHour;

    /**
     * Ending hour for a prohibited interval where the exams cannot start.
     */
    private LocalTime prohibitedIntervalEndingHour;

    /**
     * Default time to be added to the exam duration in order to compute its finishing hour.
     */
    private Duration defaultExamExtraMinutes;

    /**
     * Constructor for the class
     * @param dateTimeFilepath filepath to property files where the date and time configurations are stored.
     * @param inputDataFilepath filepath to the input excel, where the exams, constrictions and calendar are provided.
     * @throws IOException In case of error with the files.
     */
    public DateTimeConfigurer(String dateTimeFilepath, String inputDataFilepath) throws IOException {
        this.examDates = new ArrayList<>();
        parseDates(inputDataFilepath);
        parseTimeConfigurations(dateTimeFilepath);
    }

    /**
     * Parses the configurations from the property file
     * @param dateTimeFilepath filepath to the properties file where the date and time configurations are stored.
     * @throws IOException In case the property loading fails.
     */
    private void parseTimeConfigurations(String dateTimeFilepath) throws IOException {

        InputStream configStream;
        Properties fileProperties = new Properties();
        configStream = new FileInputStream(dateTimeFilepath);
        fileProperties.load(configStream);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;
        this.dayInitialHour = LocalTime.parse(fileProperties.getProperty("initialDayHour"), formatter);
        this.dayEndingHour = LocalTime.parse(fileProperties.getProperty("endDayHour"), formatter);
        this.prohibitedIntervalInitialHour = LocalTime.parse(fileProperties.getProperty("beginningProhibitedIntervalHour"));
        this.prohibitedIntervalEndingHour = LocalTime.parse(fileProperties.getProperty("endProhibitedIntervalHour"));
        this.defaultExamExtraMinutes = Duration.ofMinutes(Long.parseLong(fileProperties.getProperty("defaultCleaningTimeMinutes")));
    }

    /**
     * Parses the dates from the input Excel file
     * @param inputDataFilepath filepath to the input excel, where the exams, constrictions and calendar are provided.
     * @throws IOException In case an error occurs when loading the Excel sheet.
     */
    private void parseDates(String inputDataFilepath) throws IOException {


        //creating workbook instance that refers to .xls file
        try (FileInputStream fis = new FileInputStream(inputDataFilepath);
        Workbook workbook = new XSSFWorkbook(fis)
        ) {
            Sheet sheet = workbook.getSheetAt(2);

            Map<Integer, List<String>> data = new HashMap<>();
            int i = -1;

            ConsoleLogger.getConsoleLoggerInstance().logInfo("Parseando fechas...");

            for (Row row : sheet) {
                LocalDate date = generateDate(row);
                examDates.add(date);
                i++;
            }

            ConsoleLogger.getConsoleLoggerInstance().logInfo("Fechas creadas: " + i);
            //System.out.println("Fechas creadas: " + i);

            //It is important to sort the dates. Later it will be assumed that they are sorted.
            examDates.sort(LocalDate::compareTo);
        }

    }

    /**
     * Auxiliar method. Creates a {@code LocalDate} object from an excel row.
     * <p>
     * It is assumed that the date is in the first cell of the row.
     * @param row the excel row that contains the date to be parsed.
     * @return the corresponding {@code LocalDate} object.
     */
    private LocalDate generateDate(Row row) {
        return LocalDate.ofInstant(row.getCell(0).getDateCellValue().toInstant(), ZoneId.systemDefault());
    }

    /**
     * Checks whether a given hour is in the prohibited interval or not.
     * @param currentHour The hour to be checked.
     * @return true in case it is contained in the prohibited interval, false otherwise.
     */
    public boolean isHourInProhibitedInterval(LocalTime currentHour) {

        return (currentHour.isAfter(prohibitedIntervalInitialHour) &&
                currentHour.isBefore(prohibitedIntervalEndingHour))
                || currentHour.equals(prohibitedIntervalInitialHour);
    }

    /**
     * Returns the ending hour of the prohibited interval.
     * @return the ending hour of the prohibited interval.
     */
    public LocalTime getFinishingHourProhibitedInterval() {
        return prohibitedIntervalEndingHour;
    }

    /**
     * Checks whether a given hour meets the criteria to be an ending.
     * <p>
     * This implies both not to end after the day ending hour, but also not to start
     * before the day initial hour.
     * <p>
     * @param examStartHour The starting hour for the exam.
     * @param examDuration The duration of the exam.
     * @param extraExamTime The extra time assigned for the exam.
     * @return True if is a valid hour, false otherwise.
     */
    public boolean isValidEndingHourFor(LocalTime examStartHour, Duration examDuration, Duration extraExamTime) {
        LocalTime finalHour = examStartHour.plus(examDuration).plus(extraExamTime);
        return (getDayEndingHour().isAfter(finalHour) ||
                getDayEndingHour().equals(finalHour)) &&

                getDayInitialHour().isBefore(finalHour) &&
                ! getDayInitialHour().equals(finalHour)
                ;
    }

    /**
     * Returns the list of possible dates for the exams.
     * @return the list of possible exam dates.
     */
    public ArrayList<LocalDate> getExamDates() {
        return new ArrayList<>(examDates);
    }

    /**
     * Returns the day starting hour.
     * @return the day stating hour.
     */
    public LocalTime getDayInitialHour() {
        return dayInitialHour;
    }

    /**
     * Returns the hour at which all exams must have finished.
     * @return The {@code LocalTime} at which all exams must have finished.
     */
    public LocalTime getDayEndingHour() {
        return dayEndingHour;
    }

    /**
     * Returns the initial hour of the prohibited interval.
     * @return The {@code LocalTime} at which the prohibited interval ends.
     */
    public LocalTime getProhibitedIntervalInitialHour() {
        return prohibitedIntervalInitialHour;
    }

    /**
     * Returns the default extra time for the exams
     * @return an extra Duration for the exams.
     */
    public Duration getDefaultExamExtraMinutes() {
        return defaultExamExtraMinutes;
    }

    /**
     * Returns the time intervals at which exams can be placed.
     *
     * <p>
     * Note that the current implementation considers just a prohibited interval, in case that there were more,
     * then this method should return every possible time interval.
     *
     * @return A list with the time intervals at which exams can be placed.
     * @see Interval
     */
    public List<Interval> getValidIntervals(){
        List<Interval>  result = new ArrayList<>();
        result.add(new Interval(getDayInitialHour(), getProhibitedIntervalInitialHour()));
        result.add(new Interval(getFinishingHourProhibitedInterval(), getDayEndingHour()));
        return result;
    }
}
