package domain.parsers.constrictionsParserTools;

import domain.DataHandler;
import domain.constrictions.Constriction;
import domain.constrictions.types.examDependant.DayBannedConstriction;
import domain.constrictions.types.examDependant.OrderExamsConstriction;
import domain.entities.Exam;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * This is the parser for {@link OrderExamsConstriction}
 */
public class OrderExamsConstrictionParserTool extends AbstractCosntrictionParserTool {
    @Override
    public Constriction parseConstriction(Row row, int baseExcelColumn, DataHandler dataHandler) {
        Exam exam1 = dataHandler.getExam((int) row.getCell(baseExcelColumn).getNumericCellValue());
        Exam exam2 = dataHandler.getExam((int) (row.getCell(baseExcelColumn + 1).getNumericCellValue()));
        return new OrderExamsConstriction(exam1, exam2);
    }

    @Override
    public void writeConstriction(Constriction con, Row row, int baseExcelColumn) {
        OrderExamsConstriction oec = (OrderExamsConstriction) con;
        int cellCounter = -1;
        Cell cell = row.createCell(baseExcelColumn + ++cellCounter);
        cell.setCellValue(oec.getFirst().getId());

        cell = row.createCell(baseExcelColumn + ++cellCounter);
        cell.setCellValue(oec.getSecond().getId());

        cell = row.createCell(baseExcelColumn + ++cellCounter);
        cell.setCellValue(oec.getLastEvaluation());
    }
}
