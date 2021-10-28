package fitnessFunctions.greedyAlgorithm;

import configuration.DateTimeConfigurer;
import domain.DataHandler;
import domain.entities.Exam;
import geneticAlgorithm.Individual;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * This class takes a
 */
public class CromosomeDecoder {

    public List<Exam> getExamsOrderedForCromosome(List<Integer> cromosome, DataHandler dataHandler){
        List<Exam> exams = new ArrayList<>();

        for (Integer index: cromosome) {
            exams.add(dataHandler.getExam(index));
        }

        return exams;
    }

    public void decode2(Individual individual, DataHandler dataHandler){

        DateTimeConfigurer dateTimeConfigurer = dataHandler.getConfigurer().getDateTimeConfigurer();

        // Counters
        LocalTime currentHour = dateTimeConfigurer.getDayInitialHour();
        LocalDate currentDate;
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


    private HashMap<LocalDate, LocalTime> initializeDays(DateTimeConfigurer dateTimeConfigurer) {
        HashMap<LocalDate, LocalTime> daysTimes = new HashMap<>();
        for (LocalDate date :dateTimeConfigurer.getExamDates()) {
            daysTimes.put(date, dateTimeConfigurer.getDayInitialHour());
        }
        return daysTimes;
    }


    public void decode(Individual individual, DataHandler dataHandler){

        DateTimeConfigurer dateTimeConfigurer = dataHandler.getConfigurer().getDateTimeConfigurer();

        // Counters
        LocalTime currentHour;
        LocalDate currentDate;
        Exam exam;

        // Dates
        List<LocalDate> dates = dateTimeConfigurer.getExamDates();
        int indexDate = -1;

        // Times for the days.
        HashMap<LocalDate, LocalTime> daysTimes = initializeDays(dateTimeConfigurer);

        // Exams to schedule
        List<Integer> cromosome = individual.getCromosome();
        List<Exam> exams = getExamsOrderedForCromosome(cromosome, dataHandler);
        Iterator<Exam> examsIterator = exams.listIterator();

        // Fin de la declaración de variables.


        //TODO Ordenar las fechas por si no lo están

        // Miramos que tengamos días y exámenes para clasificar o acabamos.


        // if (examsIterator.hasNext() && datesIterator.hasNext()){
        if (examsIterator.hasNext() && indexDate < dates.size() -1){
            // Inicializamos cosas. Primer examen, primera fecha.
            exam = examsIterator.next();
            currentDate = dates.get(++indexDate); //datesIterator.next();
        }
        else {
            return;
        }

        do {
            // Buscamos el primer día en el que se pueda poner el examen, iterando la lista.
            // Si los pasamos todos, paso al siguiente examen y vuelvo a empezar.
            // Se se me acaban los exámenes, fin.
            int allDayCheck = 0;
            currentHour = daysTimes.get(currentDate);
            while (! dateTimeConfigurer.isValidEndingHourFor(currentHour, exam.getDuration())) {
                indexDate = updateIndex(indexDate, dates.size());
                currentDate = dates.get(indexDate);
                currentHour = dateTimeConfigurer.getDayInitialHour();
                allDayCheck++;

                if (allDayCheck == dates.size()) {
                    // Hemos mirado todos los días y no hemos encontado ninguno válido para el examen
                    // Tratamos de pasar al siguiente examen

                    if (examsIterator.hasNext()){
                        exam = examsIterator.next();
                        allDayCheck = 0;
                    }
                    else{
                        // No hemos podido encontrar más exámenes
                        return; // Aquí se acaba.
                    }
                }
            }


            if (dateTimeConfigurer.isHourInProhibitedInterval(currentHour)){
                daysTimes.put(currentDate, dateTimeConfigurer.getFinishingHourProhibitedInterval());
                continue;
                // No se pueden iniciar exámenes en el intervalo definido, por tanto ponemos la de final para este día
                // Pasamos de nuevo arriba para ver si la hora de finalización es válida.
            }

            Exam collidingExam = dataHandler.checkColisionOf(currentDate, currentHour, exam.getDuration());
            // Sí. Lo clasifico
            // No. Necesito la hora de finalización del otro examen. Y pruebo con esa.


            if (collidingExam == null) {
                dataHandler.schedule(exam, currentDate, currentHour);
                if (! examsIterator.hasNext()){
                    break;
                }
                daysTimes.put(currentDate, exam.getFinishingHour());
                indexDate = updateIndex(indexDate, dates.size());
                currentDate = dates.get(indexDate);
                exam = examsIterator.next();

            }
            else{
                daysTimes.put(currentDate, collidingExam.getFinishingHour());
            }

        } while(true);

    }

    private int updateIndex(int index, int limit) {
        if (index + 1 == limit){
            return 0;
        }
        else{
            return ++index;
        }
    }




}
