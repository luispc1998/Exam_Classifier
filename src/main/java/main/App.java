package main;

import configuration.Configurer;
import domain.DataHandler;
import domain.entities.Exam;
import domain.entities.ExamDatesComparator;
import domain.parsers.ExamParser;
import fitnessFunctions.FitnessFunction;
import fitnessFunctions.greedyAlgorithm.CromosomeDecoder;
import fitnessFunctions.greedyAlgorithm.LinearFitnessFunction;
import geneticAlgorithm.Enconder;
import geneticAlgorithm.GeneticCore;
import geneticAlgorithm.Individual;
import geneticAlgorithm.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Collections.*;

public class App {

    public static void main(String[] args) throws IOException {

        String outputFileName = args[1];

        Configurer conf = new Configurer(args[0]);
        DataHandler dataHandler = new DataHandler(conf);
        CromosomeDecoder decoder = new CromosomeDecoder();
        Enconder basicEncoder = new Enconder();


        Individual individualPrime = basicEncoder.encodeListExams(dataHandler.getExams());
        FitnessFunction fn = new LinearFitnessFunction(dataHandler);
        GeneticCore genCore = new GeneticCore(individualPrime, 1000);


        Individual finalOne = genCore.geneticAlgorithm(0.15, fn, 300);



        List<Individual> finalPopulation = genCore.getPopulation();
        finalPopulation.sort(Comparator.comparingDouble(i -> i.getFitnessScore(fn)));

        HashSet<Individual> outputIndividuals = new HashSet<>();
        outputIndividuals.add(finalOne);

        getBestSchedules(finalPopulation, outputIndividuals, 3);

        String directory = createOuputDirectory(dataHandler.getConfigurer().getFilePaths("outputBaseDirectory"));

        int counter = 0;
        for (Individual idv: outputIndividuals) {
            decoder.decode(idv, dataHandler);
            List<Exam> finalResult = dataHandler.getClonedSchedule();
            dataHandler.resetScheduling();
            Comparator<Exam> examComparator = new ExamDatesComparator();
            finalResult.sort(examComparator);
            ExamParser.parseToExcel(finalResult, directory + "/" + outputFileName + "_" + counter + ".xlsx");
            counter++;
        }




    }


    public static String createOuputDirectory(String outputBaseDirectory) {

        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.now();

        StringBuilder directoryBuilder = new StringBuilder();
        directoryBuilder.append(outputBaseDirectory);
        directoryBuilder.append(ld.getYear());
        directoryBuilder.append(ld.getMonthValue());
        directoryBuilder.append(ld.getDayOfMonth());

        directoryBuilder.append("_");
        directoryBuilder.append(lt.getHour());
        directoryBuilder.append(lt.getMinute());

        File theDir = new File(directoryBuilder.toString());

        if (!theDir.exists()){
            theDir.mkdirs();
        }

        return directoryBuilder.toString();
    }

    public static void getBestSchedules(List<Individual> finalPopulation, HashSet<Individual> finalSet, int maxToTake) {
        int iterations = 0;
        for (Individual idv: finalPopulation) {
            iterations++;
            if (maxToTake == 0) {
                break;
            }
            if (! sadContains(finalSet, idv)) {
                finalSet.add(idv);
                maxToTake--;
            }
        }
    }

    public static boolean sadContains(Set<Individual> finalSet, Individual idv) {
        for (Individual fidv: finalSet) {
            if (fidv.equals(idv)) {
                return true;
            }
        }
        return false;
    }
}
