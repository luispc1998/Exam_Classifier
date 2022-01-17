package domain.parsers.constrictionsParserTools;

import domain.DataHandler;
import domain.constrictions.Constriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.SameDayConstriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.UserConstriction;
import domain.entities.Exam;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * This is the parser for {@link SameDayConstriction}
 */
public class SameDayConstrictionParserTool extends AbstractCosntrictionParserTool {
    @Override
    public UserConstriction parseConstriction(Row row, int baseExcelColumn, DataHandler dataHandler){
        Exam exam1 = dataHandler.getExamById((int) row.getCell(baseExcelColumn).getNumericCellValue());
        Exam exam2 = dataHandler.getExamById((int) (row.getCell(baseExcelColumn + 1).getNumericCellValue()));
        UserConstriction uc = new SameDayConstriction(exam1, exam2);
        checkIfHard(uc, row, baseExcelColumn + 2);
        return uc;
    }

    @Override
    public void writeConstriction(Constriction con, Row row, int baseExcelColumn) {
        SameDayConstriction sdc = (SameDayConstriction) con;
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
