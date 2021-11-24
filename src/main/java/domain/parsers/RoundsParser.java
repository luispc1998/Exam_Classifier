package domain.parsers;

import domain.constrictions.types.weakConstriction.hardifiableConstrictions.SameDayConstriction;
import domain.entities.Exam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This parses the rounds file in which the exam rounds are specified.
 *
 * <p>
 * An exam round is a group of exams that must take place on the same day. They are in fact the same exams which is held
 * multiple times due to a large amount of students.
 *
 * <p>
 * Therefore, an exam can only belong to one round.
 */
public class RoundsParser {


    /**
     * Parses the rounds and returns them.
     * @param filepath The filepath to the rounds file
     * @param exams The list of parsed exams
     * @throws IOException In case there is a problem with the file.
     */
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

    /**
     * Creates a round, sets the hard constrictions to the exams. Configures the rounds on the corresponding exams.
     * @param round The list of {@code Exam} that conforms the round.
     */
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
