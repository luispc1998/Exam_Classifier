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
 * This is the parser for {@link DayIntervalConstriction}
 */
public class DayIntervalConstrictionParserTool extends AbstractCosntrictionParserTool {

    @Override
    public UserConstriction parseConstriction(Row row, int baseExcelColumn, DataHandler dataHandler) {
        List<LocalDate> calendar = dataHandler.getConfigurer().getDateTimeConfigurer().getExamDates();
        Exam exam1 = dataHandler.getExamById((int) row.getCell(baseExcelColumn).getNumericCellValue());
        UserConstriction uc = new DayIntervalConstriction(exam1, row.getCell(baseExcelColumn+1).getDateCellValue()
                .toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate(), row.getCell(baseExcelColumn+2).getDateCellValue()
                .toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate(), calendar);
        checkIfHard(uc, row, baseExcelColumn + 3);
        return uc;
    }

    @Override
    public void writeConstriction(Constriction con, Row row, int baseExcelColumn) {
        DayIntervalConstriction dic = (DayIntervalConstriction) con;
        int cellCounter = baseExcelColumn -1;
        Cell cell = row.createCell(++cellCounter);
        cell.setCellValue(dic.getExam().getId());

        cell = row.createCell(++cellCounter);
        cell.setCellValue(DateUtil.getExcelDate(Date.from(dic.getIntervalStart().atStartOfDay(ZoneId.systemDefault()).toInstant())));

        cell = row.createCell(++cellCounter);
        cell.setCellValue(DateUtil.getExcelDate(Date.from(dic.getIntervalEnd().atStartOfDay(ZoneId.systemDefault()).toInstant())));

        cellCounter = writeCommonThings(row, cellCounter, dic.wasHardified(), dic.getLastEvaluation());

        cell = row.createCell(++cellCounter);
        cell.setCellValue(dic.getExam().getTextualIdentifier());
    }
}
