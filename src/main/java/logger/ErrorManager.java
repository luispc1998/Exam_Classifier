package logger;

import java.util.ArrayList;
import java.util.List;

public class ErrorManager {


    private List<String> showedErrors;
    private List<String> notShowedErrors;


    public ErrorManager() {
        showedErrors = new ArrayList<>();
        notShowedErrors = new ArrayList<>();
    }


    public void addError(String warning) {
        notShowedErrors.add(warning);
    }

    public void markPendingErrorsAsShowed() {
        showedErrors.addAll(notShowedErrors);
        notShowedErrors = new ArrayList<>();
    }

    public boolean wasThereErrorsOrWarnings() {
        return notShowedErrors.size() > 0;
    }

    public String getFormattedString() {
        StringBuilder output = new StringBuilder();
        if (notShowedErrors.size() > 0) {
            output.append("---NEW ERRORS---\n\n");
            for (String error : notShowedErrors) {
                output.append("\t");
                output.append(error);
                output.append("\n");
            }
        }

        if (showedErrors.size() > 0) {
            output.append("\n");
            output.append("---ALREADY ACCEPTED ERRORS---\n\n");
            for (String warning : showedErrors) {
                output.append("\t");
                output.append(warning);
                output.append("\n");
            }
        }
        markPendingErrorsAsShowed();
        return output.toString();
    }
}
