package main;

import configuration.Configurer;
import domain.DataHandler;
import domain.parsers.ConstrictionParser;
import domain.parsers.ExamParser;
import fitnessFunctions.FitnessFunction;
import fitnessFunctions.greedyAlgorithm.LinearFitnessFunction;
import geneticAlgorithm.Enconder;
import geneticAlgorithm.GeneticCore;
import geneticAlgorithm.Individual;
import geneticAlgorithm.output.ExcelWriter;
import geneticAlgorithm.output.OutputHandler;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class App {

    public static void main(String[] args) {

        String outputFileName = args[1];



        Configurer conf = new Configurer(args[0]);
        ExamParser examParser = new ExamParser();
        ConstrictionParser constrictionParser = new ConstrictionParser();
        DataHandler dataHandler = new DataHandler(conf, examParser, constrictionParser);


        Enconder basicEncoder = new Enconder();


        Individual individualPrime = basicEncoder.encodeListExams(dataHandler);
        FitnessFunction fn = new LinearFitnessFunction(dataHandler);
        GeneticCore genCore = new GeneticCore(individualPrime, conf.getGeneticParameters().getPopulationSize());


        Individual finalOne = genCore.geneticAlgorithm(conf.getGeneticParameters().getMutationProbability(),
                conf.getGeneticParameters().getCrossingProbability(), fn,
                conf.getGeneticParameters().getMaxIterations(), conf.getGeneticParameters().getLoggingFrequency());



        List<Individual> finalPopulation = genCore.getPopulation();
        finalPopulation.sort(Comparator.comparingDouble(i -> i.getFitnessScore(fn)));

        HashSet<Individual> outputIndividuals = new HashSet<>();
        outputIndividuals.add(finalOne);

        getBestSchedules(finalPopulation, outputIndividuals, conf.getGeneticParameters().getMaxSchedulesToTake());

        ExcelWriter excelWriter = new ExcelWriter(examParser, constrictionParser);
        OutputHandler outputHandler = new OutputHandler(outputIndividuals, dataHandler, outputFileName, genCore.getLogging(),
                excelWriter);

        outputHandler.writeOutputFiles();


    }




    public static void getBestSchedules(List<Individual> finalPopulation, HashSet<Individual> finalSet, int maxToTake) {
        for (Individual idv: finalPopulation) {
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
