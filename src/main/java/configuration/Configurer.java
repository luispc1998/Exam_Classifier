package configuration;
import java.io.IOException;
import java.util.Properties;

/**
 * This groups all the configurations that the application may need.
 * <p>
 * This is in charge of reading the configuration files and make their data available
 * from the code. However due to the variety of configuration objects the responsibilities are
 * spread out over other classes.
 *
 * @see DateTimeConfigurer
 * @see WeightConfigurer
 *
 */
public class Configurer {

    /**
     * Properties with the filepaths to all necessary input and output files.
     */
    private Properties filePaths;

    /**
     * The weigth configurer
     */
    private WeightConfigurer weigthConfigurer;

    /**
     * The dateTime configurer
     */
    private DateTimeConfigurer dateTimeConfigurer;


    /**
     * Constructor for the class
     * @param filePathsFilepath path to a properties file which has the paths to the other configuration files
     * @throws IOException In case any of the crucial files is not found or the property loading fails.
     */
    public Configurer(String filePathsFilepath) throws IOException {

        filePaths = new Properties();
        filePaths.load(getClass().getClassLoader().getResourceAsStream(filePathsFilepath));

        loadWeightConfigurer(filePaths.getProperty("weights"));
        loadDateTimeConfigurer(filePaths.getProperty("dateTimes"), filePaths.getProperty("inputFile"));

    }

    /**
     * Creates a new instance of the WeightConfigurer.
     * @param weightsFile filepath to the properties file where the weights for the constrictions are declared
     * @throws IOException In case the property loading fails.
     */
    private void loadWeightConfigurer(String weightsFile) throws IOException {
        this.weigthConfigurer = new WeightConfigurer(weightsFile);
    }

    /**
     * Creates a new instance of the DateTimeConfigurer
     * @param dateTimeFilepath filepath to the properties file where the date and times configurations are declared.
     * @param inputDataFilepath filepath to the input excel file where the exams, constrictions, and calendar are declared.
     * @throws IOException In case the property loading fails.
     */
    private void loadDateTimeConfigurer(String dateTimeFilepath, String inputDataFilepath) throws IOException {
        this.dateTimeConfigurer = new DateTimeConfigurer(dateTimeFilepath, inputDataFilepath);
    }

    /**
     * Returns a filepath, for a given key.
     * @param key The key related to the filepath to be returned. Example: "inputFile"
     * @return The filepath corresponding to the given key.
     */
    public String getFilePaths(String key){
        return filePaths.getProperty(key);
    }

    /**
     * Returns the {@code weightConfigurer} attribute.
     * @return {@code weightConfigurer} attribute
     */
    public WeightConfigurer getWeightConfigurer() {
        return weigthConfigurer;
    }

    /**
     * Returns the {@code dateTimeConfigurer} attribute.
     * @return {@code dateTimeConfigurer} attribute
     */
    public DateTimeConfigurer getDateTimeConfigurer() {
        return dateTimeConfigurer;
    }

    /**
     * Checks with the {@code weigthConfigurer} whether a constriction id exists or not.
     * @param id The constriction id to be checked
     * @return true in case it exists, false otherwise.
     */
    public boolean existsConstrinctionID(String id) {
        return getWeightConfigurer().existsConstrictionID(id);
    }
}
