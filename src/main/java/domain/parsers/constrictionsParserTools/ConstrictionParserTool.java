package domain.parsers.constrictionsParserTools;

import domain.DataHandler;
import domain.constrictions.Constriction;
import domain.constrictions.types.examDependant.HardifiableConstriction;
import org.apache.poi.ss.usermodel.Row;

/**
 * This interface represents all the creation methods of the different {@link Constriction} when
 * parsing.
 *
 * <p>
 * This is a State design pattern over the {@link domain.parsers.ConstrictionParser}, so that when
 * finding a new set of {@link Constriction} in the excel, the parser can swap (if needed) the creation method to
 * the one needed for that type of {@link Constriction}
 *
 * @see domain.parsers.ConstrictionParser
 */
public interface ConstrictionParserTool {

    /**
     * Parsing method for {@code row} that generates a {@code Constriction}.
     * @param row The Excel row to be parsed.
     * @param baseExcelColumn The base column of the Excel. (To handle displacements)
     * @param dataHandler The {@code DataHandler} from which the created constrictions will retrieve data.
     * @return A new {@code Constriction} parsed from {@code row}.
     */
    HardifiableConstriction parseConstriction(Row row, int baseExcelColumn, DataHandler dataHandler);

    /**
     * Sets the description of this constriction in the excel.
     * @param description The description of this constriction in the excel.
     */
    void setDescription(String description);

    /**
     * Gets the description of this constriction in the excel.
     * @return The description for the type of constriction in the excel.
     */
    String getDescription();

    /**
     * Headers for this type of constriction in the excel
     * @param headers Headers in the excel.
     */
    void setHeaders(String[] headers);

    /**
     * Gets the headers for this type of constriction in the excel.
     * @return The headers to be written in the excel.
     */
    String[] getHeaders();

    void writeConstriction(Constriction con, Row row, int baseExcelColumn);
}
