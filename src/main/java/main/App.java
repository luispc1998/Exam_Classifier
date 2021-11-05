package main;

import configuration.Configurer;
import domain.DataHandler;
import domain.constrictions.Constriction;
import domain.entities.Exam;
import domain.entities.ExamDatesComparator;
import domain.parsers.ConstrictionParser;
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
import java.util.*;

import static java.util.Collections.*;

public class App {

    public static void main(String[] args) throws IOException {

        String outputFileName = args[1];

        Configurer conf = new Configurer(args[0]);
        DataHandler dataHandler = new DataHandler(conf);
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


        ExcelWriter.excelWrite(outputIndividuals, dataHandler, outputFileName);



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
