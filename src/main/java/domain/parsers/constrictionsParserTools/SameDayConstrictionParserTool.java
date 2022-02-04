package domain.parsers.constrictionsParserTools;

import domain.DataHandler;
import domain.constraints.Constraint;
import domain.constraints.types.softConstrictions.userConstraints.SameDayConstraint;
import domain.constraints.types.softConstrictions.userConstraints.UserConstraint;
import domain.entities.Exam;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import utils.Utils;

/**
 * This is the parser for {@link SameDayConstraint}
 */
public class SameDayConstrictionParserTool extends AbstractCosntrictionParserTool {
    @Override
    public UserConstraint specificParseConstriction(Row row, int baseExcelColumn, DataHandler dataHandler){
        Utils.checkCellValuesArePresent(row, new int[]{baseExcelColumn, baseExcelColumn+1, baseExcelColumn+2},
                "Error creating Same Day Constraint.");
        Exam exam1 = dataHandler.getExamById((int) row.getCell(baseExcelColumn).getNumericCellValue());
        Exam exam2 = dataHandler.getExamById((int) (row.getCell(baseExcelColumn + 1).getNumericCellValue()));
        UserConstraint uc = new SameDayConstraint(exam1, exam2);
        checkIfMustBeHard(uc, row, baseExcelColumn + 2);
        return uc;
    }

    @Override
    public void writeConstriction(Constraint con, Row row, int baseExcelColumn) {
        SameDayConstraint sdc = (SameDayConstraint) con;
        int cellCounter = baseExcelColumn -1;
        Cell cell = row.createCell(++cellCounter);
        cell.setCellValue(sdc.getFirst().getId());

        cell = row.createCell(++cellCounter);
        cell.setCellValue(sdc.getSecond().getId());

        cellCounter = writeCommonThings(row, cellCounter, sdc.wasHardified(), sdc.getLastEvaluation());

        cell = row.createCell(++cellCounter);
        cell.setCellValue(sdc.getFirst().getTextualIdentifier());

        cell = row.createCell(++cellCounter);
        cell.setCellValue(sdc.getSecond().getTextualIdentifier());
    }
}
