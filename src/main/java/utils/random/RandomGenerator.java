package utils.random;

import java.util.Random;

/**
 * This is used to generate random objects.
 */
public class RandomGenerator {

    /**
     * Seed to apply to the generator in case we want the algorithm to turn deterministic.
     */
    public final static int SEED = 0;

    /**
     * {@code Random} object with Singleton design pattern.
     */
    private static Random generator;


    /**
     * Provides always the same {@code Random} object.
     * @return Always the same {@code Random} object.
     */
    public static Random getGenerator(){
        if (generator == null){
            //generator = new Random(SEED);
            generator = new Random();
        }
        return generator;
    }

}
