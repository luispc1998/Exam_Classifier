package domain.parsers.constrictionsParserTools;

import domain.DataHandler;
import domain.constrictions.Constriction;
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
    Constriction parseConstriction(Row row, int baseExcelColumn, DataHandler dataHandler);
}
