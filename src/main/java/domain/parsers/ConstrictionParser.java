package domain.parsers;

import domain.constrictions.Constriction;
import domain.constrictions.types.DayBannedConstriction;
import domain.constrictions.types.SameDayConstriction;
import domain.constrictions.types.TimeDisplacementConstriction;
import domain.DataHandler;
import domain.entities.Exam;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConstrictionParser {

    private final static int JUMP_LINES = 0;


    public static void main(String[] args) throws IOException {
        parseConstrictions("files/v6 (junio-julio).xlsx", new DataHandler());

    }


    public static List<Constriction> parseConstrictions(String filepath, DataHandler dataHandler) throws IOException {
        List<Constriction> constrictions = new ArrayList<>();
        FileInputStream fis=new FileInputStream(new File(filepath));
        //creating workbook instance that refers to .xls file
        Workbook workbook =new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(1);

        Map<Integer, List<String>> data = new HashMap<>();
        int i = 0;
        int jumpLines = JUMP_LINES;

        for (Row row : sheet) {

            if (jumpLines >0) {
                System.out.println("Skipped line");
                jumpLines--;
                continue;
            }

            Constriction constriction = generateConstriction(row, i, dataHandler);
            if (constriction == null) {
                System.out.println("Línea " + i +" saltada. No fue posible parsear el examen");
                continue;
            }
            constrictions.add(constriction);
            i++;
        }
        System.out.println("Restricciones creadas: " + i);
        return constrictions;
    }

    private static Constriction generateConstriction(Row row, int i, DataHandler dataHandler) {
        Constriction constriction = null;
        try {
            Exam exam1 = dataHandler.getExam(row.getCell(1).getStringCellValue());
            Exam exam2 = null;
            switch (row.getCell(0).getStringCellValue()) {
                case "a":
                    //TD - GCCAS-02-12 - GDVS-2-131 - 3
                    exam2 = dataHandler.getExam(row.getCell(1).getStringCellValue());
                    constriction = new TimeDisplacementConstriction(exam1, exam2, (long) row.getCell(2).getNumericCellValue());
                    break;
                case "b":
                    //DB - GVVAS-053-13 - 12/2/2022
                    constriction = new DayBannedConstriction(exam1, row.getCell(2).getDateCellValue()
                            .toInstant().atZone(ZoneId.systemDefault())
                            .toLocalDate());
                    break;
                case "c":
                    //SD - GCCAS-02-12 - GDVS-2-131
                    exam2 = dataHandler.getExam(row.getCell(2).getStringCellValue());
                    List<Exam> sameDateExams = new ArrayList<>();
                    sameDateExams.add(exam1); sameDateExams.add(exam2);
                    constriction = new SameDayConstriction(sameDateExams);
                    break;
            }
        }catch(Exception e){
            System.out.println("Cannot create constriction for line: " + i + ". " +
                    "Reason: " + e.getMessage());
            constriction = null;
        }

        return constriction;


    }

}
