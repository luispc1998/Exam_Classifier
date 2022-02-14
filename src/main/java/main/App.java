package main;

import domain.DataHandler;
import domain.entities.Exam;
import domain.parsers.ConstraintParser;
import domain.parsers.ExamParser;
import geneticAlgorithm.Enconder;
import geneticAlgorithm.GeneticCore;
import geneticAlgorithm.Individual;
import geneticAlgorithm.configuration.Configurer;
import geneticAlgorithm.fitnessFunctions.FitnessFunction;
import geneticAlgorithm.fitnessFunctions.LinearFitnessFunction;
import logger.dataGetter.fitnessLogger.GeneticLogger;
import geneticAlgorithm.operators.GeneticOperators;
import geneticAlgorithm.output.ExcelWriter;
import geneticAlgorithm.output.OutputHandler;
import greedyAlgorithm.ChromosomeDecoder;
import logger.ConsoleLogger;
import logger.ErrorManager;
import utils.Utils;
import logger.dataGetter.StatisticalDataGetter;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

/**
 * This is the main class to be compiled and executed.
 */
public class App {

    private static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {

        try {
            applicationCode(args);
        } catch (RuntimeException e) {
            System.out.println("Fatal error on the application.");
            System.out.println("Cause: " + e.getMessage());
            System.out.println("Application will end its execution.");
        }


    }

    private static void applicationCode(String[] args) {
        String outputFileName = args[1];
        StatisticalDataGetter statisticalDataGetter = null;


        Configurer conf = new Configurer(args[0]);
        ExamParser examParser = new ExamParser();
        ConstraintParser constraintParser;
        Enconder basicEncoder = new Enconder();

        int repetitions = conf.getGeneticParameters().getAlgorithmRepetitions();
        boolean errorAsking = conf.getGeneticParameters().isErrorAsking();

        if (args[2] != null) {
            statisticalDataGetter = new StatisticalDataGetter(args[2], Utils.createDirectoryStringBasedOnHour());
            constraintParser = new ConstraintParser(conf, statisticalDataGetter);
        }
        else {
            constraintParser = new ConstraintParser();
        }

        for (int j = 1; j <= repetitions; j++) {
            if (repetitions > 1) {
                System.out.println();
                System.out.println("Repetition: " + j);
            }
            // Iteration start
            ErrorManager errorManager = ConsoleLogger.getConsoleLoggerInstance().getErrorManager();
            List<Exam> exams = examParser.parseExams(conf.getFilePaths("inputFile"), conf);
            String outputDirectory = Utils.createOutputDirectory(conf.getFilePaths("outputBaseDirectory"));



            if (errorAsking && errorManager.wasThereErrorsOrWarnings()) {
                System.out.println("Se encontraron errores durante la generación de exámenes, por favor revise el archivo errorLog en la carpeta de salida.");
                ConsoleLogger.getConsoleLoggerInstance().writeInputLogData(outputDirectory);
                stoppingInputRequest();
            }

            DataHandler dataHandler = new DataHandler(conf, exams, constraintParser);
            ExcelWriter excelWriter = new ExcelWriter(examParser, constraintParser);
            OutputHandler outputHandler = new OutputHandler(dataHandler, outputFileName, outputDirectory, excelWriter);



            if (errorAsking && errorManager.wasThereErrorsOrWarnings()) {
                System.out.println("Se encontraron errores o avisos, por favor revise el archivo errorLog en la carpeta de salida.");
                ConsoleLogger.getConsoleLoggerInstance().writeInputLogData(outputDirectory);
                stoppingInputRequest();
            }


            Individual individualPrime = basicEncoder.encodeListExams(dataHandler);
            FitnessFunction fn = new LinearFitnessFunction(dataHandler);

            GeneticOperators geneticOperators = new GeneticOperators(conf.getGeneticParameters().getPopulationSize());
            GeneticLogger logger = new GeneticLogger();
            GeneticCore genCore = new GeneticCore(individualPrime, conf.getGeneticParameters().getPopulationSize(),
                    geneticOperators, logger);


            Individual finalOne = genCore.geneticAlgorithm(conf.getGeneticParameters().getMutationProbability(),
                    conf.getGeneticParameters().getCrossingProbability(), fn,
                    conf.getGeneticParameters().getGenerations(), conf.getGeneticParameters().getLoggingFrequency());


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

    private static void stoppingInputRequest() {
        System.out.print("Introduzca '0' para abortar la ejecución. Cierre los archivos de log y pulse ENTER para continuarla: ");

        String input = scan.nextLine();
        if (input.equals("0")) {
            System.out.println("La aplicación procederá a cerrarse.");
            System.exit(0);
        } else {
            System.out.println("La aplicación continuará con la ejecución...");
        }

    }


}
