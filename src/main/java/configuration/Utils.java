package configuration;

import java.io.BufferedReader;
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
