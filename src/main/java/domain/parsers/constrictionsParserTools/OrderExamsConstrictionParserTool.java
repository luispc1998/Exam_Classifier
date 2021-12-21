package domain.parsers.constrictionsParserTools;

import domain.DataHandler;
import domain.constrictions.Constriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.OrderExamsConstriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.UserConstriction;
import domain.entities.Exam;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * This is the parser for {@link OrderExamsConstriction}
 */
public class OrderExamsConstrictionParserTool extends AbstractCosntrictionParserTool {
    @Override
    public UserConstriction parseConstriction(Row row, int baseExcelColumn, DataHandler dataHandler) {
        Exam exam1 = dataHandler.getExam((int) row.getCell(baseExcelColumn).getNumericCellValue());
        Exam exam2 = dataHandler.getExam((int) (row.getCell(baseExcelColumn + 1).getNumericCellValue()));
        UserConstriction uc = new OrderExamsConstriction(exam1, exam2);
        checkIfHard(uc, row, baseExcelColumn + 2);
        return uc;
    }

    @Override
    public void writeConstriction(Constriction con, Row row, int baseExcelColumn) {
        OrderExamsConstriction oec = (OrderExamsConstriction) con;
        int cellCounter = baseExcelColumn -1;
        Cell cell = row.createCell(++cellCounter);
        cell.setCellValue(oec.getFirst().getId());

        cell = row.createCell(++cellCounter);
        cell.setCellValue(oec.getSecond().getId());

        cellCounter = writeCommonThings(row, cellCounter, oec.wasHardified(), oec.getLastEvaluation());

        cell = row.createCell(++cellCounter);
        cell.setCellValue(oec.getFirst().getTextualIdentifier());

        cell = row.createCell(++cellCounter);
        cell.setCellValue(oec.getSecond().getTextualIdentifier());
    }
}
