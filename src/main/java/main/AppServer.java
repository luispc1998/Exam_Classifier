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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * This is the main class to be compiled and executed.
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
            ExamParser examParser = new ExamParser();
            ConstrictionParser constrictionParser = new ConstrictionParser();
            Enconder basicEncoder = new Enconder();

            int repetitions = conf.getGeneticParameters().getAlgorithmRepetitions();

            String instanceFilename = getInstanceFilename(instance);
            statisticalDataGetter = new StatisticalDataGetter(instanceFilename, "");

            for (int j = 1; j <= repetitions; j++) {
                // Iteration start
                DataHandler dataHandler = new DataHandler(conf, examParser, constrictionParser);

                Individual individualPrime = basicEncoder.encodeListExams(dataHandler);
                FitnessFunction fn = new LinearFitnessFunction(dataHandler);

                GeneticOperators geneticOperators = new GeneticOperators(conf.getGeneticParameters().getPopulationSize());
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
