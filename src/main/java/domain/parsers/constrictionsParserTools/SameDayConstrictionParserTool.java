package domain.parsers.constrictionsParserTools;

import domain.DataHandler;
import domain.constrictions.Constriction;
import domain.constrictions.types.examDependant.DayBannedConstriction;
import domain.constrictions.types.examDependant.DifferentDayConstriction;
import domain.constrictions.types.examDependant.SameDayConstriction;
import domain.entities.Exam;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the parser for {@link SameDayConstriction}
 */
public class SameDayConstrictionParserTool extends AbstractCosntrictionParserTool {
    @Override
    public Constriction parseConstriction(Row row, int baseExcelColumn, DataHandler dataHandler){
        Exam exam1 = dataHandler.getExam((int) row.getCell(baseExcelColumn).getNumericCellValue());
        Exam exam2 = dataHandler.getExam((int) (row.getCell(baseExcelColumn + 1).getNumericCellValue()));
        List<Exam> sameDateExams = new ArrayList<>();
        sameDateExams.add(exam1); sameDateExams.add(exam2);


        return new SameDayConstriction(sameDateExams);
    }

    @Override
    public void writeConstriction(Constriction con, Row row, int baseExcelColumn) {
        SameDayConstriction sdc = (SameDayConstriction) con;
        int cellCounter = -1;
        Cell cell = row.createCell(baseExcelColumn + ++cellCounter);
        cell.setCellValue(sdc.getFirst().getId());

        cell = row.createCell(baseExcelColumn + ++cellCounter);
        cell.setCellValue(sdc.getSecond().getId());

        cell = row.createCell(baseExcelColumn + ++cellCounter);
        cell.setCellValue(sdc.getLastEvaluation());
    }
}
