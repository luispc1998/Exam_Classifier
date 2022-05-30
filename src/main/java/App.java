import domain.ExamsSchedule;
import domain.entities.Exam;
import domain.parsers.ConstraintParser;
import domain.parsers.ExamParser;
import geneticAlgorithm.Enconder;
import geneticAlgorithm.GeneticCore;
import geneticAlgorithm.Individual;
import domain.configuration.Configurer;
import geneticAlgorithm.fitnessFunctions.FitnessFunction;
import geneticAlgorithm.fitnessFunctions.LinearFitnessFunction;
import geneticAlgorithm.operators.GeneticOperators;
import geneticAlgorithm.output.ExcelWriter;
import geneticAlgorithm.output.OutputHandler;
import greedyAlgorithm.ChromosomeDecoder;
import logger.ConsoleLogger;
import logger.ErrorManager;
import logger.dataGetter.StatisticalDataGetter;
import logger.dataGetter.fitnessLogger.GeneticLogger;
import utils.Utils;

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

        ConstraintParser constraintParser;
        Enconder basicEncoder = new Enconder();

        int repetitions = conf.getGeneticParameters().getAlgorithmRepetitions();
        if (repetitions<1) {
            repetitions = 1;
        }
        boolean errorAsking = conf.getGeneticParameters().isErrorAsking();



        HashSet<Individual> elite = new HashSet<>();
        Individual bestFitnessIndividual;
        ExamsSchedule examsSchedule = null;
        GeneticCore genCore = null;
        OutputHandler outputHandler = null;
        String outputDirectory = Utils.createOutputDirectory(conf.getFilePaths("outputBaseDirectory"));

        if (args[2] != null) {
            statisticalDataGetter = new StatisticalDataGetter(args[2], outputDirectory);
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
            ExamParser examParser = new ExamParser();
            ErrorManager errorManager = ConsoleLogger.getConsoleLoggerInstance().getErrorManager();
            List<Exam> exams = examParser.parseExams(conf.getFilePaths("inputFile"), conf);



            if (errorAsking && errorManager.wasThereNewErrorsOrWarnings() && j==1) {
                System.out.println("Se encontraron errores durante la generación de exámenes.");
                ConsoleLogger.getConsoleLoggerInstance().writeInputLogData(outputDirectory);
                stoppingInputRequest();
            }

            examsSchedule = new ExamsSchedule(conf, exams, constraintParser);
            ExcelWriter excelWriter = new ExcelWriter(examParser, constraintParser);
            outputHandler = new OutputHandler(examsSchedule, outputFileName, outputDirectory, excelWriter);



            if (errorAsking && errorManager.wasThereNewErrorsOrWarnings() && j==1) {
                System.out.println("Se encontraron errores o avisos durante el parseo de restricciones.");
                ConsoleLogger.getConsoleLoggerInstance().writeInputLogData(outputDirectory);
                stoppingInputRequest();
            }


            if (j == 1) {
                System.out.println(conf.getGeneticParameters().getStatusMessage());
            }

            Individual individualPrime = basicEncoder.encodeListExams(examsSchedule);
            FitnessFunction fn = new LinearFitnessFunction(examsSchedule);

            GeneticOperators geneticOperators = new GeneticOperators(conf.getGeneticParameters().getPopulationSize());
            GeneticLogger logger = new GeneticLogger();
            genCore = new GeneticCore(individualPrime, conf.getGeneticParameters().getPopulationSize(),
                    geneticOperators, logger, elite);


            bestFitnessIndividual = genCore.geneticAlgorithm(conf.getGeneticParameters().getMutationProbability(),
                    conf.getGeneticParameters().getCrossoverProbability(), fn,
                    conf.getGeneticParameters().getGenerations(), conf.getGeneticParameters().getLoggingFrequency());


            List<Individual> finalPopulation = genCore.getPopulation();
            finalPopulation.sort(Comparator.comparingDouble(i -> i.getFitnessScore(fn)));
            elite.clear();
            elite.add(bestFitnessIndividual);

            Utils.getBestSchedules(finalPopulation, elite, conf.getGeneticParameters().getMaxSchedulesToTake());
            ConsoleLogger.getConsoleLoggerInstance().writeInputLogData(outputDirectory);


        }

        if (statisticalDataGetter != null) {
            statisticalDataGetter.writeLogFor(elite, new ChromosomeDecoder(conf), examsSchedule, outputFileName);
        }
        outputHandler.writeOutputFiles(elite, genCore.getLogging(), genCore.getFitnessGraphData());
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
