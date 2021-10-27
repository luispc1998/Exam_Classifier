package domain.parsers.constrictionsParserTools;

import domain.DataHandler;
import domain.constrictions.Constriction;
import domain.constrictions.types.examDependant.DayBannedConstriction;
import domain.entities.Exam;
import org.apache.poi.ss.usermodel.Row;

import java.time.ZoneId;

public class DayBannedConstrictionParserTool implements ConstrictionParserTool{
    @Override
    public Constriction parseConstriction(Row row, DataHandler dataHandler) {
        Exam exam1 = dataHandler.getExam((int) row.getCell(0).getNumericCellValue());
        Constriction constriction = new DayBannedConstriction(exam1, row.getCell(1).getDateCellValue()
                .toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate());

        return constriction;
    }
}
