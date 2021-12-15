package configuration;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utils {


    public static List<String> parseHardConstrictionsId(String hardConstrictionsFilepath) {
        List<String> result = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(hardConstrictionsFilepath));
            String line;
            while ((line = reader.readLine()) != null) {
                result.add(line.trim());
            }
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Could not find Hard Constrictions file");
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not parse Hard Constrictions file");
        }
        return result;
    }
}
