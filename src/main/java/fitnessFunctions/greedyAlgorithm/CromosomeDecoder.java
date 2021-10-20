package fitnessFunctions.greedyAlgorithm;

import configuration.DateTimeConfigurer;
import domain.DataHandler;
import domain.entities.Exam;
import geneticAlgorithm.Individual;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CromosomeDecoder {

    public List<Exam> getExamsOrderedForCromosome(List<Integer> cromosome, DataHandler dataHandler){
        List<Exam> exams = new ArrayList<>();

        for (Integer index: cromosome) {
            exams.add(dataHandler.getExam(index));
        }

        return exams;
    }

    public void decode(Individual individual, DataHandler dataHandler){

        DateTimeConfigurer dateTimeConfigurer = dataHandler.getConfigurer().getDateTimeConfigurer();

        // Counters
        LocalTime currentHour = dateTimeConfigurer.getDayInitialHour();
        LocalDate currentDate = null;
        Exam exam;

        List<LocalDate> dates = dateTimeConfigurer.getExamDates();
        Iterator<LocalDate> datesIterator = dates.listIterator();

        List<Integer> cromosome = individual.getCromosome();

        List<Exam> exams = getExamsOrderedForCromosome(cromosome, dataHandler);
        Iterator<Exam> examsIterator = exams.listIterator();

        if (examsIterator.hasNext() && datesIterator.hasNext()){
            exam = examsIterator.next();
            currentDate = datesIterator.next();
        }
        else {
            return;
        }
        int infiniteCounter = 0;
        do {
            infiniteCounter++;
            //check de hora y duración sobre hora máxima de finalización.
            if (! dateTimeConfigurer.isValidEndingHourFor(currentHour, exam.getDuration())){
                if (datesIterator.hasNext()){
                    currentDate = datesIterator.next();
                    currentHour = dateTimeConfigurer.getDayInitialHour();
                }
                else {
                    break;
                    // TODO, what happens if i run out of days ?
                    // I can do nothing, for now. Maybe some fixes later.
                }
            }

            if (dateTimeConfigurer.isHourInProhibitedInterval(currentHour)){
                currentHour = dateTimeConfigurer.getFinishingHourProhibitedInterval();
            }

            Exam collidingExam = dataHandler.checkColisionOf(currentDate, currentHour, exam.getDuration());
            // Sí. Lo clasifico
            // No. Necesito la hora de finalización del otro examen. Y pruebo con esa.
            if (collidingExam == null) {
                dataHandler.schedule(exam, currentDate, currentHour);
                currentHour = exam.getFinishingHour();
                if (! examsIterator.hasNext()){
                    break;
                }
                exam = examsIterator.next();

            }
            else{
                currentHour = collidingExam.getFinishingHour();
            }

        } while(true);
      
    }

}
