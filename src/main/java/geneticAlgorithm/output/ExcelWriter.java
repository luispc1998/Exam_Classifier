package geneticAlgorithm.output;

import domain.ExamsSchedule;
import domain.constraints.counter.ConstraintCounter;
import domain.constraints.counter.DefaultConstraintCounter;
import domain.constraints.types.softConstraints.SoftConstraint;
import domain.entities.Exam;
import domain.entities.ExamDatesComparator;
import domain.entities.Interval;
import domain.parsers.ConstraintParser;
import domain.parsers.ExamParser;
import geneticAlgorithm.Individual;
import greedyAlgorithm.ChromosomeDecoder;
import logger.ConsoleLogger;
import main.PrettyTimetable;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This writes the final individuals to Excel format.
 *
 * <p>
 * It provides the final scheduling as well as the user constraints specifying if they were fulfilled or not
 * in the provided scheduling.
 */
public class ExcelWriter {

    /**
     * Exam parser instance used in the execution.
     */
    private final ExamParser examParser;

    /**
     * Constraint parser instance used in the execution.
     */
    private final ConstraintParser constraintParser;

    public ExcelWriter(ExamParser examParser, ConstraintParser constraintParser) {
        this.constraintParser = constraintParser;
        this.examParser = examParser;
    }

    /**
     * This writes the provided individuals to Excel files. One per individual
     * @param outputIndividuals The {@code Individual} instances, that lead to the final scheduling to be outputted.
     * @param examsSchedule The {@code DataHandler} instance over which the individuals will be decoded.
     * @param directory The output directory.
     * @param outputFileName The name prefix of the output file.
     */
    public void excelWrite(HashSet<Individual> outputIndividuals, ExamsSchedule examsSchedule,
                                  String directory, String outputFileName) {

        ChromosomeDecoder decoder = new ChromosomeDecoder(examsSchedule.getConfigurer());
        PrettyTimetable prettyTimetable = new PrettyTimetable();



        int counter = 0;
        for (Individual idv: outputIndividuals) {
            writeIndividualToExcel(examsSchedule, directory, outputFileName, decoder, prettyTimetable, counter++, idv);
        }
    }

    public void writeIndividualToExcel(ExamsSchedule examsSchedule, String directory, String outputFileName,
                                       ChromosomeDecoder decoder, PrettyTimetable prettyTimetable, int counter, Individual idv) {
        examsSchedule.resetScheduling();
        decoder.decode(idv, examsSchedule);
        prettyTimetable.orderScheduling(examsSchedule);
        List<Exam> finalResult = examsSchedule.getClonedSchedule();
        ConstraintCounter constraintCounter = new DefaultConstraintCounter();
        HashMap<String, List<SoftConstraint>> verifiedConstraints = examsSchedule.verifyConstraints(constraintCounter);
        Comparator<Exam> examComparator = new ExamDatesComparator();
        finalResult.sort(examComparator);
        parseExamListToExcel(directory, outputFileName, counter, finalResult,
                verifiedConstraints, examsSchedule.getConfigurer().getDateTimeConfigurer().getExamDatesWithTimes());
    }

    /**
     * Writes the exam list to an Excel file.
     * @param directory The directory of the output file.
     * @param outputFileName The name of the outputFile.
     * @param counter A counter for the suffix.
     * @param finalResult The List of exams to be written.
     * @param verifiedConstraints The list of constraints to be written.
     * @param calendar The calendar of days to be written.
     */
    public void parseExamListToExcel(String directory, String outputFileName, int counter, List<Exam> finalResult, HashMap<String,
            List<SoftConstraint>> verifiedConstraints, HashMap<LocalDate, Interval> calendar) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        examParser.parseToExcel(finalResult, workbook);
        constraintParser.parseToExcel(verifiedConstraints, workbook);
        writeCalendar(workbook, calendar);
        String subDirectory = directory + outputFileName + "_" + counter;
        Utils.createDirectory(subDirectory);
        String path = directory + outputFileName + "_" + counter + "/" + outputFileName + "_" + counter + ".xlsx";

