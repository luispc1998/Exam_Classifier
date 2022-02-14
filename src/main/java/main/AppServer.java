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
import greedyAlgorithm.ChromosomeDecoder;
import utils.Utils;
import logger.dataGetter.StatisticalDataGetter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * This is the main class used for tuning the values of the genetic parameters. It is prepared to be run by script.
 *
 * <p>
 * Note that for performance issues it will advisable to comment all the console printing statements in {@code GeneticCore}.
 *
 * @see geneticAlgorithm.configuration.GeneticParameters
 */
public class AppServer {

    public static void main(String[] args) {



        //String outputFileName = args[1];
        StatisticalDataGetter statisticalDataGetter;

        int populationSize = Integer.parseInt(args[0]);
        double mutationProb = Double.parseDouble(args[1]);
        double crossingProb = Double.parseDouble(args[2]);
        int repairingDepth = Integer.parseInt(args[3]);
        String folderName = generateFolderName(populationSize, mutationProb, crossingProb, repairingDepth);
        folderName = "results/" + folderName;
        Utils.createDirectory(folderName);

        // Parsing extra file with input files.
        List<String> instancesToEvaluate = parseInstances("instances.txt");

        String filesFile = "defaultConfiguration/filesFile";
        Configurer conf = new Configurer(filesFile);

        conf.getGeneticParameters().setPopulationSize(populationSize);
        conf.getGeneticParameters().setMutationProb(mutationProb);
        conf.getGeneticParameters().setCrossingProb(crossingProb);
        conf.getGeneticParameters().setRepairingDepth(repairingDepth);

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
                DataHandler dataHandler = new DataHandler(conf, exams, constraintParser);

                Individual individualPrime = basicEncoder.encodeListExams(dataHandler);
                FitnessFunction fn = new LinearFitnessFunction(dataHandler);

                GeneticOperators geneticOperators = new GeneticOperators(conf.getGeneticParameters().getPopulationSize());
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



                statisticalDataGetter.writeLogFor(finalOne, new ChromosomeDecoder(conf), dataHandler);


            }
            Utils.createDirectory(folderName + "/fitnessGraphs");
            logger.writeFitnessGraphData(folderName + "/fitnessGraphs", instanceFilename);




        }



    }

    private static String generateFolderName(double populationSize, double mutationProb, double
            crossingProb, double repairingDepth) {

        return populationSize + "pop_" + String.format(Locale.UK, "%.2f",mutationProb) + "mut_"
                + String.format(Locale.UK, "%.1f", crossingProb) + "cross_" + repairingDepth + "depth";
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
