package main;

import domain.ExamsSchedule;
import domain.configuration.WeightConfigurer;
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
import greedyAlgorithm.ChromosomeDecoder;
import logger.dataGetter.StatisticalDataGetter;
import logger.dataGetter.fitnessLogger.GeneticLogger;
import utils.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * This is the main class used for tuning the weights of the fitness function. It is prepared to be run by script.
 *
 * <p>
 * Note that for performance issues it will advisable to comment all the console printing statements in {@code GeneticCore}.
 *
 * @see WeightConfigurer
 */
public class AppServerWeigths {

    public static void main(String[] args) {



        //String outputFileName = args[1];
        StatisticalDataGetter statisticalDataGetter;

        double userConstraintsWeight = Double.parseDouble(args[0]);
        double restingIntervalWeight = Double.parseDouble(args[1]);
        double numericalComplexityWeight = Double.parseDouble(args[2]);

        String folderName = generateFolderName(userConstraintsWeight, restingIntervalWeight, numericalComplexityWeight);
        folderName = "weightsResults/" + folderName;
        Utils.createDirectory(folderName);

        // Parsing extra file with input files.
        List<String> instancesToEvaluate = parseInstances("instances.txt");

        String filesFile = "defaultConfiguration/filesFile";
        Configurer conf = new Configurer(filesFile);

        conf.getWeightConfigurer().setUserConstraintsWeight(userConstraintsWeight);
        conf.getWeightConfigurer().setProhibitedIntervalWeight(restingIntervalWeight);
        conf.getWeightConfigurer().setNumericalComplexityWeight(numericalComplexityWeight);

        //Set the right input file in the configurer!
        GeneticLogger logger = new GeneticLogger();

        for (String instance : instancesToEvaluate) {
            conf.swapInputFile(instance);
            conf.swapStatisticDirectory(folderName + "/statistics");
            String instanceFilename = getInstanceFilename(instance);
            statisticalDataGetter = new StatisticalDataGetter(instanceFilename, "");

            ExamParser examParser = new ExamParser();

            Enconder basicEncoder = new Enconder();

            int repetitions = conf.getGeneticParameters().getAlgorithmRepetitions();




            for (int j = 1; j <= repetitions; j++) {
                // Iteration start
                ConstraintParser constraintParser = new ConstraintParser(conf, statisticalDataGetter);
                List<Exam> exams = examParser.parseExams(conf.getFilePaths("inputFile"), conf);
                ExamsSchedule examsSchedule = new ExamsSchedule(conf, exams, constraintParser);

                Individual individualPrime = basicEncoder.encodeListExams(examsSchedule);
                FitnessFunction fn = new LinearFitnessFunction(examsSchedule);

                GeneticOperators geneticOperators = new GeneticOperators(conf.getGeneticParameters().getPopulationSize());
                GeneticCore genCore = new GeneticCore(individualPrime, conf.getGeneticParameters().getPopulationSize(),
                        geneticOperators, logger);


                Individual finalOne = genCore.geneticAlgorithm(conf.getGeneticParameters().getMutationProbability(),
                        conf.getGeneticParameters().getCrossoverProbability(), fn,
                        conf.getGeneticParameters().getGenerations(), conf.getGeneticParameters().getLoggingFrequency());



                List<Individual> finalPopulation = genCore.getPopulation();
                finalPopulation.sort(Comparator.comparingDouble(i -> i.getFitnessScore(fn)));

                HashSet<Individual> outputIndividuals = new HashSet<>();
                outputIndividuals.add(finalOne);


                Utils.getBestSchedules(finalPopulation, outputIndividuals, conf.getGeneticParameters().getMaxSchedulesToTake());



                statisticalDataGetter.writeLogFor(finalOne, new ChromosomeDecoder(conf), examsSchedule);


            }
            Utils.createDirectory(folderName + "/fitnessGraphs");
            logger.writeFitnessGraphData(folderName + "/fitnessGraphs", instanceFilename);




        }



    }

    private static String generateFolderName(double userConstraintsWeight, double restingIntervalWeight, double
            numericalComplexityWeight) {

        return userConstraintsWeight + "uc_" + String.format(Locale.UK, "%.2f",restingIntervalWeight) + "pi_"
                + String.format(Locale.UK, "%.1f", numericalComplexityWeight) + "nc";
    }

    private static String getInstanceFilename(String instance) {
        String[] tmpo = instance.split("/");
        String tmp = tmpo[tmpo.length-1];

        for (int i = tmp.length()-1; i >= 0 ; i--) {
            if (tmp.charAt(i) == '.') {
                return tmp.substring(0, i);
            }
        }
        return tmp;
    }


    public static List<String> parseInstances(String instancesFile) {
        List<String> result = new ArrayList<>();
        try (BufferedReader bfr = new BufferedReader(new FileReader(instancesFile))){
            String line;
            while ((line = bfr.readLine()) != null) {
                result.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }



}
