package domain.parsers.constrictionsParserTools;

import domain.DataHandler;
import domain.constrictions.Constriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.DifferentDayConstriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.UserConstriction;
import domain.entities.Exam;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import utils.Utils;

/**
 * This is the parser for {@link DifferentDayConstriction}
 */
public class DifferentDayConstrictionParserTool extends AbstractCosntrictionParserTool {

    @Override
    public UserConstriction specificParseConstriction(Row row, int baseExcelColumn, DataHandler dataHandler) {
        Utils.checkCellValuesArePresent(row, new int[]{baseExcelColumn, baseExcelColumn+1, baseExcelColumn+2},
                "Error creating Different Day Constraint.");
        Exam exam1 = dataHandler.getExamById((int) row.getCell(baseExcelColumn).getNumericCellValue());
        Exam exam2 = dataHandler.getExamById((int) (row.getCell(baseExcelColumn + 1).getNumericCellValue()));
        UserConstriction uc = new DifferentDayConstriction(exam1, exam2);
        checkIfMustBeHard(uc, row, baseExcelColumn + 2);
        return uc;
    }

    @Override
    public void writeConstriction(Constriction con, Row row, int baseExcelColumn) {
        DifferentDayConstriction ddc = (DifferentDayConstriction) con;
        int cellCounter = baseExcelColumn -1;
        Cell cell = row.createCell(++cellCounter);
        cell.setCellValue(ddc.getFirst().getId());

        cell = row.createCell(++cellCounter);
        cell.setCellValue(ddc.getSecond().getId());

        cellCounter = writeCommonThings(row, cellCounter, ddc.wasHardified(), ddc.getLastEvaluation());

        cell = row.createCell(++cellCounter);
        cell.setCellValue(ddc.getFirst().getTextualIdentifier());

        cell = row.createCell(++cellCounter);
        cell.setCellValue(ddc.getSecond().getTextualIdentifier());
    }
}
