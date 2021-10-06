package geneticAlgorithm;

import domain.entities.Exam;

import java.util.ArrayList;
import java.util.List;

public class Enconder {


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
