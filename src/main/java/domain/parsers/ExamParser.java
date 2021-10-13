package domain.parsers;

import domain.entities.Exam;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class ExamParser {

    private final static int[] vitalCells = {0, 1, 2, 3, 4, 5, 6, 7, 9};
    private final static int[] optionalCells = {8, 11, 12, 13, 14};


    public static void main(String[] args) throws IOException {
        parseExams("files/v6 (junio-julio).xlsx");

    }

    public static List<Exam> parseExams(String filepath) throws IOException {
        List<Exam> exams = new ArrayList<>();
        FileInputStream fis;
        Workbook workbook;
        try {


            fis = new FileInputStream(new File(filepath));
            //creating workbook instance that refers to .xls file
            workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);

            Map<Integer, List<String>> data = new HashMap<>();
            int i = 0;
            int jumpLines = 4;

            for (Row row : sheet) {
                if (jumpLines > 0) {
                    System.out.println("Skipped line");
                    jumpLines--;
                    continue;
                }

                Exam exam = generateExam(row, i);
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


    private static Exam generateExam(Row row, int i) {
        Exam exam = null;

        try {
            checkVitalRowData(row, i);

            exam = new Exam((int) row.getCell(0).getNumericCellValue(),
                    (int) row.getCell(1).getNumericCellValue(),
                    row.getCell(2).getStringCellValue(),
                    row.getCell(3).getStringCellValue(),
                    row.getCell(4).getStringCellValue(),
                    (int) row.getCell(5).getNumericCellValue(),
                    row.getCell(6).getStringCellValue(),
                    row.getCell(7).getStringCellValue(),
                    row.getCell(9).getNumericCellValue());


            if (checkForAlreadyClassifiedExam(row)) {
                exam.setDate(row.getCell(10).getDateCellValue());
                exam.setHour(row.getCell(12).getNumericCellValue() * 24);
            }


        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e){
            System.out.println("Unknown error raised when creating exam from line: " + i);
        }

        return exam;
    }

    private static boolean checkForAlreadyClassifiedExam(Row row) {

        if (row.getCell(10) != null && row.getCell(12) != null){
            try {
                row.getCell(10).getDateCellValue();
                row.getCell(12).getNumericCellValue();
                return true;
            } catch (Exception e){
                return false;
            }
        }

        return false;
    }

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

}


