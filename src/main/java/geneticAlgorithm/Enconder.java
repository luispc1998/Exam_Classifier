package geneticAlgorithm;

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
     * Encodes the list of exams to a cromosome
     * @param exams The list of {@code Exam} to be scheduled
     * @return An individual with the positions of the exams in the list.
     */
    public Individual encodeListExams(List<Exam> exams){
        List<Integer> indexes = new ArrayList<>();
        int indexCounter = 0;
        for (Exam exam: exams) {
            if (! exam.isScheduled()){
                indexes.add(indexCounter);
            }
            indexCounter++;
        }
        return new Individual(indexes);
    }


}
