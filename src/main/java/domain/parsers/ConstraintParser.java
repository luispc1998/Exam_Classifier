package domain.parsers;

import domain.ExamsSchedule;
import domain.constraints.types.softConstraints.SoftConstraint;
import domain.constraints.types.softConstraints.userConstraints.*;
import domain.parsers.constraintsParserTools.*;
import domain.configuration.Configurer;
import logger.ConsoleLogger;
import logger.dataGetter.StatisticalDataGetter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This parses from the input Excel file the constraints stated by the user.
 */
public class ConstraintParser {

    /**
     * Dynamic counter that states if lines should be jumped.
     *
     * Its value is changed dynamically.
     */
    private static int jumpLines = 0;

    /**
     * Attribute to state which is the first column of the Excel.
     *
     * It is needed in case all the tables are not stuck to the first column.
     */
    private int baseExcelColumn = 0;

    /**
     * Attribute to state which is the first row of the Excel.
     *
     * It is needed in case all the tables are not stuck to the first row.
     */
    private int baseExcelRow = 0;

    /**
     * Current {@code ConstraintParserTool} being in use.
     *
     * @see ConstraintParserTool
     */
    private ConstraintParserTool parserTool;

    /**
     * The tools that were used by the  parser.
     *
     * <p>
     * Note that if the parser was not used to parse an input file before asking it to write an output it will load
     * a default geneticAlgorithm.configuration.
     */
    private static final HashMap<String, ConstraintParserTool> usedTools = new HashMap<>();

    private StatisticalDataGetter statisticalDataGetter;
    /**
     * Default constructor for the class.
     */
    public ConstraintParser(){}

    public ConstraintParser(Configurer conf, StatisticalDataGetter statisticalDataGetter) {
        this();
        this.statisticalDataGetter = statisticalDataGetter;
        this.baseExcelColumn = conf.getExcelConfigurer().getExcelConstraintBaseColumn();
        this.baseExcelRow = conf.getExcelConfigurer().getExcelConstraintBaseRow();
        statisticalDataGetter.resetConstraintCounter();
    }

    /**
     * Method to parse the {@code Constraint} objects from the Excel.
     * @param filepath The input data Excel filepath.
     * @param examsSchedule The current dataHandler instance being use
     * @return The {@code List} of {@code Constraint} parsed from the Excel.
     */
    public List<SoftConstraint> parseConstraints(String filepath, ExamsSchedule examsSchedule) {
        List<SoftConstraint> constraints = new ArrayList<>();
        int i = 0;
        //creating workbook instance that refers to .xls file
        try (FileInputStream fis = new FileInputStream(filepath);
             Workbook workbook = new XSSFWorkbook(fis)
             ) {
            Sheet sheet = workbook.getSheetAt(1);

            ConsoleLogger.getConsoleLoggerInstance().logInfo("Parseando restricciones...");

            for (Row row : sheet) {

                if (shouldBeJumped(row)) {
                    continue;
                }

                if (isAToolSwapNeeded(row.getCell(baseExcelColumn), examsSchedule)){
                    swapTool(row, sheet.getRow(row.getRowNum() + 1), sheet.getRow(row.getRowNum() + 2));
                    jumpLines = 2;
                }
                else{

                    UserConstraint constraint = parserTool.parseConstraint(row, baseExcelColumn, examsSchedule);

                    if (constraint != null) {
                    /*
                     Even hard constraints need to be added, because these is the general
                     track to be written at the end. Hardified constraint will not execute the logic.
                     */
                        if (statisticalDataGetter != null) {
                            statisticalDataGetter.countConstraint(constraint);
                        }
                        constraints.add(constraint);
                        i++;
                    }
                }

            }

        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Could not find input Excel file");
        } catch (IOException | NullPointerException | NumberFormatException | IllegalStateException e) {
            throw new IllegalArgumentException("Could not parse input Excel file, constrains tab.");
        }
        ConsoleLogger.getConsoleLoggerInstance().logInfo("Restricciones creadas: " + i);
        return constraints;
    }

