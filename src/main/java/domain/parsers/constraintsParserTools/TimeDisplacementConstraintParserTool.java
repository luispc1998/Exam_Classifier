package domain.parsers.constraintsParserTools;

import domain.DataHandler;
import domain.constraints.Constraint;
import domain.constraints.types.softConstraints.userConstraints.TimeDisplacementConstraint;
import domain.constraints.types.softConstraints.userConstraints.UserConstraint;
import domain.entities.Exam;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import utils.Utils;

/**
 * This is the parser for {@link TimeDisplacementConstraint}
 */
public class TimeDisplacementConstraintParserTool extends AbstractConstraintParserTool {


    @Override
    public UserConstraint specificParseConstraint(Row row, int baseExcelColumn, DataHandler dataHandler) {
        Utils.checkCellValuesArePresent(row, new int[]{baseExcelColumn, baseExcelColumn+1, baseExcelColumn+2},
                "Error creating Time Displacement Constraint.");
        Exam exam1 = dataHandler.getExamById((int) row.getCell(baseExcelColumn).getNumericCellValue());
        Exam exam2 = dataHandler.getExamById((int) (row.getCell(baseExcelColumn + 1).getNumericCellValue()));
        UserConstraint uc = new TimeDisplacementConstraint(exam1, exam2, (long) row.getCell(baseExcelColumn + 2).getNumericCellValue(),
                dataHandler.getConfigurer().getDateTimeConfigurer().getExamDates());
        checkIfMustBeHard(uc, row, baseExcelColumn + 3);
        return uc;
    }



    @Override
    public void writeConstraint(Constraint con, Row row, int baseExcelColumn) {
        TimeDisplacementConstraint tdc = (TimeDisplacementConstraint) con;
        int cellCounter = baseExcelColumn -1;
        Cell cell = row.createCell(++cellCounter);
        cell.setCellValue(tdc.getFirst().getId());

        cell = row.createCell(++cellCounter);
        cell.setCellValue(tdc.getSecond().getId());

        cell = row.createCell(++cellCounter);
        cell.setCellValue(tdc.getDistanceInDays());

        cellCounter = writeCommonThings(row, cellCounter, tdc.wasHardified(), tdc.getLastEvaluation());

        cell = row.createCell(++cellCounter);
        cell.setCellValue(tdc.getFirst().getTextualIdentifier());

        cell = row.createCell(++cellCounter);
        cell.setCellValue(tdc.getSecond().getTextualIdentifier());


    }
}
