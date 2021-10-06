package main;

import domain.DataHandler;
import geneticAlgorithm.Enconder;
import geneticAlgorithm.GeneticCore;
import geneticAlgorithm.Individual;
import me.tongfei.progressbar.ProgressBar;

import java.io.IOException;

public class App {

    public static void main(String[] args) throws IOException {

        DataHandler dataHandler = new DataHandler();

        Enconder basicEncoder = new Enconder();
        Individual individualPrime = basicEncoder.encodeListExams(dataHandler.getExams());

        //GeneticCore genCore = new GeneticCore(individualPrime, 50);

        // Looping over a collection:
        int[] array = new int[1000000000];
        // try-with-resource block

        try (ProgressBar pb = new ProgressBar("Test", array.length)) { // name, initial max
            // Use ProgressBar("Test", 100, ProgressBarStyle.ASCII) if you want ASCII output style
            //pb.maxHint(array.length);
            for ( int i : array ) {
                pb.step(); // step by 1

                pb.setExtraMessage("Reading..."); // Set extra message to display at the end of the bar
            }
        }
    }
}
