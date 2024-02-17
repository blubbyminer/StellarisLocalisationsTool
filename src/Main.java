import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;

public class Main {


    public static void main(String[] args) {

        /*
        TODO: args auslesen
            - Files einlesen
            - Converter anstoßen
            - Rückschrieb der Keys anstoßen
         */

        String output = "";

        Map<String, String> allShipClasses = new TreeMap<>();

        for (File file : nameListFiles) {
            try (FileInputStream readerStream = new FileInputStream(file)) {
                String fileContent = IOUtils.toString(readerStream);

                Matcher empireShipClassesMatcher = empireShipClasses.matcher(fileContent);

                if (empireShipClassesMatcher.find()) {
                    String shipClasses = empireShipClassesMatcher.group();

                    shipClasses = shipClasses.replace("ship_class_names = {", "");

                    Matcher shipClassesEntryMatcher = shipClassesEntry.matcher(shipClasses);
                    while (shipClassesEntryMatcher.find()) {
                        String entry = shipClassesEntryMatcher.group();

                        Matcher shipClassKeyMatcher = shipClassesKeys.matcher(entry);
                        Matcher shipClassNameMatcher = shipClassesNames.matcher(entry);

                        // Both patterns need to work, otherwise entry is invalid
                        if (shipClassKeyMatcher.find() && shipClassNameMatcher.find()) {
                            String key = shipClassKeyMatcher.group();
                        }
                    }
                }
            }
        }
    }
}