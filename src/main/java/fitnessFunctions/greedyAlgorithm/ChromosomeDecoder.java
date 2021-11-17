package fitnessFunctions.greedyAlgorithm;

import configuration.DateTimeConfigurer;
import domain.DataHandler;
import domain.entities.Exam;
import geneticAlgorithm.Individual;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * This class takes a
 */
public class ChromosomeDecoder {

    public List<Exam> getExamsOrderedForChromosome(List<Integer> chromosome, DataHandler dataHandler){
        List<Exam> exams = new ArrayList<>();

        for (Integer index: chromosome) {
            exams.add(dataHandler.getExam(index));
        }

        return exams;
    }
/* Old approach. Sequential.

    public void decode2(Individual individual, DataHandler dataHandler){

        DateTimeConfigurer dateTimeConfigurer = dataHandler.getConfigurer().getDateTimeConfigurer();

        // Counters
        LocalTime currentHour = dateTimeConfigurer.getDayInitialHour();
        LocalDate currentDate;
        Exam exam;

        List<LocalDate> dates = dateTimeConfigurer.getExamDates();
        Iterator<LocalDate> datesIterator = dates.listIterator();

        List<Integer> chromosome = individual.getChromosome();

        List<Exam> exams = getExamsOrderedForChromosome(chromosome, dataHandler);
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
                    // tod, what happens if i run out of days ?
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
*/

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
        Set<LocalDate> viableDays;
        Iterator<LocalDate> viableDaysIterator;

        // Dates
        List<LocalDate> dates = dateTimeConfigurer.getExamDates();
        int indexDate = -1;

        // Times for the days.
        HashMap<LocalDate, LocalTime> daysTimes = initializeDays(dateTimeConfigurer);

        // Exams to schedule
        List<Integer> cromosome = individual.getChromosome();
        List<Exam> exams = getExamsOrderedForChromosome(cromosome, dataHandler);
        Iterator<Exam> examsIterator = exams.listIterator();

        // Fin de la declaración de variables.

        // Miramos que tengamos días y exámenes para clasificar o acabamos.


        // if (examsIterator.hasNext() && datesIterator.hasNext()){
        // Si tengo exámenes para clasificar, y tengo días para ponerlos.
        if (examsIterator.hasNext() && indexDate < dates.size() -1){
            // Inicializamos cosas. Primer examen, primera fecha.
            exam = examsIterator.next();

            //Miramos qué día se puede poner el examen según sus restricciones duras.
            viableDays = exam.getViableDays(daysTimes);





            //Si no se pueden poner ningún día, saltamos el examen. (No hay reparación, estamos al principio)
            while (viableDays.size() == 0){
                //Encontramos un examen que se pueda poner.
                if (! examsIterator.hasNext()){
                    return; // Se acabaron los exámenes
                }
                exam = examsIterator.next();
                viableDays = exam.getViableDays(daysTimes);

            }

            viableDaysIterator = viableDays.iterator(); // Iteramos los días que se puede poner ese examen.
            currentDate = viableDaysIterator.next(); //dates.get(++indexDate); //datesIterator.next();
        }
        else {
            return;
        }

