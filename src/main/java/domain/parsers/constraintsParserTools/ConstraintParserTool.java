package domain.parsers.constraintsParserTools;

import domain.DataHandler;
import domain.constraints.Constraint;
import domain.constraints.types.softConstraints.userConstraints.UserConstraint;
import domain.parsers.ConstraintParser;
import org.apache.poi.ss.usermodel.Row;

/**
 * This interface represents all the creation methods of the different {@link Constraint} when
 * parsing.
 *
 * <p>
 * This is a State design pattern over the {@link ConstraintParser}, so that when
 * finding a new set of {@link Constraint} in the excel, the parser can swap (if needed) the creation method to
 * the one needed for that type of {@link Constraint}
 *
 * @see ConstraintParser
 */
public interface ConstraintParserTool {

    /**
     * Parsing method for {@code row} that generates a {@code Constraint}.
     * @param row The Excel row to be parsed.
     * @param baseExcelColumn The base column of the Excel. (To handle displacements)
     * @param dataHandler The {@code DataHandler} from which the created constraints will retrieve data.
     * @return A new {@code Constraint} parsed from {@code row}.
     */
    UserConstraint parseConstraint(Row row, int baseExcelColumn, DataHandler dataHandler);

    /**
     * Sets the description of this constraint in the excel.
     * @param description The description of this constraint in the excel.
     */
    void setDescription(String description);

    /**
     * Gets the description of this constraint in the excel.
     * @return The description for the type of constraint in the excel.
     */
    String getDescription();

    /**
     * Headers for this type of constraint in the excel
     * @param headers Headers in the excel.
     */
    void setHeaders(String[] headers);

    /**
     * Gets the headers for this type of constraint in the excel.
     * @return The headers to be written in the excel.
     */
    String[] getHeaders();

    /**
     * Writes the constraint to excel format.
     * @param con The constraint to be written.
     * @param row The excel row in which the constraint will be written.
     * @param baseExcelColumn The base excel column from which the data can be written.
     */
    void writeConstraint(Constraint con, Row row, int baseExcelColumn);
}
