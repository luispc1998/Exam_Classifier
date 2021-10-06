package configuration;

import me.tongfei.progressbar.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationHandler {


    private Properties filePaths;
    private Properties algorithmConfs;
    private Properties fitnessConfs;

    public ConfigurationHandler(String filePathsFilepath, String algorithmConfsFilepath,
                                String fitnessConfsFilepath) throws IOException {

        filePaths = new Properties();
        algorithmConfs = new Properties();
        fitnessConfs = new Properties();

        filePaths.load(getClass().getClassLoader().getResourceAsStream(filePathsFilepath));
        algorithmConfs.load(getClass().getClassLoader().getResourceAsStream(algorithmConfsFilepath));
        fitnessConfs.load(getClass().getClassLoader().getResourceAsStream(fitnessConfsFilepath));
    }

    public String getFilePaths(String key){
        return filePaths.getProperty(key);
    }

    public String getAlgoritmsConfs(String key){
        return algorithmConfs.getProperty(key);
    }

    public String getfitnessConfs(String key){
        return fitnessConfs.getProperty(key);
    }

}
