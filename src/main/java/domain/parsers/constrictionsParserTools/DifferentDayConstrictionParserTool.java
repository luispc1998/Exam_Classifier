package domain.parsers.constrictionsParserTools;

import domain.DataHandler;
import domain.constrictions.Constriction;
import domain.constrictions.types.examDependant.DayBannedConstriction;
import domain.constrictions.types.examDependant.DifferentDayConstriction;
import domain.constrictions.types.examDependant.OrderExamsConstriction;
import domain.entities.Exam;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * This is the parser for {@link DifferentDayConstriction}
 */
public class DifferentDayConstrictionParserTool extends AbstractCosntrictionParserTool {

    @Override
    public Constriction parseConstriction(Row row, int baseExcelColumn, DataHandler dataHandler) {
        Exam exam1 = dataHandler.getExam((int) row.getCell(baseExcelColumn).getNumericCellValue());
        Exam exam2 = dataHandler.getExam((int) (row.getCell(baseExcelColumn + 1).getNumericCellValue()));
        return new DifferentDayConstriction(exam1, exam2);
    }

    @Override
    public void writeConstriction(Constriction con, Row row, int baseExcelColumn) {
        DifferentDayConstriction ddc = (DifferentDayConstriction) con;
        int cellCounter = -1;
        Cell cell = row.createCell(baseExcelColumn + ++cellCounter);
        cell.setCellValue(ddc.getFirst().getId());

        cell = row.createCell(baseExcelColumn + ++cellCounter);
        cell.setCellValue(ddc.getSecond().getId());

        cell = row.createCell(baseExcelColumn + ++cellCounter);
        cell.setCellValue(ddc.getLastEvaluation());
    }
}
