package fitnessFunctions.greedyAlgorithm;

import domain.DataHandler;
import domain.entities.Exam;
import geneticAlgorithm.Individual;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.List;

public class CromosomeDecoder {


    public void decode(Individual individual, DataHandler dataHandler){

        // Counters
        LocalTime currentHour = LocalTime.of(9,0);
        LocalDate currentDate = null;
        Exam exam;

        List<LocalDate> dates = dataHandler.getDates();
        Iterator<LocalDate> datesIterator = dates.listIterator();

        List<Exam> exams = dataHandler.getExams();
        Iterator<Exam> examsIterator = exams.listIterator();

        if (examsIterator.hasNext()){
            exam = examsIterator.next();
        }
        else {
            return;
        }

        do {

            //check de hora y duración sobre hora máxima de finalización.
            if (! dataHandler.isValidEndingHourFor(currentHour, exam.getDuration())){
                if (datesIterator.hasNext()){
                    currentDate = datesIterator.next();
                    currentHour = LocalTime.of(9,0);
                }
                else {
                    // TODO, what happens if i run out of days ?
                }
            }

            if (dataHandler.isHourInProhibitedInterval(currentHour)){
                currentHour = dataHandler.getFinishingHourProhibitedInterval();
            }


            Exam collidingExam = dataHandler.checkColisionOf(currentDate, currentHour, exam.getDuration());
            // Sí. Lo clasifico
            // No. Necesito la hora de finalización del otro examen. Y pruebo con esa.
            if (collidingExam == null) {
                dataHandler.schedule(exam, currentDate, currentHour);
                exam = examsIterator.next();
            }
            else{
                currentHour = collidingExam.getFinishingHour();
            }

        } while(examsIterator.hasNext());
        

      
    }

}
