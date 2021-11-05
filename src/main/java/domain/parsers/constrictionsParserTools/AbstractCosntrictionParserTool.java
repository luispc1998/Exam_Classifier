package domain.parsers.constrictionsParserTools;

/**
 * This is just to group some common functionality to all of the ConstrictionParserTools.
 */
public abstract class AbstractCosntrictionParserTool implements ConstrictionParserTool {

    /**
     * Description for the type of Constriction in the excel.
     */
    private String description;

    /**
     * Headers for the type of Constriction in the excel.
     */
    private String[] headers;

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String[] getHeaders() {
        return headers;
    }
}
