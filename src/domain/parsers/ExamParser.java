package domain.parsers;

import domain.entities.Exam;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class ExamParser {


    public static List<Exam> getExamsFromFile(String file) throws FileNotFoundException {

        BufferedReader reader = new BufferedReader(new FileReader(file));

        return null;
    }

}
