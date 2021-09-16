package geneticAlgorithm;

import java.util.ArrayList;
import java.util.List;

public class Individual {

    private List<Integer> cromosome;

    public Individual(List<Integer> cromosome) {
        this.cromosome = new ArrayList<>(cromosome);
    }

    public List<Integer> getCromosome() {
        return new ArrayList<>(cromosome);
    }
}
