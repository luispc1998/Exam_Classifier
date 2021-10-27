package domain.parsers.constrictionsParserTools;

import domain.DataHandler;
import domain.constrictions.Constriction;
import org.apache.poi.ss.usermodel.Row;

public interface ConstrictionParserTool {

    Constriction parseConstriction(Row row, DataHandler dataHandler);
}
