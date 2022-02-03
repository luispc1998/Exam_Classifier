package domain.parsers.constrictionsParserTools;

import domain.DataHandler;
import domain.constrictions.Constriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.DayBannedConstriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.UserConstriction;
import domain.entities.Exam;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import utils.Utils;

import java.time.ZoneId;
import java.util.Date;

/**
 * This is the parser for {@link DayBannedConstriction}
 */
public class DayBannedConstrictionParserTool extends AbstractCosntrictionParserTool {
    @Override
    public UserConstriction specificParseConstriction(Row row, int baseExcelColumn, DataHandler dataHandler) {
        Utils.checkCellValuesArePresent(row, new int[]{baseExcelColumn, baseExcelColumn+1, baseExcelColumn+2},
                "Error creating Day Banned Constraint.");
        Exam exam1 = dataHandler.getExamById((int) row.getCell(baseExcelColumn).getNumericCellValue());
        UserConstriction uc = new DayBannedConstriction(exam1, row.getCell(baseExcelColumn+1).getDateCellValue()
                .toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate());
        checkIfMustBeHard(uc, row, baseExcelColumn + 2);
        return uc;
    }

    @Override
    public void writeConstriction(Constriction con, Row row, int baseExcelColumn) {
        DayBannedConstriction dbc = (DayBannedConstriction) con;
        int cellCounter = baseExcelColumn -1;

        Cell cell = row.createCell(++cellCounter);
        cell.setCellValue(dbc.getExam().getId());

        cell = row.createCell(++cellCounter);
        cell.setCellValue(DateUtil.getExcelDate(Date.from(dbc.getDayBanned().atStartOfDay(ZoneId.systemDefault()).toInstant())));


        cellCounter = writeCommonThings(row, cellCounter, dbc.wasHardified(), dbc.getLastEvaluation());

        cell = row.createCell(++cellCounter);
        cell.setCellValue(dbc.getExam().getTextualIdentifier());
    }


}
