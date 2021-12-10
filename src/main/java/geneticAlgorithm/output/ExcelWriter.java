package geneticAlgorithm.output;

import domain.DataHandler;
import domain.constrictions.Constriction;
import domain.entities.Exam;
import domain.entities.ExamDatesComparator;
import domain.parsers.ConstrictionParser;
import domain.parsers.ExamParser;
import fitnessFunctions.greedyAlgorithm.ChromosomeDecoder;
import geneticAlgorithm.Individual;
import main.PrettyTimetable;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
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
     * This writes the provided individuals to excel files. One per individual
     * @param outputIndividuals The {@code Individual} instances, that lead to the final scheduling to be outputted.
     * @param dataHandler The {@code DataHandler} instance over which the individuals will be decoded.
     * @param directory The output directory.
     * @param outputFileName The name prefix of the output file.
     * @throws IOException In case there is a problem when writing the excel.
     */
    public static void excelWrite(HashSet<Individual> outputIndividuals, DataHandler dataHandler,
                                  String directory, String outputFileName) throws IOException {

        ChromosomeDecoder decoder = new ChromosomeDecoder();
        PrettyTimetable prettyTimetable = new PrettyTimetable();



        int counter = 0;
        for (Individual idv: outputIndividuals) {
            decoder.decodeNew(idv, dataHandler);
            prettyTimetable.orderScheduling(dataHandler);
            List<Exam> finalResult = dataHandler.getClonedSchedule();
            HashMap<String, List<Constriction>> verifiedConstrictions = dataHandler.verifyConstrictions();
            dataHandler.resetScheduling();
            Comparator<Exam> examComparator = new ExamDatesComparator();
            finalResult.sort(examComparator);
            counter++;


            parseExamListToExcel(directory, outputFileName, counter, finalResult,
                    verifiedConstrictions, dataHandler.getConfigurer().getDateTimeConfigurer().getExamDates());
        }


    }

    /**
     * Writes the exam list to an Excel file
     * @param directory The directory of the output file.
     * @param outputFileName The name of the outputFile
     * @param counter A counter for the suffix.
     * @param finalResult The List of exams to be written
     * @param verifiedConstrictions The list of constrictions to be written
     * @param calendar The calendar of days to be written
     * @throws IOException if some error occurs when writing the file.
     */
    public static void parseExamListToExcel(String directory, String outputFileName, int counter, List<Exam> finalResult, HashMap<String,
            List<Constriction>> verifiedConstrictions, List<LocalDate> calendar) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        ExamParser.parseToExcel(finalResult, workbook);
        ConstrictionParser.parseToExcel(verifiedConstrictions, workbook);
        writeCalendar(workbook, calendar);

        try (FileOutputStream outputStream = new FileOutputStream(directory + outputFileName + "_" + counter + ".xlsx")) {
            workbook.write(outputStream);
        }
    }

    /**
     * Writes the calendar in excel format in the provided {@code workbook}.
     * @param workbook the workbook where the calendar must be written.
     * @param calendar the list of days that must be written.
     */
    public static void writeCalendar(XSSFWorkbook workbook, List<LocalDate> calendar) {

        XSSFSheet sheet = workbook.createSheet("Calendar");
        int rowCount = 0;
        for (LocalDate date: calendar) {
            Row row = sheet.createRow(rowCount++);
            Cell cell = row.createCell(0);
            cell.setCellValue(Date.from((date.atStartOfDay(ZoneId.systemDefault()).toInstant())));
        }

    }



}
