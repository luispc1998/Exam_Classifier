package domain.parsers;

import configuration.Configurer;
import domain.DataHandler;
import domain.constrictions.Constriction;
import domain.constrictions.types.weakConstriction.WeakConstriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.*;
import domain.parsers.constrictionsParserTools.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.ConsoleLogger;

import java.io.FileInputStream;
import java.io.IOException;
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
    public static List<WeakConstriction> parseConstrictions(String filepath, DataHandler dataHandler) throws IOException {
        List<WeakConstriction> constrictions = new ArrayList<>();
        //creating workbook instance that refers to .xls file
        try (FileInputStream fis = new FileInputStream(filepath);
             Workbook workbook = new XSSFWorkbook(fis)
             ) {

            Sheet sheet = workbook.getSheetAt(1);

            //Map<Integer, List<String>> data = new HashMap<>();
            int i = 0;

            ConsoleLogger.getConsoleLoggerInstance().logInfo("Parseando restricciones...");

            List<String> hardConstrictionsId = dataHandler.getConfigurer().getHardConstrictionsIds();
            for (Row row : sheet) {

                if (shouldBeJumped(row)) continue;

                if (isAToolSwapNeeded(row.getCell(baseExcelColumn), dataHandler)){
                    swapTool(row, sheet.getRow(row.getRowNum() + 1), sheet.getRow(row.getRowNum() + 2));
                    jumpLines = 2;
                }
                else{
                    UserConstriction constriction = parserTool.parseConstriction(row, baseExcelColumn, dataHandler);


                    constrictions.add(constriction);

                    if (hardConstrictionsId.contains(constriction.getConstrictionID())){
                        constriction.hardify();
                    }


                    i++;
                }

            }
            ConsoleLogger.getConsoleLoggerInstance().logInfo("Restricciones creadas: " + i);
        }

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

    /**
     * Method to change the parsing type of Cosntriction when detecting it on the Excel.
     * @param constrictionIdRow The row with the constriction data
     * @param constrictionDescription The row with the constriction description
     * @param constrictionHeaders The row with the constriction headers
     */
    private static void swapTool(Row constrictionIdRow, Row constrictionDescription, Row constrictionHeaders) {

        switch (constrictionIdRow.getCell(baseExcelColumn).getStringCellValue()){
            case TimeDisplacementConstriction.CONSTRICTION_ID:
                parserTool = new TimeDisplacementConstrictionParserTool();
                parserTool.setDescription(constrictionDescription.getCell(baseExcelColumn).getStringCellValue());
                parserTool.setHeaders(getHeaders(constrictionHeaders, new int[]{baseExcelColumn, baseExcelColumn + 1
                        , baseExcelColumn + 2, baseExcelColumn + 3}));
                usedTools.put(TimeDisplacementConstriction.CONSTRICTION_ID, parserTool);
                break;
            case SameDayConstriction.CONSTRICTION_ID:
                parserTool = new SameDayConstrictionParserTool();
                parserTool.setDescription(constrictionDescription.getCell(baseExcelColumn).getStringCellValue());
                parserTool.setHeaders(getHeaders(constrictionHeaders, new int[]{baseExcelColumn, baseExcelColumn + 1
                        , baseExcelColumn + 2}));
                usedTools.put(SameDayConstriction.CONSTRICTION_ID, parserTool);
                break;
            case DifferentDayConstriction.CONSTRICTION_ID:
                parserTool = new DifferentDayConstrictionParserTool();
                parserTool.setDescription(constrictionDescription.getCell(baseExcelColumn).getStringCellValue());
                parserTool.setHeaders(getHeaders(constrictionHeaders, new int[]{baseExcelColumn, baseExcelColumn + 1
                        , baseExcelColumn + 2}));
                usedTools.put(DifferentDayConstriction.CONSTRICTION_ID, parserTool);
                break;
            case OrderExamsConstriction.CONSTRICTION_ID:
                parserTool = new OrderExamsConstrictionParserTool();
                parserTool.setDescription(constrictionDescription.getCell(baseExcelColumn).getStringCellValue());
                parserTool.setHeaders(getHeaders(constrictionHeaders, new int[]{baseExcelColumn, baseExcelColumn + 1
                        , baseExcelColumn + 2}));
                usedTools.put(OrderExamsConstriction.CONSTRICTION_ID, parserTool);
                break;
            case DayBannedConstriction.CONSTRICTION_ID:
                parserTool = new DayBannedConstrictionParserTool();
                parserTool.setDescription(constrictionDescription.getCell(baseExcelColumn).getStringCellValue());
                parserTool.setHeaders(getHeaders(constrictionHeaders, new int[]{baseExcelColumn, baseExcelColumn + 1
                        , baseExcelColumn + 2}));
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
            return dataHandler.getConfigurer().existsConstrictionID(value);

        } catch (RuntimeException e){
            return false;
        }
    }

    public static void parseToExcel(HashMap<String, List<Constriction>> verifiedConstrictions, Workbook workbook) {

            //creating workbook instance that refers to .xls file
            if (usedTools.size() == 0) {
                loadDefaultTools();
            }
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

    private static void loadDefaultTools() {

        parserTool = new TimeDisplacementConstrictionParserTool();
        parserTool.setDescription("default Description");
        parserTool.setHeaders(new String[] {"exam_id_1", "exam_id_2", "Calendar days distance", "Cumplida?"});
        usedTools.put(TimeDisplacementConstriction.CONSTRICTION_ID, parserTool);


        parserTool = new SameDayConstrictionParserTool();
        parserTool.setDescription("default Description");
        parserTool.setHeaders(new String[] {"exam_id_1", "exam_id_2", "Cumplida?"});
        usedTools.put(SameDayConstriction.CONSTRICTION_ID, parserTool);

        parserTool = new DifferentDayConstrictionParserTool();
        parserTool.setDescription("default Description");
        parserTool.setHeaders(new String[] {"exam_id_1", "exam_id_2", "Cumplida?"});
        usedTools.put(DifferentDayConstriction.CONSTRICTION_ID, parserTool);

        parserTool = new OrderExamsConstrictionParserTool();
        parserTool.setDescription("default Description");
        parserTool.setHeaders(new String[] {"exam_id_1", "exam_id_2", "Cumplida?"});
        usedTools.put(OrderExamsConstriction.CONSTRICTION_ID, parserTool);

        parserTool = new DayBannedConstrictionParserTool();
        parserTool.setDescription("default Description");
        parserTool.setHeaders(new String[] {"exam_id_1", "exam_id_2", "Cumplida?"});
        usedTools.put(DayBannedConstriction.CONSTRICTION_ID, parserTool);

    }




}
