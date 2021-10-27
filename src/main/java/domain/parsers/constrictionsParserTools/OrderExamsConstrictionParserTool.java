package domain.parsers.constrictionsParserTools;

import domain.DataHandler;
import domain.constrictions.Constriction;
import domain.constrictions.types.examDependant.OrderExamsConstriction;
import domain.entities.Exam;
import org.apache.poi.ss.usermodel.Row;

public class OrderExamsConstrictionParserTool implements ConstrictionParserTool{
    @Override
    public Constriction parseConstriction(Row row, DataHandler dataHandler) {
        Exam exam1 = dataHandler.getExam((int) row.getCell(0).getNumericCellValue());
        Exam exam2 = dataHandler.getExam((int) (row.getCell(1).getNumericCellValue()));
        return new OrderExamsConstriction(exam1, exam2);
    }
}
