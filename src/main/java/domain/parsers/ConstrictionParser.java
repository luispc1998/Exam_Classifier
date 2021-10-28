package domain.parsers;

import configuration.Configurer;
import domain.constrictions.Constriction;
import domain.constrictions.types.examDependant.*;
import domain.DataHandler;
import domain.entities.Exam;
import domain.parsers.constrictionsParserTools.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConstrictionParser {

    private static int jumpLines = 0;
    private final static int baseExcelColumn = 1;
    private static ConstrictionParserTool parserTool;



    public static void main(String[] args) throws IOException {
        Configurer conf = new Configurer("files");

        DataHandler dataHandler = new DataHandler(conf);
        parseConstrictions("files/pruebas/v6_sinFechas_2.xlsx", dataHandler);

    }


    public static List<Constriction> parseConstrictions(String filepath, DataHandler dataHandler) throws IOException {
        List<Constriction> constrictions = new ArrayList<>();


        FileInputStream fis;
        //creating workbook instance that refers to .xls file
        Workbook workbook;

        try {
            fis = new FileInputStream(filepath);
            //creating workbook instance that refers to .xls file
            workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(1);

            Map<Integer, List<String>> data = new HashMap<>();
            int i = 0;

            for (Row row : sheet) {

                if (shouldBeJumped(row)) continue;

                if (isSwapNeeded(row.getCell(baseExcelColumn), dataHandler)){
                    swapTool(row, sheet.getRow(row.getRowNum() + 1), sheet.getRow(row.getRowNum() + 2));
                    jumpLines = 2;
                }
                else{
                    constrictions.add(parserTool.parseConstriction(row, baseExcelColumn, dataHandler));
                    i++;
                }
                /*
                Constriction constriction = generateConstriction(row, i, dataHandler);
                if (constriction == null) {
                    //System.out.println("Línea " + i +" saltada. No fue posible parsear el examen");
                    continue;
                }
                constrictions.add(constriction);


                 */


            }
            System.out.println("Restricciones creadas: " + i);
        }finally {}

        return constrictions;
    }

    private static boolean shouldBeJumped(Row row) {
        if (jumpLines > 0 || row.getCell(baseExcelColumn) == null) {
            jumpLines--;
            return true;
        }

        try {
            return (row.getCell(baseExcelColumn).getStringCellValue().equals("")) ;
        } catch (IllegalStateException e) {
            return false;
        }

    }

    private static void swapTool(Row constrictionIdRow, Row constrictionDescription, Row constrictionHeaders) {

        switch (constrictionIdRow.getCell(baseExcelColumn).getStringCellValue()){
            case TimeDisplacementConstriction.CONSTRICTION_ID:
                TimeDisplacementConstriction.setClassDescription(constrictionDescription.getCell(baseExcelColumn).getStringCellValue());
                //TODO guardo las decripciones para ponerlas luego ?
                parserTool = new TimeDisplacementConstrictionParserTool();
                break;
            case SameDayConstriction.CONSTRICTION_ID:
                parserTool = new SameDayConstrictionParserTool();
                break;
            case DifferentDayConstriction.CONSTRICTION_ID:
                parserTool = new DifferentDayConstrictionParserTool();
                break;
            case OrderExamsConstriction.CONSTRICTION_ID:
                parserTool = new OrderExamsConstrictionParserTool();
                break;
            case DayBannedConstriction.CONSTRICTION_ID:
                parserTool = new DayBannedConstrictionParserTool();
                break;

        }
    }

    private static boolean isSwapNeeded(Cell cell, DataHandler dataHandler) {
        try {
            String value = cell.getStringCellValue();
            return dataHandler.getConfigurer().existsConstrinctionID(value);

        } catch (RuntimeException e){
            return false;
        }
    }

    private static Constriction generateConstriction(Row row, int i, DataHandler dataHandler) {
        Constriction constriction = null;
        try {
            Exam exam1 = dataHandler.getExam((int) row.getCell(1).getNumericCellValue());
            Exam exam2;
            switch (row.getCell(0).getStringCellValue()) {
                case TimeDisplacementConstriction.CONSTRICTION_ID:
                    //TD - GCCAS-02-12 - GDVS-2-131 - 3
                    exam2 = dataHandler.getExam((int) (row.getCell(2).getNumericCellValue()));
                    constriction = new TimeDisplacementConstriction(exam1, exam2, (long) row.getCell(3).getNumericCellValue(),
                            dataHandler.getConfigurer().getDateTimeConfigurer().getExamDates());
                    break;
                case DayBannedConstriction.CONSTRICTION_ID:
                    //DB - GVVAS-053-13 - 12/2/2022
                    constriction = new DayBannedConstriction(exam1, row.getCell(2).getDateCellValue()
                            .toInstant().atZone(ZoneId.systemDefault())
                            .toLocalDate());
                    break;
                case SameDayConstriction.CONSTRICTION_ID:
                    //SD - GCCAS-02-12 - GDVS-2-131
                    exam2 = dataHandler.getExam((int) (row.getCell(2).getNumericCellValue()));
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