    /**
     * Checks wheter the row it's an empty line or it was stated that should be jumped.
     * @param row The row that we are currently checking.
     * @return true if it should be jumped, false otherwise.
     */
    private boolean shouldBeJumped(Row row) {
        if (jumpLines > 0) {
            jumpLines--;
            return true;
        }

        return isEmptyRow(row);
    }

    private boolean isEmptyRow(Row row) {
        for (int i = 0; i < 5; i++) {
            if (! (row.getCell(baseExcelColumn + i) == null ||
                    row.getCell(baseExcelColumn + i).getCellType().equals(CellType.BLANK))) {
                return false;
            }
        }
        return true;
    }
    /**
     * Method to change the parsing type of Cosntriction when detecting it on the Excel.
     * @param constraintIdRow The row with the constraint data
     * @param constraintDescription The row with the constraint description
     * @param constraintHeaders The row with the constraint headers
     */
    private void swapTool(Row constraintIdRow, Row constraintDescription, Row constraintHeaders) {

        switch (constraintIdRow.getCell(baseExcelColumn).getStringCellValue()){
            case TimeDisplacementConstraint.CONSTRICTION_ID:
                parserTool = new TimeDisplacementConstraintParserTool();
                parserTool.setDescription(constraintDescription.getCell(baseExcelColumn).getStringCellValue());
                parserTool.setHeaders(getHeaders(constraintHeaders, new int[]{baseExcelColumn, baseExcelColumn + 1
                        , baseExcelColumn + 2, baseExcelColumn + 3, baseExcelColumn + 4}));
                usedTools.put(TimeDisplacementConstraint.CONSTRICTION_ID, parserTool);
                break;
            case SameDayConstraint.CONSTRICTION_ID:
                parserTool = new SameDayConstraintParserTool();
                parserTool.setDescription(constraintDescription.getCell(baseExcelColumn).getStringCellValue());
                parserTool.setHeaders(getHeaders(constraintHeaders, new int[]{baseExcelColumn, baseExcelColumn + 1
                        , baseExcelColumn + 2, baseExcelColumn + 3}));
                usedTools.put(SameDayConstraint.CONSTRICTION_ID, parserTool);
                break;
            case DifferentDayConstraint.CONSTRICTION_ID:
                parserTool = new DifferentDayConstraintParserTool();
                parserTool.setDescription(constraintDescription.getCell(baseExcelColumn).getStringCellValue());
                parserTool.setHeaders(getHeaders(constraintHeaders, new int[]{baseExcelColumn, baseExcelColumn + 1
                        , baseExcelColumn + 2, baseExcelColumn + 3}));
                usedTools.put(DifferentDayConstraint.CONSTRICTION_ID, parserTool);
                break;
            case OrderExamsConstraint.CONSTRICTION_ID:
                parserTool = new OrderExamsConstraintParserTool();
                parserTool.setDescription(constraintDescription.getCell(baseExcelColumn).getStringCellValue());
                parserTool.setHeaders(getHeaders(constraintHeaders, new int[]{baseExcelColumn, baseExcelColumn + 1
                        , baseExcelColumn + 2, baseExcelColumn + 3}));
                usedTools.put(OrderExamsConstraint.CONSTRICTION_ID, parserTool);
                break;
            case DayBannedConstraint.CONSTRICTION_ID:
                parserTool = new DayBannedConstraintParserTool();
                parserTool.setDescription(constraintDescription.getCell(baseExcelColumn).getStringCellValue());
                parserTool.setHeaders(getHeaders(constraintHeaders, new int[]{baseExcelColumn, baseExcelColumn + 1
                        , baseExcelColumn + 2, baseExcelColumn + 3}));
                usedTools.put(DayBannedConstraint.CONSTRICTION_ID, parserTool);
                break;
            case DayIntervalConstraint.CONSTRICTION_ID:
                parserTool = new DayIntervalConstraintParserTool();
                parserTool.setDescription(constraintDescription.getCell(baseExcelColumn).getStringCellValue());
                parserTool.setHeaders(getHeaders(constraintHeaders, new int[]{baseExcelColumn, baseExcelColumn + 1
                        , baseExcelColumn + 2, baseExcelColumn + 3, baseExcelColumn + 4}));
                usedTools.put(DayIntervalConstraint.CONSTRICTION_ID, parserTool);
                break;

        }

    }

