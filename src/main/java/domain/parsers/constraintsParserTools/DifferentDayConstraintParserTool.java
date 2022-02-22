package domain.parsers.constraintsParserTools;

import domain.ExamsSchedule;
import domain.constraints.types.softConstraints.SoftConstraint;
import domain.constraints.types.softConstraints.userConstraints.DifferentDayConstraint;
import domain.constraints.types.softConstraints.userConstraints.UserConstraint;
import domain.entities.Exam;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import utils.Utils;

/**
 * This is the parser for {@link DifferentDayConstraint}
 */
public class DifferentDayConstraintParserTool extends AbstractConstraintParserTool {

    @Override
    public UserConstraint specificParseConstraint(Row row, int baseExcelColumn, ExamsSchedule examsSchedule) {
        Utils.checkCellValuesArePresent(row, new int[]{baseExcelColumn, baseExcelColumn+1, baseExcelColumn+2},
                "Error creating Different Day Constraint.");
        Exam exam1 = examsSchedule.getExamById((int) row.getCell(baseExcelColumn).getNumericCellValue());
        Exam exam2 = examsSchedule.getExamById((int) (row.getCell(baseExcelColumn + 1).getNumericCellValue()));
        UserConstraint uc = new DifferentDayConstraint(exam1, exam2);
        checkIfMustBeHard(uc, row, baseExcelColumn + 2);
        return uc;
    }

    @Override
    public void writeConstraint(SoftConstraint con, Row row, int baseExcelColumn) {
        DifferentDayConstraint ddc = (DifferentDayConstraint) con;
        int cellCounter = baseExcelColumn -1;
        Cell cell = row.createCell(++cellCounter);
        cell.setCellValue(ddc.getFirst().getId());

        cell = row.createCell(++cellCounter);
        cell.setCellValue(ddc.getSecond().getId());

        cellCounter = writeCommonThings(row, cellCounter, ddc.wasHardified(), ddc.getLastEvaluation());

        cell = row.createCell(++cellCounter);
        cell.setCellValue(ddc.getFirst().getTextualIdentifier());

        cell = row.createCell(++cellCounter);
        cell.setCellValue(ddc.getSecond().getTextualIdentifier());
    }
}
