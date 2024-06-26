package javasrc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class Main {

    public static int EMPIRE_PREFIX_LENGTH = 3;
    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("No arguments given, aborting");
        }

        if (args[0] == null || args[0].isEmpty() || args[0].isBlank() ) {
            System.out.println("Argument 'projectPath' was left empty or null, aborting");
        }

        if (args[1] != null) {
            try {
                int temp = Integer.parseInt(args[1]);
                if (temp >= EMPIRE_PREFIX_LENGTH) {
                    EMPIRE_PREFIX_LENGTH = temp;
                } else throw new NumberFormatException("Values below 3 are not allowed!");
            } catch (NumberFormatException e) {
                System.out.println("Argument invalid, proceeding with default value for empire prefix length.");
                System.out.println("Details: " + e.getMessage());
            }
        }

        final String projectPath = args[0];
        final String localisationsPath = projectPath + "/localisation/english/swfr_name_list/";
        final String nameListPath = projectPath + "/common/name_lists/";

        final String shipClassLocfilePath = localisationsPath + "swfr_ship_classes_l_english.yml";

        File localisationsDir = new File(localisationsPath);
        File nameListDir = new File(nameListPath);

        if ( ! localisationsDir.isDirectory()) {
            System.out.println("Path to localisations is not a directory, aborting");
            return;
        }
        if ( ! nameListDir.isDirectory()) {
            System.out.println("Path to name lists is not a directory, aborting");
            return;
        }

        ArrayList<File> nameListFiles = new ArrayList<>();
        ArrayList<File> existingLocFiles = new ArrayList<>();

        Map<File, File> locFilesToCreate = new TreeMap<>();

        for (File file : Objects.requireNonNull(nameListDir.listFiles())) {
            if ( ! nameListFiles.contains(file)) {
                nameListFiles.add(file);
            }
        }

        for (File file : Objects.requireNonNull(localisationsDir.listFiles())) {
            if ( ! existingLocFiles.contains(file)) {
                existingLocFiles.add(file);
            }
        }

        // Get missing localisation files
        for (File nameList : nameListFiles) {
            String empireName = nameList.getName().replace("SWP_", "").replace(".txt", "").toLowerCase();

            boolean existsAlready = false;
            for (File locFile : existingLocFiles) {
                String locEmpireString = locFile.getName()
                        .replace("swfr_", "")
                        .replace("_l_english.yml", "")
                        .replace(localisationsPath, "")
                        .toLowerCase();
                if (locEmpireString.contains(empireName)) {
                    existsAlready = true;
                    break;
                }
            }

            if ( ! existsAlready) {
                String filename = localisationsPath + "swfr_" + empireName + "_l_english.yml";
                File toCreate = new File(filename);
                locFilesToCreate.put(nameList, toCreate);
            }
        }

        File shipClassLocFile = new File(shipClassLocfilePath);

        // writeToFile checks again, but we can skip generating the map if it already exists
        if ( ! shipClassLocFile.exists()) {
            RandomLocEntryMap shipClasses = Converter.createShipClassNames(nameListFiles);

            FileUtils.writeToBOMFile(shipClassLocFile, shipClasses);
        }

        // create missing locfiles
        for (Map.Entry<File, File> entry : locFilesToCreate.entrySet()) {
            try {
                Converter.createLocFile(entry.getKey(), entry.getValue());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}