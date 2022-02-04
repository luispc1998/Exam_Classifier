package geneticAlgorithm.output;

import domain.DataHandler;
import domain.constraints.Constraint;
import domain.constraints.counter.ConstrictionCounter;
import domain.constraints.counter.DefaultConstrictionCounter;
import domain.entities.Exam;
import domain.entities.ExamDatesComparator;
import domain.entities.Interval;
import domain.parsers.ConstrictionParser;
import domain.parsers.ExamParser;
import geneticAlgorithm.Individual;
import greedyAlgorithm.ChromosomeDecoder;
import main.PrettyTimetable;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * This writes the final individuals to excel format.
 *
 * <p>
 * It provides the final scheduling as well as the user constrictions specifying if they were fulfilled or not
 * in the provided scheduling.
 */
public class ExcelWriter {

    /**
     * Exam parser instance used in the execution.
     */
    private final ExamParser examParser;

    /**
     * Constriction parser instance used in the execution.
     */
    private final ConstrictionParser constrictionParser;

    public ExcelWriter(ExamParser examParser, ConstrictionParser constrictionParser) {
        this.constrictionParser = constrictionParser;
        this.examParser = examParser;
    }

    /**
     * This writes the provided individuals to excel files. One per individual
     * @param outputIndividuals The {@code Individual} instances, that lead to the final scheduling to be outputted.
     * @param dataHandler The {@code DataHandler} instance over which the individuals will be decoded.
     * @param directory The output directory.
     * @param outputFileName The name prefix of the output file.
     */
    public void excelWrite(HashSet<Individual> outputIndividuals, DataHandler dataHandler,
                                  String directory, String outputFileName) {

        ChromosomeDecoder decoder = new ChromosomeDecoder(dataHandler.getConfigurer());
        PrettyTimetable prettyTimetable = new PrettyTimetable();



        int counter = 0;
        for (Individual idv: outputIndividuals) {
            writeIndividualToExcel(dataHandler, directory, outputFileName, decoder, prettyTimetable, counter++, idv);
        }
    }

    public void writeIndividualToExcel(DataHandler dataHandler, String directory, String outputFileName, ChromosomeDecoder decoder, PrettyTimetable prettyTimetable, int counter, Individual idv) {
        dataHandler.resetScheduling();
        decoder.decodeNew(idv, dataHandler);
        prettyTimetable.orderScheduling(dataHandler);
        List<Exam> finalResult = dataHandler.getClonedSchedule();
        ConstrictionCounter constrictionCounter = new DefaultConstrictionCounter();
        HashMap<String, List<Constraint>> verifiedConstrictions = dataHandler.verifyConstrictions(constrictionCounter);
        Comparator<Exam> examComparator = new ExamDatesComparator();
        finalResult.sort(examComparator);
        parseExamListToExcel(directory, outputFileName, counter, finalResult,
                verifiedConstrictions, dataHandler.getConfigurer().getDateTimeConfigurer().getExamDatesWithTimes());
    }

    /**
     * Writes the exam list to an Excel file
     * @param directory The directory of the output file.
     * @param outputFileName The name of the outputFile
     * @param counter A counter for the suffix.
     * @param finalResult The List of exams to be written
     * @param verifiedConstrictions The list of constrictions to be written
     * @param calendar The calendar of days to be written
     */
    public void parseExamListToExcel(String directory, String outputFileName, int counter, List<Exam> finalResult, HashMap<String,
            List<Constraint>> verifiedConstrictions, HashMap<LocalDate, Interval> calendar) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        examParser.parseToExcel(finalResult, workbook);
        constrictionParser.parseToExcel(verifiedConstrictions, workbook);
        writeCalendar(workbook, calendar);
        String path = directory + outputFileName + "_" + counter + ".xlsx";
        try (FileOutputStream outputStream = new FileOutputStream(path)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException("No se ha podido escribir el excel en directorio de salida: "
                    + "[" + path + "]");
        }
    }

    /**
     * Writes the calendar in excel format in the provided {@code workbook}.
     * @param workbook the workbook where the calendar must be written.
     * @param calendar the list of days that must be written.
     */
    public static void writeCalendar(XSSFWorkbook workbook, HashMap<LocalDate, Interval> calendar) {

        XSSFSheet sheet = workbook.createSheet("Calendar");
        int rowCount = 0;
        for (Map.Entry<LocalDate, Interval> dayTime: calendar.entrySet()) {
            Row row = sheet.createRow(rowCount++);
            Cell cell = row.createCell(0);
            cell.setCellValue(Date.from((dayTime.getKey().atStartOfDay(ZoneId.systemDefault()).toInstant())));

            cell = row.createCell(1);
            cell.setCellValue(DateUtil.convertTime(dayTime.getValue().getStart().toString()));

            cell = row.createCell(2);
            cell.setCellValue(DateUtil.convertTime(dayTime.getValue().getEnd().toString()));
        }

    }



}
