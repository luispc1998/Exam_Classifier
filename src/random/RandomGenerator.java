package random;

import java.util.Random;

public class RandomGenerator {

    public final static int SEED = 0;
    private static Random generator;



    public static Random getGenerator(){
        if (generator == null){
            generator = new Random(SEED);
        }
        return generator;
    }
}
