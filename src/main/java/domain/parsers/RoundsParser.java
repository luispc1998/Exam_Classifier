package domain.parsers;

import domain.constrictions.types.weakConstriction.hardifiableConstrictions.SameDayConstriction;
import domain.entities.Exam;
import utils.ConsoleLogger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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


    public static void createRoundIfNecessary(HashMap<String, List<Integer>> roundsMap, List<Exam> exams) {
        int roundCounter = 0;
        for(List<Integer> ids : roundsMap.values()) {
            if (ids.size() > 1) {
                List<Exam> round = exams.stream().filter((ex) -> ids.contains(ex.getId())).collect(Collectors.toList());
                setUpRound(round);
                roundCounter++;
            }
        }
        ConsoleLogger.getConsoleLoggerInstance().logInfo("Rounds parsed: " + roundCounter);
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