    /**
     * Gets the headers of the provided type of constraints.
     *
     * <p>
     * It gets the headers of the specified row.
     * @param row The row in which the headers are.
     * @param indexes An array containing the cell indexes of the headers to take.
     * @return An array with the extracted headers.
     */
    private String[] getHeaders(Row row, int[] indexes) {
        String[] result = new String[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            result[i] = row.getCell(indexes[i]).getStringCellValue();
        }
        return result;
    }

    /**
     * Checks whether it is needed to change the {@code ConstraintParserTool}.
     * @param cell Excel cell.
     * @param examsSchedule Current dataHandler.
     * @return true if it is needed to change the tool, false otherwise.
     */
    private boolean isAToolSwapNeeded(Cell cell, ExamsSchedule examsSchedule) {
        try {
            String value = cell.getStringCellValue();
            return examsSchedule.getConfigurer().existsConstraintID(value);

        } catch (RuntimeException e){
            return false;
        }
    }

    /**
     * Writes the constraint data to the provided {@code Workbook}.
     * @param verifiedConstraints The list of constraints to be written on the workbook.
     * @param workbook The workbook in which the constraints will be written.
     */
    public void parseToExcel(HashMap<String, List<SoftConstraint>> verifiedConstraints, Workbook workbook) {

            //creating workbook instance that refers to .xls file
            if (usedTools.size() == 0) {
                loadDefaultTools();
            }
            Sheet sheet = workbook.createSheet("Restricciones");
            int rowCount = 0;

            for(Map.Entry<String, List<SoftConstraint>> entry: verifiedConstraints.entrySet()) {
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

                // Write data of the constraints

                for (SoftConstraint con: entry.getValue()) {
                    row = sheet.createRow(baseExcelRow + ++rowCount);
                    parserTool.writeConstraint(con, row, baseExcelColumn);
                }

                ++rowCount;
            }

    }

    /**
     * Default geneticAlgorithm.configuration for the Constraint parser instance.
     */
    private void loadDefaultTools() {

        parserTool = new TimeDisplacementConstraintParserTool();
        parserTool.setDescription("default Description");
        parserTool.setHeaders(new String[] {"exam_id_1", "exam_id_2", "Calendar days distance", "Hard?", "Cumplida?"});
        usedTools.put(TimeDisplacementConstraint.CONSTRICTION_ID, parserTool);


        parserTool = new SameDayConstraintParserTool();
        parserTool.setDescription("default Description");
        parserTool.setHeaders(new String[] {"exam_id_1", "exam_id_2", "Hard?", "Cumplida?"});
        usedTools.put(SameDayConstraint.CONSTRICTION_ID, parserTool);

        parserTool = new DifferentDayConstraintParserTool();
        parserTool.setDescription("default Description");
        parserTool.setHeaders(new String[] {"exam_id_1", "exam_id_2", "Hard?", "Cumplida?"});
        usedTools.put(DifferentDayConstraint.CONSTRICTION_ID, parserTool);

        parserTool = new OrderExamsConstraintParserTool();
        parserTool.setDescription("default Description");
        parserTool.setHeaders(new String[] {"exam_id_1", "exam_id_2", "Hard?", "Cumplida?"});
        usedTools.put(OrderExamsConstraint.CONSTRICTION_ID, parserTool);

        parserTool = new DayBannedConstraintParserTool();
        parserTool.setDescription("default Description");
        parserTool.setHeaders(new String[] {"exam_id_1", "day_banned", "Hard?", "Cumplida?"});
        usedTools.put(DayBannedConstraint.CONSTRICTION_ID, parserTool);

        parserTool = new DayIntervalConstraintParserTool();
        parserTool.setDescription("default Description");
        parserTool.setHeaders(new String[] {"exam_id_1", "interval_start", "interval_end", "Hard?", "Cumplida?"});
        usedTools.put(DayIntervalConstraint.CONSTRICTION_ID, parserTool);

    }




}
