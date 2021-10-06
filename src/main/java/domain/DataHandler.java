package domain;

import domain.entities.Exam;
import domain.parsers.ConstrictionParser;
import domain.parsers.ExamParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataHandler {

    private ExamParser exParser;
    private List<Exam> exams;
    private ConstrictionParser conParser;

    public DataHandler() throws IOException {

        this.exams = ExamParser.parseExams("files/v6 (junio-julio).xlsx");

        // ConstrictionParser.parseConstrictions("", this);

    }

    public List<Exam> getExams(){
        return new ArrayList<>(exams);
    }
    public Exam getExam(String code){
        for (Exam exam: exams) {
            if (exam.getCode().equals(code)){
                return exam;
            }
        }
        return null;
    }



}