        do {
            // Buscamos el primer día en el que se pueda poner el examen, iterando la lista.
            // Si los pasamos todos, paso al siguiente examen y vuelvo a empezar.
            // Se se me acaban los exámenes, fin.
            //int allDayCheck = 0;


            currentHour = daysTimes.get(currentDate);


            while (! dateTimeConfigurer.isValidEndingHourFor(currentHour, exam.getDuration(), exam.getExtraTime())) {

                // indexDate = updateIndex(indexDate, dates.size());
                if (viableDaysIterator.hasNext()) { //Me quedan más días viables para el examen
                    currentDate = viableDaysIterator.next();
                    currentHour = daysTimes.get(currentDate);
                }
                else{ // No me quedan días para el examen

                    // Todo, posible algoritmo de reparación aquí.

                    if (examsIterator.hasNext()){ // Voy a mirar si tengo más exámenes.
                        exam = examsIterator.next();
                        viableDays = exam.getViableDays(daysTimes);

                        while (viableDays.size() == 0){
                            // Todo, posible alritmo de reparación ?

                            //Encontramos un examen que se pueda poner.
                            if (! examsIterator.hasNext()){
                                return; // Se acabaron los exámenes
                            }
                            exam = examsIterator.next();
                            viableDays = exam.getViableDays(daysTimes);

                        }
                        // Tengo un examen que puedo intentar poner.
                        viableDaysIterator = viableDays.iterator();
                        currentDate = viableDaysIterator.next();
                        currentHour = daysTimes.get(currentDate);

                    }
                    else{
                        // No hemos podido encontrar más exámenes
                        return; // Aquí se acaba.
                    }
                }
                /*
                currentDate = dates.get(indexDate);
                currentHour = daysTimes.get(currentDate); // Tod, tenía el inicio del día aquí. ¿Bug? Sí.
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

                 */
            }


            if (dateTimeConfigurer.isHourInProhibitedInterval(currentHour)){
                daysTimes.put(currentDate, dateTimeConfigurer.getFinishingHourProhibitedInterval());
                continue;
                // No se pueden iniciar exámenes en el intervalo definido, por tanto ponemos la de final para este día
                // Pasamos de nuevo arriba para ver si la hora de finalización es válida.
            }

            Exam collidingExam = dataHandler.checkCollisionOf(currentDate, currentHour, exam.getDuration(), exam.getExtraTime());
            // Sí. Lo clasifico
            // No. Necesito la hora de finalización del otro examen. Y pruebo con esa.


            if (collidingExam == null) {
                dataHandler.schedule(exam, currentDate, currentHour);
                if (! examsIterator.hasNext()){ // si no quedan exámenes por revisar, acabamos
                    return;
                }
                else{

                    daysTimes.put(currentDate, exam.getFinishingHour()); // Ponemos la hora a la que acaba.

                    exam = examsIterator.next();
                    viableDays = exam.getViableDays(daysTimes);

                    while (viableDays.size() == 0){
                        // Todo, posible alritmo de reparación ?

                        //Encontramos un examen que se pueda poner.
                        if (! examsIterator.hasNext()){
                            return; // Se acabaron los exámenes
                        }
                        exam = examsIterator.next();
                        viableDays = exam.getViableDays(daysTimes);

                    }

                    viableDaysIterator = viableDays.iterator();
                    currentDate = viableDaysIterator.next();
                    //currentHour = daysTimes.get(currentDate); // No es necesario por principio de iteración del dowhile.

                    //indexDate = updateIndex(indexDate, dates.size());
                    //currentDate = dates.get(indexDate);

                }

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



















    public void decodeNew(Individual individual, DataHandler dataHandler){

        DateTimeConfigurer dateTimeConfigurer = dataHandler.getConfigurer().getDateTimeConfigurer();

        // Counters
        LocalTime currentHour;
        LocalDate currentDate;
        //Exam exam;
        Set<LocalDate> viableDays;


        // Dates
        List<LocalDate> dates = dateTimeConfigurer.getExamDates();
        int indexDate = -1;

        // Times for the days.
        HashMap<LocalDate, LocalTime> daysTimes = initializeDays(dateTimeConfigurer);

        // Exams to schedule
        List<Integer> cromosome = individual.getChromosome();
        List<Exam> exams = getExamsOrderedForChromosome(cromosome, dataHandler);


        // Fin de la declaración de variables.


        // Miramos que tengamos días y exámenes para clasificar o acabamos.


        // if (examsIterator.hasNext() && datesIterator.hasNext()){
        // Si tengo exámenes para clasificar, y tengo días para ponerlos.

        for(Exam exam : exams) {
            viableDays = exam.getViableDays(daysTimes);
            boolean scheduled = false;

            for (LocalDate day :viableDays){
                currentHour = daysTimes.get(day);

                Exam collidingExam = null;
                while (dateTimeConfigurer.isHourInProhibitedInterval(currentHour) ||
                        (collidingExam = dataHandler.checkCollisionOf(day, currentHour, exam.getDuration(), exam.getExtraTime())) != null) {

                    if (dateTimeConfigurer.isHourInProhibitedInterval(currentHour)){
                        daysTimes.put(day, dateTimeConfigurer.getFinishingHourProhibitedInterval());
                    }

                    if(collidingExam != null){
                        daysTimes.put(day, collidingExam.getFinishingHour());
                    }
                    currentHour = daysTimes.get(day);
                }


                if (dateTimeConfigurer.isValidEndingHourFor(currentHour, exam.getDuration(), exam.getExtraTime())){
                    dataHandler.schedule(exam, day, currentHour);
                    daysTimes.put(day, exam.getFinishingHour());
                    scheduled = true;
                    break;
                }
            }

            if (!scheduled) {
                // reparación

            }
        }


    }























}
