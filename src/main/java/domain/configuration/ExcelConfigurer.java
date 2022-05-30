package domain.configuration;


import utils.Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class is in charge of parsing the properties file associated with the Excel.
 *
 * <p>
 * There should be specified the headers of the exam, as well as the initial row and columns to read and parse constraints.
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

            this.excelConstraintBaseColumn = Integer.parseInt(Utils.nullFilter(fileProperties.getProperty("constraintsFirstCol")));
            this.excelConstraintBaseRow = Integer.parseInt(Utils.nullFilter(fileProperties.getProperty("constraintsFirstRow")));
            this.examFirstHeader = Utils.nullFilter(fileProperties.getProperty("examFirstHeader")).trim();
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Missing configuration option in Excel configuration file. Mandatory options: [constraintsFirstCol , " +
                    "constraintsFirstRow, examFirstHeader].");
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Could not parse property/ies in Excel configuration file due to number format problems.");
        }catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Could not find Excel configuration file");
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not parse properties in Excel configuration file");
        }



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
