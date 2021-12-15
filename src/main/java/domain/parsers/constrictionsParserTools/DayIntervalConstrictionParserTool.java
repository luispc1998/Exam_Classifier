package domain.parsers.constrictionsParserTools;

import domain.DataHandler;
import domain.constrictions.Constriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.DayIntervalConstriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.UserConstriction;
import domain.entities.Exam;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * This is the parser for {@link domain.constrictions.types.weakConstriction.hardifiableConstrictions.DayIntervalConstriction}
 */
public class DayIntervalConstrictionParserTool extends AbstractCosntrictionParserTool {

    @Override
    public UserConstriction parseConstriction(Row row, int baseExcelColumn, DataHandler dataHandler) {
        List<LocalDate> calendar = dataHandler.getConfigurer().getDateTimeConfigurer().getExamDates();
        Exam exam1 = dataHandler.getExam((int) row.getCell(baseExcelColumn).getNumericCellValue());


        return new DayIntervalConstriction(exam1, row.getCell(baseExcelColumn+1).getDateCellValue()
                .toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate(), row.getCell(baseExcelColumn+2).getDateCellValue()
                .toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate(), calendar);
    }

    @Override
    public void writeConstriction(Constriction con, Row row, int baseExcelColumn) {
        DayIntervalConstriction dic = (DayIntervalConstriction) con;
        int cellCounter = -1;
        Cell cell = row.createCell(baseExcelColumn + ++cellCounter);
        cell.setCellValue(dic.getExam().getId());

        cell = row.createCell(baseExcelColumn + ++cellCounter);
        cell.setCellValue(DateUtil.getExcelDate(Date.from(dic.getIntervalStart().atStartOfDay(ZoneId.systemDefault()).toInstant())));

        cell = row.createCell(baseExcelColumn + ++cellCounter);
        cell.setCellValue(DateUtil.getExcelDate(Date.from(dic.getIntervalEnd().atStartOfDay(ZoneId.systemDefault()).toInstant())));

        cell = row.createCell(baseExcelColumn + ++cellCounter);
        cell.setCellValue(dic.getLastEvaluation());
    }
}
