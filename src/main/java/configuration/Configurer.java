package configuration;

import me.tongfei.progressbar.ProgressBar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configurer {


    private Properties filePaths;

    private WeightConfigurer weigthConfigurer;
    private DateTimeConfigurer dateTimeConfigurer;


    public Configurer(String filePathsFilepath) throws IOException {

        filePaths = new Properties();


        filePaths.load(getClass().getClassLoader().getResourceAsStream(filePathsFilepath));

        loadWeightConfigurer(filePaths.getProperty("weights"));
        loadDateTimeConfigurer(filePaths.getProperty("dateTimes"), filePaths.getProperty("inputFile"));

    }

    private void loadWeightConfigurer(String weightsFile) throws IOException {
        this.weigthConfigurer = new WeightConfigurer(weightsFile);
    }


    private void loadDateTimeConfigurer(String dateTime, String inputFile) throws IOException {
        this.dateTimeConfigurer = new DateTimeConfigurer(inputFile, dateTime);
    }

    public String getFilePaths(String key){
        return filePaths.getProperty(key);
    }

    public WeightConfigurer getWeightConfigurer() {
        return weigthConfigurer;
    }

    public DateTimeConfigurer getDateTimeConfigurer() {
        return dateTimeConfigurer;
    }
}
