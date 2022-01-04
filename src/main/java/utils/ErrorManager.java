package utils;

import java.util.ArrayList;
import java.util.List;

public class ErrorManager {


    private List<String> errors;
    private List<String> warnings;

    public ErrorManager() {
        errors = new ArrayList<>();
        warnings = new ArrayList<>();
    }



    public void addError(String error) {
        errors.add(error);
    }

    public void addWarning(String warning) {
        warnings.add(warning);
    }

    public boolean wasThereErrorsOrWarnigns() {
        return errors.size()>0 || warnings.size()>0;
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
