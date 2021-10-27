package domain.parsers.constrictionsParserTools;

import domain.DataHandler;
import domain.constrictions.Constriction;
import domain.constrictions.types.examDependant.SameDayConstriction;
import domain.entities.Exam;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.List;

public class SameDayConstrictionParserTool implements ConstrictionParserTool{
    @Override
    public Constriction parseConstriction(Row row, int baseExcelColumn, DataHandler dataHandler){
        Exam exam1 = dataHandler.getExam((int) row.getCell(baseExcelColumn).getNumericCellValue());
        Exam exam2 = dataHandler.getExam((int) (row.getCell(baseExcelColumn + 1).getNumericCellValue()));
        List<Exam> sameDateExams = new ArrayList<>();
        sameDateExams.add(exam1); sameDateExams.add(exam2);


        return new SameDayConstriction(sameDateExams);
    }
}
