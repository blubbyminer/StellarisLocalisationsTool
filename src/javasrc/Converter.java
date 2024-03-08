package javasrc;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javasrc.StringTools.extractNames;

@SuppressWarnings("RegExpRedundantEscape")
public class Converter {
    // Notizen
    /*
    - Eigene Loc-Datei für Schiffklassen, da packen wir alles rein, wird sonst richtig mist und duplizierung
    - RegEx für jeden anderen Namensblock, und dann alles in eine Reich-spezifische Loc-Datei
    - Armeen sind Reich-spezifisch
    - Vorerst keine Prüfung von existierenden Dateien

    Ablauf weiteres Vorgehen:
    - Erstmal RegEx-Matcher aufbauen, damit alles sauber ausgelesen wird
    - Dann Daten sammeln in entsprechenden Listen
    - yaml generieren, mit Kommentar-Blöcken etc
     */

    public static void createLocFile(File namesListFile, File locFile) throws FileNotFoundException {
        // Output for the loc file
        String output = "";

        // content of the name list file
        String content = "";

        String empireName = namesListFile.getName().replace("SWP_", "").replace(".txt", "");
        String speciesPrefix = namesListFile.getName().replace("SWP_", "").substring(0, 3).toUpperCase();

        try (FileInputStream readerStream = new FileInputStream(namesListFile)){
            content = IOUtils.toString(readerStream);
        } catch (IOException e) {
            System.out.println("Exception: " + e.getMessage() + "; aborting!");
            throw new RuntimeException(e);
        }

        if (content .isEmpty() || content.isBlank()) {
            throw new IllegalArgumentException("Content is empty, aborting!");
        }

        ArrayList<NameListCategory> categories = new ArrayList<>();

        categories.add(
                new SimpleNameListCategory(
                        empireName,
                        speciesPrefix,
                        "Generic Ship Names",
                        "SHIP",
                        Pattern.compile("ship_names = \\{\\s+generic = \\{\\s+(.[^\\}]+)"),
                        Pattern.compile("ship_names = \\{\\s+generic = \\{\\s+"),
                        false
                )
        );

        categories.add(
                new SimpleNameListCategory(
                        empireName,
                        speciesPrefix,
                        "Subjugator Class Ship Names",
                        "SHIP",
                        Pattern.compile("swp_subjugator = \\{\\s+(.[^\\}]+)"),
                        Pattern.compile("swp_subjugator = \\{\\s+"),
                        false
                )
        );

        categories.add(
                new SimpleNameListCategory(
                        empireName,
                        speciesPrefix,
                        "Bellator Class Ship Names",
                        "SHIP",
                        Pattern.compile("swp_bellator = \\{\\s+(.[^\\}]+)"),
                        Pattern.compile("swp_bellator = \\{\\s+"),
                        false
                )
        );

        categories.add(
                new SimpleNameListCategory(
                        empireName,
                        speciesPrefix,
                        "Science Ship Names",
                        "SCIENCE_SHIP",
                        Pattern.compile("science = \\{\\s+(.[^\\}]+)"),
                        Pattern.compile("science = \\{\\s+"),
                        false
                )
        );

        categories.add(
                new SimpleNameListCategory(
                        empireName,
                        speciesPrefix,
                        "Colony Ship Names",
                        "COLONY_SHIP",
                        Pattern.compile("\\bcolonizer = \\{\\s+(.[^\\}]+)"),
                        Pattern.compile("\\bcolonizer = \\{\\s+"),
                        false
                )
        );

        categories.add(
                new SimpleNameListCategory(
                        empireName,
                        speciesPrefix,
                        "Sponsored Colony Ship Names",
                        "SPONS_COL_SHIP",
                        Pattern.compile("sponsored_colonizer = \\{\\s+(.[^\\}]+)"),
                        Pattern.compile("sponsored_colonizer = \\{\\s+"),
                        false
                )
        );

        categories.add(
                new SimpleNameListCategory(
                        empireName,
                        speciesPrefix,
                        "Constructor Ship Names",
                        "CONSTRUCTION_SHIP",
                        Pattern.compile("constructor = \\{\\s+(.[^\\}]+)"),
                        Pattern.compile("constructor = \\{\\s+"),
                        false
                )
        );

        categories.add(
                new SimpleNameListCategory(
                        empireName,
                        speciesPrefix,
                        "Ground Transport Names",
                        "TRANSPORT",
                        Pattern.compile("transport = \\{\\s+(.[^\\}]+)"),
                        Pattern.compile("transport = \\{\\s+"),
                        false
                )
        );

        categories.add(
                new SimpleNameListCategory(
                        empireName,
                        speciesPrefix,
                        "XQ1 Station Names",
                        "XQ1_STATION",
                        Pattern.compile("military_station_xq1 = \\{\\s+(.[^\\}]+)"),
                        Pattern.compile("military_station_xq1 = \\{\\s+"),
                        false
                )
        );

        categories.add(
                new SimpleNameListCategory(
                        empireName,
                        speciesPrefix,
                        "XQ2 Station Names",
                        "XQ2_STATION",
                        Pattern.compile("military_station_xq2 = \\{\\s+(.[^\\}]+)"),
                        Pattern.compile("military_station_xq2 = \\{\\s+"),
                        false
                )
        );

        categories.add(
                new SimpleNameListCategory(
                        empireName,
                        speciesPrefix,
                        "Golan 1 Station Names",
                        "GOLAN_1",
                        Pattern.compile("military_station_golan1 = \\{\\s+(.[^\\}]+)"),
                        Pattern.compile("military_station_golan1 = \\{\\s+"),
                        false
                )
        );

        categories.add(
                new SimpleNameListCategory(
                        empireName,
                        speciesPrefix,
                        "Golan 2 Station Names",
                        "GOLAN_2",
                        Pattern.compile("military_station_golan2 = \\{\\s+(.[^\\}]+)"),
                        Pattern.compile("military_station_golan2 = \\{\\s+"),
                        false
                )
        );

        categories.add(
                new SimpleNameListCategory(
                        empireName,
                        speciesPrefix,
                        "Golan 3 Station Names",
                        "GOLAN_3",
                        Pattern.compile("military_station_golan3 = \\{\\s+(.[^\\}]+)"),
                        Pattern.compile("military_station_golan3 = \\{\\s+"),
                        false
                )
        );

        categories.add(
                new SimpleNameListCategory(
                        empireName,
                        speciesPrefix,
                        "Battle Station Names",
                        "BATTLE_STATION",
                        Pattern.compile("military_station_fleetop = \\{\\s+(.[^\\}]+)"),
                        Pattern.compile("military_station_fleetop = \\{\\s+"),
                        false
                )
        );

        categories.add(
                new SimpleNameListCategory(
                        empireName,
                        speciesPrefix,
                        "Small military Station Names",
                        "XS_MIL_STATION",
                        Pattern.compile("military_station_small = \\{\\s+(.[^\\}]+)"),
                        Pattern.compile("military_station_small = \\{\\s+"),
                        false
                )
        );

        categories.add(
                new SimpleNameListCategory(
                        empireName,
                        speciesPrefix,
                        "Ion Cannon Names",
                        "ION_CANNON",
                        Pattern.compile("ion_cannon = \\{\\s+(.[^\\}]+)"),
                        Pattern.compile("ion_cannon = \\{\\s+"),
                        false
                )
        );

        // TODO: here be ship classes
        // TODO: here be fleet names
        // TODO: here be army names

        categories.add(
                new SimpleNameListCategory(
                        empireName,
                        speciesPrefix,
                        "Male First Names",
                        "FIRST_MALE",
                        Pattern.compile("\\sfirst_names_male = \\{\\s+(.[^\\}]+)"),
                        Pattern.compile("\\sfirst_names_male = \\{\\s+"),
                        true
                )
        );

        categories.add(
                new SimpleNameListCategory(
                        empireName,
                        speciesPrefix,
                        "Female First Names",
                        "FIRST_FEMALE",
                        Pattern.compile("\\sfirst_names_female = \\{\\s+(.[^\\}]+)"),
                        Pattern.compile("\\sfirst_names_female = \\{\\s+"),
                        true
                )
        );

        categories.add(
                new SimpleNameListCategory(
                        empireName,
                        speciesPrefix,
                        "Last Names",
                        "SECOND",
                        Pattern.compile("\\ssecond_names = \\{\\s+(.[^\\}]+)"),
                        Pattern.compile("\\ssecond_names = \\{\\s+"),
                        true
                )
        );


        ArrayList<LocEntryMap> allCategories = new ArrayList<>();

        for (NameListCategory category : categories) {
            allCategories.add(LocEntryMap.generateCategoryEntries(category, content));
        }


        try {
//            java.LocEntryMap fullNames = createFullNames(empireName, content);
//            allCategories.add(fullNames);

            LocEntryMap fleetNames = createFleetNames(speciesPrefix, empireName, content);
            allCategories.add(fleetNames);

            LocEntryMap armyNames = createArmyNames(speciesPrefix, empireName, content);
            allCategories.add(armyNames);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if ( ! allCategories.isEmpty()) {
            output = output.concat("l_english:\n");
        }

        for (LocEntryMap map : allCategories) {
            output = output.concat(
                    StringTools.generateCategory(empireName, map.category, map)
            ).concat("\n\n");
        }

        FileUtils.replaceKeys(allCategories, content, namesListFile);

        System.out.println(output);

        // Disarmed for the moment; TODO: Rearm
        FileUtils.writeToBOMFile(locFile, output);
    }

    //TODO: regnal names

//    private static java.LocEntryMap createFullNames(String empireName, String content) throws IOException {
//        final String category = "Full Names";
//        final String locPrefix = " " + empireName.substring(0, 3).toUpperCase() + "_FULL_";
//
//        if (content.isEmpty() || content.isBlank()) {
//            System.out.println("Empty file, presuming error, aborting!");
//            throw new IOException("Empty content");
//        }
//
//        Pattern topLevelNamesPattern = Pattern.compile("full_names = \\{\\s+(.[^\\}]+)");
//        Matcher overheadMatcher = Pattern.compile("full_names = \\{\\s+").matcher(content);
//        Matcher matcher = topLevelNamesPattern.matcher(content);
//
//        return generateCategoryEntries(matcher, overheadMatcher, category, locPrefix, false);
//    }

    private static LocEntryMap createFleetNames(String empirePrefix, String empireName, String content) throws IOException {
        final String category = "Fleet Names";
        final String locCategory = "_fleet_";

        final String locPrefix = " " + empirePrefix + locCategory;

        if (content.isEmpty() || content.isBlank()) {
            System.out.println("Empty file, presuming error, aborting!");
            throw new IOException("Empty content");
        }

        LocEntryMap categoryEntries = new LocEntryMap(category, null);

        // Cover all cases, only one can come true
        Pattern sequentialRandomPattern = Pattern.compile("fleet_names = \\{\\s+random_names = \\{\\s+(.*)\\s+\\}\\s+sequential_name = (.+)");
        Pattern onlySequentialPattern = Pattern.compile("fleet_names = \\{\\s+sequential_name = (.+)");
        Pattern onlyRandomPattern = Pattern.compile("fleet_names = \\{\\s+random_names = \\{\\s+(.*)\\s+\\}");

        Matcher sequentialRandomMatcher = sequentialRandomPattern.matcher(content);
        Matcher onlySequentialMatcher = onlySequentialPattern.matcher(content);
        Matcher onlyRandomMatcher = onlyRandomPattern.matcher(content);

        boolean isProcessed = false;

        while (sequentialRandomMatcher.find()) {
            String result = sequentialRandomMatcher.group();

            String locValue = StringTools.extractSequentialNames(result);

            categoryEntries.setPrimaryRaw(result);

            String sequentialKey = StringTools.extractSequentialKey(locValue);
            String locKey = locPrefix.concat(sequentialKey);

            categoryEntries.put(locKey, locValue);

            String innerRandom = StringTools.extractRandomNames(result);
            categoryEntries.setSecondaryRaw(innerRandom);

            ArrayList<String> randomNames = extractNames(innerRandom);

            for (String name : randomNames) {
                String cleanName = name.replace("\"", "");
                String key = locPrefix.concat(StringUtils.stripAccents(cleanName).replace(" ", "_").toLowerCase());

                categoryEntries.put(key, cleanName);
            }


            isProcessed = true;
        }
        while (onlySequentialMatcher.find() && ! isProcessed) {
            String result = onlySequentialMatcher.group();

            String locValue = StringTools.extractSequentialNames(result);
            String sequentialKey = StringTools.extractSequentialKey(locValue);

            String locKey = locPrefix.concat(sequentialKey);

            categoryEntries.put(locKey, locValue);

        }

        while (onlyRandomMatcher.find() &&  ! isProcessed) {
            String result = onlyRandomMatcher.group();

            String innerRandom = StringTools.extractRandomNames(result);

            categoryEntries.setPrimaryRaw(innerRandom);

            ArrayList<String> randomNames = extractNames(innerRandom);

            for (String name : randomNames) {
                String cleanName = name.replace("\"", "");
                String key = locPrefix.concat(StringUtils.stripAccents(cleanName).replace(" ", "_").toLowerCase());

                categoryEntries.put(key, cleanName);
            }

        }

        return categoryEntries;
    }


    private static LocEntryMap createArmyNames(String empirePrefix, String empireName, String content) throws IOException {
        String locPrefix = " " + empirePrefix + "_ARMY_";

        LocEntryMap map = new LocEntryMap("Armies");

        // This will return the fleet_names, too, so we have to sieve a bit
        Pattern outerPattern = Pattern.compile("(?:(?:.+ = )\\{\\s+sequential_name = (?:.+))\\s+\\}|(?:.+\\s+random_names = \\{\\s+(?:.*)\\s+\\}\\s+sequential_name = (?:.+))\\s+\\}");

        Matcher outerMatcher = outerPattern.matcher(content);

        String raw = "";

        while (outerMatcher.find()) {
            String outer = outerMatcher.group();
            if (outer.contains("fleet_names")) continue;

            raw = raw
                    .concat(outer)
                    .concat("\n");

            Pattern armyKeyPattern = Pattern.compile(".+?(?= = \\{)");
            Matcher armyKeyMatcher = armyKeyPattern.matcher(outer);

            String armyKey = "";

            if (armyKeyMatcher.find()) {
                armyKey = armyKeyMatcher.group();
                armyKey = armyKey.strip();
            }

            if ( ! outer.contains("random_names")) {

                String value = StringTools.extractSequentialNames(outer);

                String locKey = "";

                if ( ! armyKey.isBlank()) {
                    locKey = locPrefix.concat(armyKey.toLowerCase());
                }

                if ( ! locKey.isEmpty() && ! value.isEmpty()) {
                    map.put(locKey, value);
                }
            } else {
                String sequentialInner = StringTools.extractSequentialNames(outer);
                String randomInner = StringTools.extractRandomNames(outer);

                ArrayList<String> randomNames = new ArrayList<>();

                if ( ! randomInner.isEmpty() && ! randomInner.isBlank()) {
                    randomNames = extractNames(randomInner);

                    for (String name : randomNames) {
                        String cleanName = name.replace("\"", "");
                        String key = locPrefix.concat(
                                StringUtils.stripAccents(cleanName)
                                        .replace(" ", "_")
                                        .toLowerCase()
                        );

                        map.put(key, cleanName);
                    }
                }

                String locKey = locPrefix.concat(StringTools.extractSequentialKey(sequentialInner));

                map.put(locKey, sequentialInner);
            }
        }

        // TODO: Unterschied zwischen %O% und $ORD$ bedenken
        map.setPrimaryRaw(raw);

        return map;
    }


    /**
     * This method reads every name list file and generates the localisation entries for a singular ship class file
     * @param nameListFiles all relevant name list files
     * @return All localisation entries in a Map
     */
    public static LocEntryMap createShipClassNames(ArrayList<File> nameListFiles) {
        Pattern empireShipClasses = Pattern.compile("ship_class_names = \\{(\\s+swp_.*= \\{\\s+.+})+");
        Pattern shipClassesEntry = Pattern.compile("swp_.[^=\\s]* = \\{\\s+.+\\}");
        Pattern shipClassesKeys = Pattern.compile("swp_.[^=\\s]*");
        Pattern shipClassesNames = Pattern.compile("\\{\\s+.+\\}");

        LocEntryMap allShipClasses = new LocEntryMap("Ship Classes", null);

        Map<File, String> fileContents = new HashMap<>();

        for (File file : nameListFiles) {
            try (FileInputStream readerStream = new FileInputStream(file)){
                String fileContent = IOUtils.toString(readerStream);

                fileContents.put(file, fileContent);

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
                        if ( shipClassKeyMatcher.find() && shipClassNameMatcher.find()) {
                            String rawkey = shipClassKeyMatcher.group();
                            String value = shipClassNameMatcher.group()
                                    .replace("{ ", "")
                                    .replace(" }", "")
                                    .replace("\"", "");

                            String key = "SWFR_SHIP_CLASS_" + rawkey.replace("swp_", "");

                            if ( ! allShipClasses.containsKey(key)) {
                                allShipClasses.put(key, value);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        for (Map.Entry<File, String> entry : fileContents.entrySet()) {
            String content = entry.getValue();

            Matcher shipClasses = empireShipClasses.matcher(content);

            if (shipClasses.find()) {
                String classesBlock = shipClasses.group();
                String newClassesBlock = classesBlock;

                Matcher classNameMatcher = shipClassesEntry.matcher(classesBlock.replace("ship_class_names = {", ""));

                while (classNameMatcher.find()) {
                    String classEntry = classNameMatcher.group();

                    Matcher shipClassesKeyMatcher = shipClassesKeys.matcher(classEntry);

                    if (shipClassesKeyMatcher.find()) {
                        String key = shipClassesKeyMatcher.group();

                        for (Map.Entry<String, String> shipClass : allShipClasses.entrySet()) {
                            if (shipClass.getKey().replace("SWFR_SHIP_CLASS", "swp").equals(key)) {
                                newClassesBlock = newClassesBlock.replace(
                                        classEntry,
                                        classEntry.replace(
                                            shipClass.getValue(),
                                                shipClass.getKey()
                                        ).replace("\"", "")
                                );
                            }
                        }
                    }
                }

                content = content.replace(classesBlock, newClassesBlock);

                FileUtils.writeToNamelistFile(entry.getKey(), content);
            }
        }
        return allShipClasses;
    }
}