        try (FileOutputStream outputStream = new FileOutputStream(path)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException("No se ha podido escribir el Excel en directorio de salida: "
                    + "[" + path + "]");
        }
    }

    /**
     * Writes the calendar in Excel format in the provided {@code workbook}.
     * @param workbook the workbook where the calendar must be written.
     * @param calendar the list of days that must be written.
     */
    public static void writeCalendar(XSSFWorkbook workbook, HashMap<LocalDate, Interval> calendar) {

        XSSFSheet sheet = workbook.createSheet("Calendar");
        int rowCount = 0;
        List<LocalDate> dates = calendar.keySet().stream().sorted().collect(Collectors.toList());
        //for (Map.Entry<LocalDate, Interval> dayTime: calendar.entrySet()) {

        writeCalendarHeaders(sheet.createRow(rowCount++));
        for (LocalDate date: dates){

            Row row = sheet.createRow(rowCount++);
            Cell cell = row.createCell(0);
            cell.setCellValue(Date.from((date.atStartOfDay(ZoneId.systemDefault()).toInstant())));

            cell = row.createCell(1);
            cell.setCellValue(DateUtil.convertTime(calendar.get(date).getStart().toString()));

            cell = row.createCell(2);
            cell.setCellValue(DateUtil.convertTime(calendar.get(date).getEnd().toString()));
        }

    }

    private static void writeCalendarHeaders(XSSFRow row) {
        String[] calendarHeaders = new String[]{"Day" , "Interval start", "Interval end"};
        int cellCounnt = 0;
        for (String header: calendarHeaders) {
            Cell cell = row.createCell(cellCounnt++);
            cell.setCellValue(header);
        }
    }


    /**
     * Parses the dates from the input Excel file.
     * @param inputDataFilepath filepath to the input Excel, where the exams, constraints and calendar are provided.
     * @return A map of dates where dates are the keys and the time interval in which exams can be placed the values.
     */
    public static HashMap<LocalDate, Interval> parseDates(String inputDataFilepath) {
        HashMap<LocalDate, Interval> examDates = new HashMap<>();
        //creating workbook instance that refers to .xls file
        try (FileInputStream fis = new FileInputStream(inputDataFilepath);
             Workbook workbook = new XSSFWorkbook(fis)
        ) {
            Sheet sheet = workbook.getSheetAt(2);

            int i = -1;

            ConsoleLogger.getConsoleLoggerInstance().logInfo("Parseando fechas...");

            for (Row row : sheet) {
                if (i<0) {
                    i++;
                    continue;
                }
                LocalDate date = generateDate(row);
                if (Utils.emptyCell(row.getCell(1)) || Utils.emptyCell(row.getCell(2))) {
                    throw new IllegalArgumentException();
                }

                Interval dayInterval = new Interval(setHourFromExcel(row.getCell(1).getNumericCellValue()),
                        setHourFromExcel(row.getCell(2).getNumericCellValue()));
                dayInterval.roundBoundsToHours();
                examDates.put(date, dayInterval);
                i++;
            }

            ConsoleLogger.getConsoleLoggerInstance().logInfo("Fechas creadas: " + i);
            return examDates;
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Could not find input Excel file: " + inputDataFilepath);
        } catch (IOException | NullPointerException | IllegalArgumentException e ) {
            throw new IllegalArgumentException("Could not parse calendar in input Excel file: " + inputDataFilepath);
        }

    }

    /**
     * Transforms the Excel hour format into {@link LocalTime}
     * @param excelHour Hour in excel hour format.
     * @return A LocalTime object referring the same time.
     */
    private static LocalTime setHourFromExcel(double excelHour) {
        return LocalTime.ofSecondOfDay((long) (excelHour * 3600 * 24));
    }



    /**
     * Auxiliar method. Creates a {@code LocalDate} object from an Excel row.
     * <p>
     * It is assumed that the date is in the first cell of the row.
     * @param row the Excel row that contains the date to be parsed.
     * @return the corresponding {@code LocalDate} object.
     */
    private static LocalDate generateDate(Row row) {

        if (Utils.emptyCell(row.getCell(0))){
            throw new IllegalArgumentException();
        }
        return LocalDate.ofInstant(row.getCell(0).getDateCellValue().toInstant(), ZoneId.systemDefault());
    }



}
