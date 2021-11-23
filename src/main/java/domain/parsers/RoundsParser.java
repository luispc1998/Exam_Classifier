package domain.parsers;

import domain.DataHandler;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.SameDayConstriction;
import domain.entities.Exam;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RoundsParser {


    public static void parseRounds(String filepath, List<Exam> exams) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        String line;
        while((line = reader.readLine()) != null ) {
            ArrayList<Integer> ids = new ArrayList<>();
            for (String idString: line.split(",")) {
                ids.add(Integer.parseInt(idString));
            }
            List<Exam> round = exams.stream().filter((ex) -> ids.contains(ex.getId())).collect(Collectors.toList());
            setUpRound(round);
        }
    }

    private static void setUpRound(List<Exam> round) {
        for (int i = 0; i < round.size(); i++) {
            round.get(i).addRound(round);
            for (int j = i+1; j < round.size(); j++) {
                SameDayConstriction sameDayConstriction = new SameDayConstriction(round.get(i), round.get(j));
                sameDayConstriction.hardify();
            }
        }
    }
}
