package domain.parsers.constrictionsParserTools;

import domain.DataHandler;
import domain.constrictions.Constriction;
import domain.constrictions.types.examDependant.TimeDisplacementConstriction;
import domain.entities.Exam;
import org.apache.poi.ss.usermodel.Row;

public class TimeDisplacementConstrictionParserTool implements ConstrictionParserTool {


    @Override
    public Constriction parseConstriction(Row row, int baseExcelColumn, DataHandler dataHandler) {
        Exam exam1 = dataHandler.getExam((int) row.getCell(baseExcelColumn).getNumericCellValue());
        Exam exam2 = dataHandler.getExam((int) (row.getCell(baseExcelColumn + 1).getNumericCellValue()));
        Constriction constriction = new TimeDisplacementConstriction(exam1, exam2, (long) row.getCell(baseExcelColumn + 2).getNumericCellValue(),
                dataHandler.getConfigurer().getDateTimeConfigurer().getExamDates());
        return constriction;
    }
}
