package geneticAlgorithm.configuration;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Properties;

/**
 * This class is in charge of parsing the properties file associated with the excel.
 *
 * <p>
 * There should be specified the headers of the exam, as well as the initial row and columns to read and parse constrictions.
 */

public class ExcelConfigurer {

    private int excelConstraintBaseColumn;
    private int excelConstraintBaseRow;
    private String examFirstHeader;

    public ExcelConfigurer(String excelConfigFilepath) {

        InputStream configStream;
        Properties fileProperties = new Properties();
        try {
            configStream = new FileInputStream(excelConfigFilepath);
            fileProperties.load(configStream);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Could not find excel configuration file");
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not parse properties in excel configuration file");
        }

        this.excelConstraintBaseColumn = Integer.parseInt(fileProperties.getProperty("constraintsFirstCol"));
        this.excelConstraintBaseRow = Integer.parseInt(fileProperties.getProperty("constraintsFirstRow"));
        this.examFirstHeader = fileProperties.getProperty("examFirstHeader").trim();

    }

    public int getExcelConstraintBaseColumn() {
        return excelConstraintBaseColumn;
    }

    public int getExcelConstraintBaseRow() {
        return excelConstraintBaseRow;
    }

    public String getExamFirstHeader() {
        return examFirstHeader;
    }
}
