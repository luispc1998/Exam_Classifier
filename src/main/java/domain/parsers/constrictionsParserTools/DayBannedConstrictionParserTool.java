package domain.parsers.constrictionsParserTools;

import domain.DataHandler;
import domain.constrictions.Constriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.DayBannedConstriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.UserConstriction;
import domain.entities.Exam;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

import java.time.ZoneId;
import java.util.Date;

/**
 * This is the parser for {@link DayBannedConstriction}
 */
public class DayBannedConstrictionParserTool extends AbstractCosntrictionParserTool {
    @Override
    public UserConstriction parseConstriction(Row row, int baseExcelColumn, DataHandler dataHandler) {
        Exam exam1 = dataHandler.getExam((int) row.getCell(baseExcelColumn).getNumericCellValue());
        return new DayBannedConstriction(exam1, row.getCell(baseExcelColumn+1).getDateCellValue()
                .toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate());
    }

    @Override
    public void writeConstriction(Constriction con, Row row, int baseExcelColumn) {
        DayBannedConstriction dbc = (DayBannedConstriction) con;
        int cellCounter = -1;
        Cell cell = row.createCell(baseExcelColumn + ++cellCounter);
        cell.setCellValue(dbc.getExam().getId());

        cell = row.createCell(baseExcelColumn + ++cellCounter);
        cell.setCellValue(DateUtil.getExcelDate(Date.from(dbc.getDayBanned().atStartOfDay(ZoneId.systemDefault()).toInstant())));

        cell = row.createCell(baseExcelColumn + ++cellCounter);
        cell.setCellValue(dbc.getLastEvaluation());
    }
}
