package geneticAlgorithm.configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This groups all the configurations that the application may need.
 * <p>
 * This is in charge of reading the geneticAlgorithm.configuration files and make their data available
 * from the code. However due to the variety of geneticAlgorithm.configuration objects the responsibilities are
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
     * Genetic algorithm parameters.
     */
    private GeneticParameters geneticParameters;

    /**
     * Folder that will contain all the data, configurations and outputs for the execution.
     */
    private final String statisticalFolder;

    /**
     * Configurer with the excel configurations.
     */
    private ExcelConfigurer excelConfigurer;

    /**
     * Constructor for the class
     * @param filePathsFilepath path to a properties file which has the paths to the other geneticAlgorithm.configuration files
     */
    public Configurer(String filePathsFilepath) {
        this(filePathsFilepath, "");
    }

    /**
     * Constructor for the class used for statistical purposes.
     * @param filePathsFilepath to a properties file which has the paths to the other configuration files.
     * <p> Does not include the statistical folder in the path.
     * @param statisticalFolder Statistical folder of the execution.
     * <p>
     * The statistical folder is just a directory with the data and configuration of one of the testing executions.
     */
    public Configurer( String filePathsFilepath, String statisticalFolder) {
        this.statisticalFolder = statisticalFolder.isEmpty() ? "" : (statisticalFolder + "/");
        loadFilePaths(filePathsFilepath);
        loadWeightConfigurer(this.statisticalFolder + filePaths.getProperty("weights"));
        loadDateTimeConfigurer(this.statisticalFolder + filePaths.getProperty("dateTimes"),
                filePaths.getProperty("inputFile"));
        loadGeneticAlgorithmParameters(this.statisticalFolder + filePaths.getProperty("geneticConfiguration"));
        loadExcelConfigurer(filePaths.getProperty("excelConfiguration"));
    }

    /**
     * Loads from the provided file the properties containing the paths to all the other geneticAlgorithm.configuration files.
     * @param filePathsFilepath path to a properties file containing the paths of the other geneticAlgorithm.configuration files.
     */
    private void loadFilePaths(String filePathsFilepath) {
        InputStream configStream;
        filePaths = new Properties();

        try {
            configStream = new FileInputStream(filePathsFilepath);
            filePaths.load(configStream);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Could not find file with filepaths.");
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not parse file with filepaths.");
        }
    }

    /**
     * Loads the parameters of the genetic algorithm.
     * @param geneticConfiguration The file where the parameters are specified.
     */
    private void loadGeneticAlgorithmParameters( String geneticConfiguration) {
        this.geneticParameters = GeneticParameters.loadFromFile(geneticConfiguration);
    }

    /**
     * Returns the parameters of the genetic algorithm.
     * @return the parameters of the genetic algorithm.
     */
    public GeneticParameters getGeneticParameters() {
        return geneticParameters;
    }

    /**
     * Creates a new instance of the WeightConfigurer.
     * @param weightsFile filepath to the properties file where the weights for the constrictions are declared
     */
    private void loadWeightConfigurer(String weightsFile) {
        this.weigthConfigurer = new WeightConfigurer(weightsFile);
    }

    /**
     * Creates a new instance of the DateTimeConfigurer
     * @param dateTimeFilepath filepath to the properties file where the date and times configurations are declared.
     * @param inputDataFilepath filepath to the input excel file where the exams, constrictions, and calendar are declared.
     */
    private void loadDateTimeConfigurer( String dateTimeFilepath,  String inputDataFilepath) {
        this.dateTimeConfigurer = new DateTimeConfigurer(dateTimeFilepath, inputDataFilepath);
    }

    /**
     * Creates the excel configurer instance laoding the configuration file.
     * @param excelFilepath The path of the configuration file of the excel.
     */
    private void loadExcelConfigurer(String excelFilepath) {
        this.excelConfigurer = new ExcelConfigurer(excelFilepath);
    }

    /**
     * Returns the {@code excelConfigurer} attribute.
     * @return {@code excelConfigurer} attribute.
     */
    public ExcelConfigurer getExcelConfigurer() {
        return excelConfigurer;
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
     * @return {@code weightConfigurer} attribute.
     */
    public WeightConfigurer getWeightConfigurer() {
        return weigthConfigurer;
    }

    /**
     * Returns the {@code dateTimeConfigurer} attribute.
     * @return {@code dateTimeConfigurer} attribute.
     */
    public DateTimeConfigurer getDateTimeConfigurer() {
        return dateTimeConfigurer;
    }

    /**
     * Checks with the {@code WeightConfigurer} whether a constriction id exists or not.
     * @param id The constriction id to be checked.
     * @return true in case it exists, false otherwise.
     */
    public boolean existsConstrictionID(String id) {
        return getWeightConfigurer().existsConstrictionID(id);
    }

    /**
     * Changes the actual input file path for the passed as parameter.
     * @param inputFile New path to the input file.
     */
    public void swapInputFile(String inputFile) {
        filePaths.put("inputFile", inputFile);
        loadDateTimeConfigurer(statisticalFolder + filePaths.getProperty("dateTimes"),
                filePaths.getProperty("inputFile"));
    }

    /**
     * Changes the actual statistic file path for the passed as parameter.
     * @param statisticDirectory New path to the statistic file.
     */
    public void swapStatisticDirectory(String statisticDirectory) {
        filePaths.put("statisticsBaseDirectory", statisticDirectory);

    }
}


