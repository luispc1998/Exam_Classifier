package geneticAlgorithm;

import domain.DataHandler;
import domain.entities.Exam;

import java.util.ArrayList;
import java.util.List;

/**
 * This is in charge of enconding the cromosomes to build the {@link Individual} objects.
 *
 * <p>
 * It ignores the already scheduled exams since they are fixed to a date, and therefore they
 * cannot be assigned any other.
 */
public class Enconder {

    /**
     * Encodes the list of exams to a chromosome.
     * @param dataHandler the {@code DataHandler} instance with the data of the problem.
     * @return An individual with the positions of the exams in the list.
     */
    public Individual encodeListExams(DataHandler dataHandler){
        List<Integer> indexes = new ArrayList<>();
        for (Exam exam: dataHandler.getPreUnscheduledExams()) {
                indexes.add(dataHandler.getIndexOfExam(exam));
        }
        return new Individual(indexes);
    }


}
