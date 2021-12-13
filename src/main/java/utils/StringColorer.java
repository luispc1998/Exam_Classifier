package utils;

public class StringColorer {

    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_RESET = "\u001B[0m";


    public String colorRed(String msg) {
        return ANSI_RED + msg + ANSI_RESET;
    }

    public String colorBlack(String msg) {
        return ANSI_BLACK + msg + ANSI_RESET;
    }

    public String colorGreen(String msg) {
        return ANSI_GREEN + msg + ANSI_RESET;
    }

    public String colorYellow(String msg) {
        return ANSI_YELLOW + msg + ANSI_RESET;
    }

    public String colorBlue(String msg) {
        return ANSI_BLUE + msg + ANSI_RESET;
    }

    public String colorPurple(String msg) {
        return ANSI_PURPLE + msg + ANSI_RESET;
    }

    public String colorCyan(String msg) {
        return ANSI_CYAN + msg + ANSI_RESET;
    }

    public String colorWhite(String msg) {
        return ANSI_WHITE + msg + ANSI_RESET;
    }
}
