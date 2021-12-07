package domain.parsers;

import domain.DataHandler;
import domain.entities.Exam;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

/**
 * This parses the exams from the Excel input file. It also writes the schedule to the ouput file.
 */
public class ExamParser {

    /**
     * Index of rows that contain vital information of the exam.
     */
    private final static int[] vitalCells = {0, 1, 2, 3, 4, 5, 6, 7, 9};

    /**
     * Date column of the excel.
     */
    private final static int dateCol = 10;

    /**
     * Duration column of the excel.
     */
    private final static int durationCol = 12;

    /**
     * Constant to store the name of the Excel headers.
     */
    private final static String[] excelHeaders = {
            "Curso",
            "Sem",
            "Cod",
            "Acron.",
            "Asignatura",
            "Orden",
            "Contenido",
            "Modalidad",
            "Alumnos",
            "Tiempo",
            "Fecha",
            "Día",
            "Ini",
            "Fin",
            "Extra time",
            "CN",
            "ID"
    };


    /**
     * Parsing method of the exams
     * @param filepath The input Excel file
     * @return A {@code List} of parsed {@code Exam}
     * @throws IOException In case Excel reading fails
     */
    public static List<Exam> parseExams(String filepath, DataHandler dataHandler) throws IOException {
        List<Exam> exams = new ArrayList<>();
        FileInputStream fis;
        Workbook workbook;
        try {


            fis = new FileInputStream(filepath);
            //creating workbook instance that refers to .xls file
            workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheet("Planificación");

            Map<Integer, List<String>> data = new HashMap<>();
            int i = 0;
            int jumpLines = 1;

            for (Row row : sheet) {
                if (jumpLines > 0) {
                    //System.out.println("Skipped line");
                    jumpLines--;
                    continue;
                }

                Exam exam = generateExam(row, i, dataHandler);
                if (exam == null) {
                    System.out.println("Línea " + i + " saltada. No fue posible parsear el examen");
                    continue;
                }
                exams.add(exam);
                i++;
            }
            System.out.println("Examenes creados: " + i);

        }finally {}
        return exams;
    }

    /**
     * Generates an exam based of a row.
     * @param row The row with the exm data.
     * @param i The index of the row.
     * @return The {@code Exam object parsed}
     */
    private static Exam generateExam(Row row, int i, DataHandler dataHandler) {
        Exam exam = null;

        try {
            //checkVitalRowData(row, i);

            exam = new Exam((int) row.getCell(0).getNumericCellValue(),
                    (int) row.getCell(1).getNumericCellValue(),
                    row.getCell(2).getStringCellValue(),
                    row.getCell(3).getStringCellValue(),
                    row.getCell(4).getStringCellValue(),
                    (int) row.getCell(5).getNumericCellValue(),
                    row.getCell(6).getStringCellValue(),
                    row.getCell(7).getStringCellValue(),
                    (int) row.getCell(8).getNumericCellValue(),
                    row.getCell(9).getNumericCellValue(),
                    (int) row.getCell(15).getNumericCellValue(),
                    (int) row.getCell(16).getNumericCellValue());


            if (checkForAlreadyClassifiedExam(row)) {
                exam.setDateFromExcel(row.getCell(10).getDateCellValue());
                exam.setHourFromExcel(row.getCell(12).getNumericCellValue());
            }



            if (row.getCell(14) != null && row.getCell(14).getNumericCellValue() >= 0) { //TOD, por defecto tengo un 0.
                                                                                            // Tengo que poner un número negativo en el excel.
                exam.setExtraTimeFromExcel(row.getCell(14).getNumericCellValue());
            }
            else {
                exam.setExtraTime(dataHandler.getConfigurer().getDateTimeConfigurer().getDefaultExamExtraMinutes());
            }



        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e){
            System.out.println("Unknown error raised when creating exam from line: " + i);
        }

        return exam;
    }

    /**
     * Checks if an exam is already classified.
     * @param row The row of the exam
     * @return true if the exam was classified, false otherwise.
     */
    private static boolean checkForAlreadyClassifiedExam(Row row) {

        if (row.getCell(10) != null && row.getCell(12) != null) {
            try {
                return !row.getCell(10).getDateCellValue().toString().equals("") && row.getCell(12).getNumericCellValue() != 0;
            } catch (Exception e){
                return false;
            }
        }

        return false;
    }

    /**
     * Checks that all the rows that are needed for the exam instance are in place.
     * @param row The row with the exam data
     * @param lineNumber The line number of the row.
     */
    private static void checkVitalRowData(Row row, int lineNumber) {

        for (Integer i: vitalCells) {
            if (row.getCell(i) == null) {
                throw new IllegalArgumentException("Null value detected for row: " + lineNumber + ", at cell: " + i);
            }
            try {
                switch (i) {
                    case 0:
                    case 1:
                    case 5:
                    case 9:
                        row.getCell(i).getNumericCellValue();
                        break;
                    default:
                        row.getCell(i).getStringCellValue();
                }
            }catch (Exception e){
                throw new IllegalArgumentException("Illegal type detected for row: " + lineNumber + ", at cell: " + i);
            }
        }
    }


    /**
     * Parses the exam schedule to an Excel file.
     * @param exams The exam schedule.
     * @param workbook The workbook where the exam scheduling must be written.
     */
    public static void parseToExcel(List<Exam> exams, XSSFWorkbook workbook) {

        XSSFSheet sheet = workbook.createSheet("Planificación");

        int rowCount = 0;
        Row row = sheet.createRow(rowCount);
        writeHeaders(row);

        for (Exam exam : exams) {
            row = sheet.createRow(++rowCount);
            int cellCount = 0;

            for (Object att : exam.getAttributes()) {
                Cell cell = row.createCell(cellCount++);
                switch (cellCount-1) {
                    case 0:
                    case 1:
                    case 5:
                    case 8:
                    //case 14:
                    case 15:
                    case 16:
                        cell.setCellValue((int) att);
                        break;
                    case 14:
                        if (att == null) {
                            break;
                        }
                    case 9: //duration
                        cell.setCellValue((double) att);
                        break;
                    case 10: //date
                        if (att==null){
                            //cell.setCellValue("");
                            break;
                        }
                        cell.setCellValue(Date.from(((LocalDate) att).atStartOfDay(ZoneId.systemDefault()).toInstant()));
                        break;


                    default:
                        cell.setCellValue((String) att);
                }

            }



        }
    }

    /**
     * Writes the headers row for the excel file
     * @param row The row at which the Headers will be written.
     */
    private static void writeHeaders(Row row) {
        int cellCount = 0;
        for (String header : excelHeaders) {
            Cell cell = row.createCell(cellCount++);
            cell.setCellValue(header);
        }
    }


}


