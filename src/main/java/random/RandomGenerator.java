package random;

import java.util.Random;

/**
 * This is used to generate random objects.
 */
public class RandomGenerator {

    public final static int SEED = 0;
    private static Random generator;



    public static Random getGenerator(){
        if (generator == null){
            //generator = new Random(SEED);
            generator = new Random();
        }
        return generator;
    }


    public static Random getGeneratorWithSeed(int seed){
        return new Random(seed);
    }
}
