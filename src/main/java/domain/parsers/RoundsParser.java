package domain.parsers;

import domain.constraints.types.softConstraints.userConstraints.SameDayConstraint;
import domain.entities.Exam;
import logger.ConsoleLogger;

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


    /**
     * Creates a round.
     *
     * <p>
     * It does not create a round in case there is just one exam with an identifier.
     * @param roundsMap A map containing the round identifiers as keys and a list of exam ids with that round id.
     * @param exams Lista de exámenes.
     */
    public void createRoundIfNecessary(HashMap<String, List<Integer>> roundsMap, List<Exam> exams) {
        int roundCounter = 0;
        for(List<Integer> ids : roundsMap.values()) {
            if (ids.size() > 1) {
                List<Exam> round = exams.stream().filter((ex) -> ids.contains(ex.getId())).collect(Collectors.toList());
                setUpRound(round);
                roundCounter++;
            }
        }
        ConsoleLogger.getConsoleLoggerInstance().logInfo("Tardas parseadas: " + roundCounter);
    }



    /**
     * Creates a round, sets the hard constraints to the exams. Configures the rounds on the corresponding exams.
     * @param round The list of {@code Exam} that conforms the round.
     */
    private void setUpRound(List<Exam> round) {
        for (int i = 0; i < round.size(); i++) {
            round.get(i).addRound(round);
            for (int j = i+1; j < round.size(); j++) {
                SameDayConstraint sameDayConstraint = new SameDayConstraint(round.get(i), round.get(j));
                sameDayConstraint.hardify();
            }
        }
    }
}
