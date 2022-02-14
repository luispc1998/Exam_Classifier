package domain.parsers.constraintsParserTools;

import domain.DataHandler;
import domain.constraints.Constraint;
import domain.constraints.types.softConstraints.userConstraints.DayIntervalConstraint;
import domain.constraints.types.softConstraints.userConstraints.UserConstraint;
import domain.entities.Exam;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import utils.Utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * This is the parser for {@link DayIntervalConstraint}
 */
public class DayIntervalConstraintParserTool extends AbstractConstraintParserTool {

    @Override
    public UserConstraint specificParseConstraint(Row row, int baseExcelColumn, DataHandler dataHandler) {
        Utils.checkCellValuesArePresent(row, new int[]{baseExcelColumn, baseExcelColumn+1, baseExcelColumn+2, baseExcelColumn + 3},
                "Error creating Day Interval Constraint.");
        List<LocalDate> calendar = dataHandler.getConfigurer().getDateTimeConfigurer().getExamDates();
        Exam exam1 = dataHandler.getExamById((int) row.getCell(baseExcelColumn).getNumericCellValue());
        UserConstraint uc = new DayIntervalConstraint(exam1, row.getCell(baseExcelColumn+1).getDateCellValue()
                .toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate(), row.getCell(baseExcelColumn+2).getDateCellValue()
                .toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate(), calendar);
        checkIfMustBeHard(uc, row, baseExcelColumn + 3);
        return uc;
    }

    @Override
    public void writeConstraint(Constraint con, Row row, int baseExcelColumn) {
        DayIntervalConstraint dic = (DayIntervalConstraint) con;
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
