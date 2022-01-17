package main;

import dataGetter.StatisticalDataGetter;
import domain.DataHandler;
import domain.parsers.ConstrictionParser;
import domain.parsers.ExamParser;
import geneticAlgorithm.Enconder;
import geneticAlgorithm.GeneticCore;
import geneticAlgorithm.Individual;
import geneticAlgorithm.configuration.Configurer;
import geneticAlgorithm.fitnessFunctions.FitnessFunction;
import geneticAlgorithm.fitnessFunctions.greedyAlgorithm.ChromosomeDecoder;
import geneticAlgorithm.fitnessFunctions.greedyAlgorithm.LinearFitnessFunction;
import geneticAlgorithm.logger.GeneticLogger;
import geneticAlgorithm.operators.GeneticOperators;
import geneticAlgorithm.output.ExcelWriter;
import geneticAlgorithm.output.OutputHandler;
import utils.ConsoleLogger;
import utils.ErrorManager;
import utils.Utils;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

/**
 * This is the main class to be compiled and executed.
 */
public class App {

    public static void main(String[] args) {

        String outputFileName = args[1];
        StatisticalDataGetter statisticalDataGetter = null;


        Configurer conf = new Configurer(args[0]);
        ExamParser examParser = new ExamParser();
        ConstrictionParser constrictionParser = new ConstrictionParser();
        Enconder basicEncoder = new Enconder();

        int repetitions = conf.getGeneticParameters().getAlgorithmRepetitions();
        boolean errorAsking = conf.getGeneticParameters().isErrorAsking();

        if (args[2] != null) {
            statisticalDataGetter = new StatisticalDataGetter(args[2], Utils.createDirectoryStringBasedOnHour());
        }

        for (int j = 1; j <= repetitions; j++) {
            if (repetitions > 1) {
                System.out.println();
                System.out.println("Repetition: " + j);
            }
            // Iteration start
            DataHandler dataHandler = new DataHandler(conf, examParser, constrictionParser);
            ExcelWriter excelWriter = new ExcelWriter(examParser, constrictionParser);
            OutputHandler outputHandler = new OutputHandler(dataHandler, outputFileName, excelWriter);

            outputHandler.writeInputLogData();

            ErrorManager errorManager = ConsoleLogger.getConsoleLoggerInstance().getErrorManager();


            if (errorAsking && errorManager.wasThereErrorsOrWarnigns()) {
                System.out.println("Se encontraron errores o avisos, por favor revise el archivo errorLog en la carpeta de salida.");
                System.out.print("Introduzca '0' para abortar la ejecución o cualquier otro input para continuarla: ");
                try (Scanner sc = new Scanner(System.in)) {
                    String input = sc.nextLine();
                    if (input.equals("0")) {
                        System.out.println("La aplicación procederá a cerrarse.");
                        System.exit(0);
                    } else {
                        System.out.println("La aplicación continuará con la ejecución...");
                    }
                }
            }


            Individual individualPrime = basicEncoder.encodeListExams(dataHandler);
            FitnessFunction fn = new LinearFitnessFunction(dataHandler);

            GeneticOperators geneticOperators = new GeneticOperators(conf.getGeneticParameters().getPopulationSize());
            GeneticLogger logger = new GeneticLogger();
            GeneticCore genCore = new GeneticCore(individualPrime, conf.getGeneticParameters().getPopulationSize(),
                    geneticOperators, logger);


            Individual finalOne = genCore.geneticAlgorithm(conf.getGeneticParameters().getMutationProbability(),
                    conf.getGeneticParameters().getCrossingProbability(), fn,
                    conf.getGeneticParameters().getMaxIterations(), conf.getGeneticParameters().getLoggingFrequency(),
                    statisticalDataGetter);


            List<Individual> finalPopulation = genCore.getPopulation();
            finalPopulation.sort(Comparator.comparingDouble(i -> i.getFitnessScore(fn)));

            HashSet<Individual> outputIndividuals = new HashSet<>();
            outputIndividuals.add(finalOne);

            Utils.getBestSchedules(finalPopulation, outputIndividuals, conf.getGeneticParameters().getMaxSchedulesToTake());


            if (statisticalDataGetter != null) {
                statisticalDataGetter.writeLogFor(finalOne, new ChromosomeDecoder(conf), dataHandler);
            }
            outputHandler.writeOutputFiles(outputIndividuals, genCore.getLogging(), genCore.getFitnessGraphData());
        }



    }



}
