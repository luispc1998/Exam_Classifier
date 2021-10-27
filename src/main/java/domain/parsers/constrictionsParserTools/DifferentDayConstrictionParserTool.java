package domain.parsers.constrictionsParserTools;

import domain.DataHandler;
import domain.constrictions.Constriction;
import domain.constrictions.types.examDependant.DifferentDayConstriction;
import domain.entities.Exam;
import org.apache.poi.ss.usermodel.Row;

public class DifferentDayConstrictionParserTool implements ConstrictionParserTool {

    @Override
    public Constriction parseConstriction(Row row, int baseExcelColumn, DataHandler dataHandler) {
        Exam exam1 = dataHandler.getExam((int) row.getCell(baseExcelColumn).getNumericCellValue());
        Exam exam2 = dataHandler.getExam((int) (row.getCell(baseExcelColumn + 1).getNumericCellValue()));
        return new DifferentDayConstriction(exam1, exam2);
    }
}
