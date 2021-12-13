package domain.parsers;

import domain.constrictions.types.weakConstriction.hardifiableConstrictions.SameDayConstriction;
import domain.entities.Exam;
import utils.ConsoleLogger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
        int lineCounter = 0;
        int createdRounds = 0;

        ConsoleLogger.getConsoleLoggerInstance().logInfo("Parseando tandas...");

        while((line = reader.readLine()) != null ) {
            lineCounter++;
            ArrayList<Integer> ids = new ArrayList<>();
            for (String idString: line.split(",")) {
                try {
                    ids.add(Integer.parseInt(idString));
                } catch (NumberFormatException e) {
                    ConsoleLogger.getConsoleLoggerInstance().logError("Could not fully parse Round on line: "
                            + lineCounter + ". Got id: " + idString + ". Use int Ids. This id will be ignored.");
                }

            }

            List<Exam> round = exams.stream().filter((ex) -> ids.contains(ex.getId())).collect(Collectors.toList());
            if (round.size() != ids.size()) {
                ConsoleLogger.getConsoleLoggerInstance().logError("Nonexistent ids found for round: " + lineCounter +
                        ". Got round: " + ids + ". Ignoring round...");
                continue;
            }

            if (round.size() != 0) {
                setUpRound(round);
                createdRounds++;
            }


        }
        ConsoleLogger.getConsoleLoggerInstance().logInfo("Tandas creadas: " + createdRounds);
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
