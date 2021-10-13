package configuration;

import me.tongfei.progressbar.ProgressBar;

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

        loadWeightConfigurer();
        loadDateTimeConfigurer();

    }

    private void loadWeightConfigurer() throws IOException {
        this.weigthConfigurer = new WeightConfigurer(getFilePaths("weights"));
    }

    public String getFilePaths(String key){
        return filePaths.getProperty(key);
    }

    public DateTimeConfigurer getDateTimeConfigurer() {
        return dateTimeConfigurer;
    }

    private void loadDateTimeConfigurer() throws IOException {
        String inputFilePath = getFilePaths("inputFile");
        this.dateTimeConfigurer = new DateTimeConfigurer(inputFilePath, getFilePaths("timeConfigurations"));
    }


    public WeightConfigurer getWeightConfigurer() {
        return weigthConfigurer;
    }
}
