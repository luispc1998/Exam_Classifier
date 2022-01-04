package main;

import geneticAlgorithm.configuration.Configurer;
import domain.DataHandler;
import domain.parsers.ConstrictionParser;
import domain.parsers.ExamParser;
import geneticAlgorithm.fitnessFunctions.FitnessFunction;
import geneticAlgorithm.fitnessFunctions.greedyAlgorithm.LinearFitnessFunction;
import geneticAlgorithm.Enconder;
import geneticAlgorithm.GeneticCore;
import geneticAlgorithm.Individual;
import geneticAlgorithm.operators.GeneticOperators;
import geneticAlgorithm.output.ExcelWriter;
import geneticAlgorithm.output.OutputHandler;
import utils.ConsoleLogger;
import utils.ErrorManager;

import java.util.*;

public class App {

    public static void main(String[] args) {

        String outputFileName = args[1];



        Configurer conf = new Configurer(args[0]);
        ExamParser examParser = new ExamParser();
        ConstrictionParser constrictionParser = new ConstrictionParser();
        DataHandler dataHandler = new DataHandler(conf, examParser, constrictionParser);

        ExcelWriter excelWriter = new ExcelWriter(examParser, constrictionParser);
        OutputHandler outputHandler = new OutputHandler(dataHandler, outputFileName, excelWriter);

        outputHandler.writeInputLogData();

        ErrorManager errorManager = ConsoleLogger.getConsoleLoggerInstance().getErrorManager();


        if (errorManager.wasThereErrorsOrWarnigns()){
            System.out.println("Se encontraron errores o avisos, por favor revise el archivo errorLog en la carpeta de salida.");
            System.out.print("Introduzca '0' para abortar la ejecución o cualquier otro input para continuarla: ");
            try (Scanner sc = new Scanner(System.in)) {
                String input = sc.nextLine();
                if (input.equals("0")) {
                    System.out.println("La aplicación procederá a cerrarse.");
                    System.exit(0);
                }
                else{
                    System.out.println("La aplicación continuará con la ejecución...");
                }
            }

        }

        Enconder basicEncoder = new Enconder();


        Individual individualPrime = basicEncoder.encodeListExams(dataHandler);
        FitnessFunction fn = new LinearFitnessFunction(dataHandler);

        GeneticOperators geneticOperators = new GeneticOperators();
        GeneticCore genCore = new GeneticCore(individualPrime, conf.getGeneticParameters().getPopulationSize(), geneticOperators);


        Individual finalOne = genCore.geneticAlgorithm(conf.getGeneticParameters().getMutationProbability(),
                conf.getGeneticParameters().getCrossingProbability(), fn,
                conf.getGeneticParameters().getMaxIterations(), conf.getGeneticParameters().getLoggingFrequency());



        List<Individual> finalPopulation = genCore.getPopulation();
        finalPopulation.sort(Comparator.comparingDouble(i -> i.getFitnessScore(fn)));

        HashSet<Individual> outputIndividuals = new HashSet<>();
        outputIndividuals.add(finalOne);

        getBestSchedules(finalPopulation, outputIndividuals, conf.getGeneticParameters().getMaxSchedulesToTake());




        outputHandler.writeOutputFiles(outputIndividuals, genCore.getLogging());


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
