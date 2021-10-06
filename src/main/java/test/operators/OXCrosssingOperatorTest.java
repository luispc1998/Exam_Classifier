package test.operators;

import geneticAlgorithm.Individual;
import geneticAlgorithm.operators.crossing.CrossingOperator;
import geneticAlgorithm.operators.crossing.OXCrosssingOperator;
import org.junit.Assert;
import org.junit.Test;
import random.RandomCromosomeGenerator;
import random.RandomGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class OXCrosssingOperatorTest {

    private final static int CROMOSOME_LENGTH = 10;




    @Test
    /**
     * Left bound and middle.
     */
    public void testLeftBoundMiddle(){

        Random generator = RandomGenerator.getGeneratorWithSeed(10);
        Individual a = RandomCromosomeGenerator.generateIndividual(CROMOSOME_LENGTH, generator);
        Individual b = RandomCromosomeGenerator.generateIndividual(CROMOSOME_LENGTH, generator);

        CrossingOperator oxOperator = new OXCrosssingOperator(RandomGenerator.getGeneratorWithSeed(0));
        List<Individual> childs = oxOperator.doCrossing(a,b);


        List<Individual> rightChilds = new ArrayList<>();
        rightChilds.add(a);
        Assert.assertTrue(childs.equals(rightChilds));

    }


    @Test
    /**
     * Picked same lower and upper limit, so just one element for the first parent was copied to the second.
     */
    public void testSameLowerUpper(){

        Random generator = RandomGenerator.getGeneratorWithSeed(11);
        Individual a = RandomCromosomeGenerator.generateIndividual(CROMOSOME_LENGTH, generator);
        Individual b = RandomCromosomeGenerator.generateIndividual(CROMOSOME_LENGTH, generator);

        CrossingOperator oxOperator = new OXCrosssingOperator(RandomGenerator.getGeneratorWithSeed(11));
        List<Individual> childs = oxOperator.doCrossing(a,b);


        List<Individual> rightChilds = new ArrayList<>();


        rightChilds.add(b);
        Assert.assertTrue(childs.equals(rightChilds));

    }


    @Test
    /**
     * Picked same lower and upper limit, so just one element for the first parent was copied to the second.
     */
    public void testMiddleRightBound(){

        Random generator = RandomGenerator.getGeneratorWithSeed(15);
        Individual a = RandomCromosomeGenerator.generateIndividual(CROMOSOME_LENGTH, generator);
        Individual b = RandomCromosomeGenerator.generateIndividual(CROMOSOME_LENGTH, generator);

        CrossingOperator oxOperator = new OXCrosssingOperator(RandomGenerator.getGeneratorWithSeed(9));
        List<Individual> childs = oxOperator.doCrossing(a,b);


        List<Individual> rightChilds = new ArrayList<>();
        List<Integer> rightCromosome = new ArrayList<>(10);
        rightCromosome.add(9); rightCromosome.add(5); rightCromosome.add(6); rightCromosome.add(7);
        rightCromosome.add(8); rightCromosome.add(4); rightCromosome.add(3); rightCromosome.add(0);
        rightCromosome.add(2); rightCromosome.add(1);

        rightChilds.add(new Individual(rightCromosome));
        Assert.assertTrue(childs.equals(rightChilds));

    }



}