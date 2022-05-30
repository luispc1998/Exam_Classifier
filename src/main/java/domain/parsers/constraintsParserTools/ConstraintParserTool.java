package domain.parsers.constraintsParserTools;

import domain.ExamsSchedule;
import domain.constraints.types.softConstraints.SoftConstraint;
import domain.constraints.types.softConstraints.userConstraints.UserConstraint;
import domain.parsers.ConstraintParser;
import org.apache.poi.ss.usermodel.Row;

/**
 * This interface represents all the creation methods of the different {@link SoftConstraint} when
 * parsing.
 *
 * <p>
 * This is a State design pattern over the {@link ConstraintParser}, so that when
 * finding a new set of {@link UserConstraint} in the Excel, the parser can swap (if needed) the creation method to
 * the one needed for that type of {@link UserConstraint}.
 *
 * @see ConstraintParser
 */
public interface ConstraintParserTool {

    /**
     * Parsing method for {@code row} that generates a {@code Constraint}.
     * @param row The Excel row to be parsed.
     * @param baseExcelColumn The base column of the Excel. (To handle displacements)
     * @param examsSchedule The {@code DataHandler} from which the created constraints will retrieve data.
     * @return A new {@code Constraint} parsed from {@code row}.
     */
    UserConstraint parseConstraint(Row row, int baseExcelColumn, ExamsSchedule examsSchedule);

    /**
     * Sets the description of this constraint in the Excel.
     * @param description The description of this constraint in the Excel.
     */
    void setDescription(String description);

    /**
     * Gets the description of this constraint in the Excel.
     * @return The description for the type of constraint in the Excel.
     */
    String getDescription();

    /**
     * Headers for this type of constraint in the Excel
     * @param headers Headers in the Excel.
     */
    void setHeaders(String[] headers);

    /**
     * Gets the headers for this type of constraint in the Excel.
     * @return The headers to be written in the Excel.
     */
    String[] getHeaders();

    /**
     * Writes the constraint to Excel format.
     * @param con The constraint to be written.
     * @param row The Excel row in which the constraint will be written.
     * @param baseExcelColumn The base Excel column from which the data can be written.
     */
    void writeConstraint(SoftConstraint con, Row row, int baseExcelColumn);
}
