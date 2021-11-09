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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This parses from the input excel file the constrictions stated by the user.
 */
public class ConstrictionParser {

    /**
     * Dinamic counter that states if lines should be jumped.
     *
     * Its value is changed dynamically.
     */
    private static int jumpLines = 0;

    /**
     * Attribute to state which is the first column of the excel.
     *
     * It is needed in case all the tables are not stuck to the first column.
     */
    private final static int baseExcelColumn = 1;

    /**
     * Attribute to state which is the first row of the excel.
     *
     * It is needed in case all the tables are not stuck to the first row.
     */
    private final static int baseExcelRow = 1;

    /**
     * Current {@code ConstrictionParserTool} being in use.
     *
     * @see ConstrictionParserTool
     */
    private static ConstrictionParserTool parserTool;


    private static HashMap<String, ConstrictionParserTool> usedTools = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Configurer conf = new Configurer("files");

        DataHandler dataHandler = new DataHandler(conf);
        parseConstrictions("files/pruebas/v6_sinFechas_2.xlsx", dataHandler);

    }

    /**
     * Method to parse the {@code Constriction} objects from the excel.
     * @param filepath The input data excel filepath.
     * @param dataHandler The current dataHandler instance being use
     * @return The {@code List} of {@code Constriction} parsed from the excel.
     * @throws IOException In case there are any problems when accesing the excel.
     */
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

                if (isAToolSwapNeeded(row.getCell(baseExcelColumn), dataHandler)){
                    swapTool(row, sheet.getRow(row.getRowNum() + 1), sheet.getRow(row.getRowNum() + 2));
                    jumpLines = 2;
                }
                else{
                    constrictions.add(parserTool.parseConstriction(row, baseExcelColumn, dataHandler));

                    //TODO, si ese tipo de restricción está en modo duro aquí debo añadirla a sus exámenes.
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

    /**
     * Checks wheter the row it's an empty line or it was stated that should be jumped.
     * @param row The row that we are currently checking.
     * @return true if it should be jumped, false otherwise.
     */
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

    /** TODO, decripciones y headers todavía no se guardan.
     * Method to change the parsing type of Cosntriction when detecting it on the Excel.
     * @param constrictionIdRow The row with the constriction data
     * @param constrictionDescription The row with the constriction description
     * @param constrictionHeaders The row with the constriction headers
     */
    private static void swapTool(Row constrictionIdRow, Row constrictionDescription, Row constrictionHeaders) {

        switch (constrictionIdRow.getCell(baseExcelColumn).getStringCellValue()){
            case TimeDisplacementConstriction.CONSTRICTION_ID:
                TimeDisplacementConstriction.setClassDescription(constrictionDescription.getCell(baseExcelColumn).getStringCellValue());
                //TODO guardo las decripciones para ponerlas luego ?
                parserTool = new TimeDisplacementConstrictionParserTool();
                parserTool.setDescription(constrictionDescription.getCell(baseExcelColumn).getStringCellValue());
                parserTool.setHeaders(getHeaders(constrictionHeaders, new int[]{baseExcelColumn, baseExcelColumn + 1, baseExcelColumn + 2}));
                usedTools.put(TimeDisplacementConstriction.CONSTRICTION_ID, parserTool);
                break;
            case SameDayConstriction.CONSTRICTION_ID:
                parserTool = new SameDayConstrictionParserTool();
                parserTool.setDescription(constrictionDescription.getCell(baseExcelColumn).getStringCellValue());
                parserTool.setHeaders(getHeaders(constrictionHeaders, new int[]{baseExcelColumn, baseExcelColumn + 1}));
                usedTools.put(SameDayConstriction.CONSTRICTION_ID, parserTool);
                break;
            case DifferentDayConstriction.CONSTRICTION_ID:
                parserTool = new DifferentDayConstrictionParserTool();
                parserTool.setDescription(constrictionDescription.getCell(baseExcelColumn).getStringCellValue());
                parserTool.setHeaders(getHeaders(constrictionHeaders, new int[]{baseExcelColumn, baseExcelColumn + 1}));
                usedTools.put(DifferentDayConstriction.CONSTRICTION_ID, parserTool);
                break;
            case OrderExamsConstriction.CONSTRICTION_ID:
                parserTool = new OrderExamsConstrictionParserTool();
                parserTool.setDescription(constrictionDescription.getCell(baseExcelColumn).getStringCellValue());
                parserTool.setHeaders(getHeaders(constrictionHeaders, new int[]{baseExcelColumn, baseExcelColumn + 1}));
                usedTools.put(OrderExamsConstriction.CONSTRICTION_ID, parserTool);
                break;
            case DayBannedConstriction.CONSTRICTION_ID:
                parserTool = new DayBannedConstrictionParserTool();
                parserTool.setDescription(constrictionDescription.getCell(baseExcelColumn).getStringCellValue());
                parserTool.setHeaders(getHeaders(constrictionHeaders, new int[]{baseExcelColumn, baseExcelColumn + 1}));
                usedTools.put(DayBannedConstriction.CONSTRICTION_ID, parserTool);
                break;
        }

    }

    private static String[] getHeaders(Row row, int[] indexes) {
        String[] result = new String[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            result[i] = row.getCell(indexes[i]).getStringCellValue();
        }
        return result;
    }

    /**
     * Checks whether it is needed to change the {@code ConstrictionParserTool}.
     * @param cell Excel cell.
     * @param dataHandler Current dataHandler.
     * @return true if it is needed to change the tool, false otherwise.
     */
    private static boolean isAToolSwapNeeded(Cell cell, DataHandler dataHandler) {
        try {
            String value = cell.getStringCellValue();
            return dataHandler.getConfigurer().existsConstrinctionID(value);

        } catch (RuntimeException e){
            return false;
        }
    }

    public static void parseToExcel(HashMap<String, List<Constriction>> verifiedConstrictions, Workbook workbook) {

            //creating workbook instance that refers to .xls file

            Sheet sheet = workbook.createSheet("Restricciones");
            int rowCount = 0;

            for(Map.Entry<String, List<Constriction>> entry: verifiedConstrictions.entrySet()) {
                if (usedTools.get(entry.getKey()) == null ){
                    continue;
                }
                parserTool = usedTools.get(entry.getKey());

                // Write ID
                Row row = sheet.createRow(baseExcelRow + ++rowCount);
                row.createCell(baseExcelColumn).setCellValue(entry.getKey());

                // Write descriptions
                row = sheet.createRow(baseExcelRow + ++rowCount);
                row.createCell(baseExcelColumn).setCellValue(parserTool.getDescription());


                // Write headers
                int col = 0;
                row = sheet.createRow(baseExcelRow + ++rowCount);
                for (String header: parserTool.getHeaders()) {
                    Cell cell = row.createCell(baseExcelColumn + col++);
                    cell.setCellValue(header);
                }

                // Write data of the constrictions

                for (Constriction con: entry.getValue()) {
                    row = sheet.createRow(baseExcelRow + ++rowCount);
                    parserTool.writeConstriction(con, row, baseExcelColumn);
                }

                ++rowCount;
            }

    }




    /* Outdated

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

     */

}
