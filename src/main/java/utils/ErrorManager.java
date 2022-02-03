package utils;

import java.util.ArrayList;
import java.util.List;

public class ErrorManager {


    private List<String> errors;
    private List<String> warnings;

    private boolean pendingErrors;

    public ErrorManager() {
        errors = new ArrayList<>();
        warnings = new ArrayList<>();
        pendingErrors = false;
    }



    public void addError(String error) {
        errors.add(error);
        pendingErrors = true;
    }

    public void addWarning(String warning) {
        warnings.add(warning);
        pendingErrors = true;
    }

    public void markPendingErrorsAsShowed() {
        pendingErrors = false;
    }

    public boolean wasThereErrorsOrWarnigns() {
        return pendingErrors;
    }

    public String getFormattedString() {
        StringBuilder output = new StringBuilder();
        if (errors.size() > 0) {
            output.append("---ERRORS---\n\n");
            for (String error : errors) {
                output.append("\t");
                output.append(error);
                output.append("\n");
            }
        }

        if (warnings.size() > 0) {
            output.append("---WARNINGS---\n\n");
            for (String warning : warnings) {
                output.append("\t");
                output.append(warning);
                output.append("\n");
            }
        }

        return output.toString();
    }
}
