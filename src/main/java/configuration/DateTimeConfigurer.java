package configuration;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DateTimeConfigurer {

    private List<LocalDate> examDates;

    private LocalTime dayInitialHour;
    private LocalTime dayEndingHour;

    private LocalTime prohibitedIntervalInitialHour;
    private LocalTime prohibitedIntervalEndingHour;

    private Duration defaultExamExtraMinutes;



    public DateTimeConfigurer(String inputFilePath, String inputDataFilePath) throws IOException {
        this.examDates = new ArrayList<>();
        parseDates(inputFilePath);
        parseTimeConfigurations(inputDataFilePath);
    }

    private void parseTimeConfigurations(String inputDataFilePath) throws IOException {

        Properties fileProperties = new Properties();
        fileProperties.load(getClass().getClassLoader().getResourceAsStream(inputDataFilePath));

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;

        this.dayInitialHour = LocalTime.parse(fileProperties.getProperty("initialDayHour"), formatter);
        this.dayEndingHour = LocalTime.parse(fileProperties.getProperty("endDayHour"), formatter);
        this.prohibitedIntervalInitialHour = LocalTime.parse(fileProperties.getProperty("beginningProhibitedIntervalHour"));
        this.prohibitedIntervalEndingHour = LocalTime.parse(fileProperties.getProperty("endProhibitedIntervalHour"));

        this.defaultExamExtraMinutes = Duration.ofMinutes(Long.parseLong(fileProperties.getProperty("defaultCleaningTimeMinutes")));
    }


    private void parseDates(String filepath) throws IOException {
        FileInputStream fis;
        Workbook workbook;
        try {


            fis = new FileInputStream(filepath);
            //creating workbook instance that refers to .xls file
            workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(2);

            Map<Integer, List<String>> data = new HashMap<>();
            int i = 0;
            // int jumpLines = 4;

            for (Row row : sheet) {
                /*
                if (jumpLines > 0) {
                    System.out.println("Skipped line");
                    jumpLines--;
                    continue;
                }
                 */

                LocalDate date = generateDate(row, i);
                if (date == null) {
                    System.out.println("Línea " + i + " saltada. No fue posible parsear la fecha");
                    continue;
                }
                examDates.add(date);
                i++;
            }
            System.out.println("Fechas creadas: " + i);

        } finally {}
    }

    private LocalDate generateDate(Row row, int i) {
        return LocalDate.ofInstant(row.getCell(0).getDateCellValue().toInstant(), ZoneId.systemDefault());
    }

    public boolean isHourInProhibitedInterval(LocalTime currentHour) {

        return (currentHour.isAfter(prohibitedIntervalInitialHour) &&
                currentHour.isBefore(prohibitedIntervalEndingHour))
                || currentHour.equals(prohibitedIntervalInitialHour)
                || currentHour.equals(prohibitedIntervalEndingHour);
    }

    public LocalTime getFinishingHourProhibitedInterval() {
        return prohibitedIntervalEndingHour;
    }

    public boolean isValidEndingHourFor(LocalTime examStartHour, Duration examDuration) {
        LocalTime finalHour = examStartHour.plus(examDuration);
        return (dayEndingHour.isAfter(finalHour) ||
                dayEndingHour.equals(finalHour)) &&

                dayInitialHour.isBefore(finalHour) &&
                ! dayInitialHour.equals(finalHour)
                ;
    }


    public ArrayList<LocalDate> getExamDates() {
        return new ArrayList<>(examDates);
    }

    public LocalTime getDayInitialHour() {
        return dayInitialHour;
    }

    public LocalTime getDayEndingHour() {
        return dayEndingHour;
    }

    public LocalTime getProhibitedIntervalInitialHour() {
        return prohibitedIntervalInitialHour;
    }

    public LocalTime getProhibitedIntervalEndingHour() {
        return prohibitedIntervalEndingHour;
    }

    public Duration getDefaultExamExtraMinutes() {
        return defaultExamExtraMinutes;
    }
}
