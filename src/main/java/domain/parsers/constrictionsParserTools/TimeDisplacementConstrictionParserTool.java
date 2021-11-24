package domain.parsers.constrictionsParserTools;

import domain.DataHandler;
import domain.constrictions.Constriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.TimeDisplacementConstriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.UserConstriction;
import domain.entities.Exam;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * This is the parser for {@link TimeDisplacementConstriction}
 */
public class TimeDisplacementConstrictionParserTool extends AbstractCosntrictionParserTool {


    @Override
    public UserConstriction parseConstriction(Row row, int baseExcelColumn, DataHandler dataHandler) {
        Exam exam1 = dataHandler.getExam((int) row.getCell(baseExcelColumn).getNumericCellValue());
        Exam exam2 = dataHandler.getExam((int) (row.getCell(baseExcelColumn + 1).getNumericCellValue()));
        return new TimeDisplacementConstriction(exam1, exam2, (long) row.getCell(baseExcelColumn + 2).getNumericCellValue(),
                dataHandler.getConfigurer().getDateTimeConfigurer().getExamDates());
    }

    @Override
    public void writeConstriction(Constriction con, Row row, int baseExcelColumn) {
        TimeDisplacementConstriction tdc = (TimeDisplacementConstriction) con;
        int cellCounter = -1;
        Cell cell = row.createCell(baseExcelColumn + ++cellCounter);
        cell.setCellValue(tdc.getFirst().getId());

        cell = row.createCell(baseExcelColumn + ++cellCounter);
        cell.setCellValue(tdc.getSecond().getId());

        cell = row.createCell(baseExcelColumn + ++cellCounter);
        cell.setCellValue(tdc.getDistanceInDays());

        cell = row.createCell(baseExcelColumn + ++cellCounter);
        cell.setCellValue(tdc.getLastEvaluation());
    }
}
