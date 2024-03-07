package javasrc;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.print.DocFlavor;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            allCategories.add(generateCategoryEntries(category, content));
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
                    generateCategory(empireName, map.category, map)
            ).concat("\n\n");


        }

        replaceKeys(allCategories, content, namesListFile);

        System.out.println(output);

        // Disarmed for the moment; TODO: Rearm
        writeToBOMFile(locFile, output);
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


    private enum SequentialKeys {
        O("ORD"),
        C("C"),
        CC("CC"),
        CCC("CCC"),
        R("R");

        final String label;
        SequentialKeys(String label) {
            this.label = label;
        }
    }
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

            String locValue = extractSequentialNames(result);

            categoryEntries.setPrimaryRaw(result);

            String sequentialKey = extractSequentialKey(locValue);
            String locKey = locPrefix.concat(sequentialKey);

            categoryEntries.put(locKey, locValue);

            String innerRandom = extractRandomNames(result);
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

            String locValue = extractSequentialNames(result);
            String sequentialKey = extractSequentialKey(locValue);

            String locKey = locPrefix.concat(sequentialKey);

            categoryEntries.put(locKey, locValue);

        }

        while (onlyRandomMatcher.find() &&  ! isProcessed) {
            String result = onlyRandomMatcher.group();

            String innerRandom = extractRandomNames(result);

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

                String value = extractSequentialNames(outer);

                String locKey = "";

                if ( ! armyKey.isBlank()) {
                    locKey = locPrefix.concat(armyKey.toLowerCase());
                }

                if ( ! locKey.isEmpty() && ! value.isEmpty()) {
                    map.put(locKey, value);
                }
            } else {
                String sequentialInner = extractSequentialNames(outer);
                String randomInner = extractRandomNames(outer);

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

                String locKey = locPrefix.concat(extractSequentialKey(sequentialInner));

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
            // TODO: replace the old keys for each file
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

                writeToNamelistFile(entry.getKey(), content);
            }
        }

        return allShipClasses;
    }


    // Utility methods
    public static String generatePrefix(String empireName, String categoryKey, int lengthEmpireKey) {
        return " " + (empireName.substring(0, lengthEmpireKey) + "_" + categoryKey + "_").toUpperCase();
    }

    private static String generateCategory(String empireName, String category, LocEntryMap entries) {
        if (entries.isEmpty()) return "";

        return generateCategoryCommentHeader(empireName, category)
                .concat("\n")
                .concat(
                        generateCategoryBody(entries)
                );
    }

    private static String extractRandomNames(String content) throws IllegalArgumentException {
        Pattern randomPattern = Pattern.compile("random_names = \\{\\s+(.+)\\s+\\}");
        Matcher randomMatcher = randomPattern.matcher(content);

        if (randomMatcher.find()) {
            return randomMatcher.group()
                    .replace("random_names = {", "")
                    .replace("}", "");
        } else throw new IllegalArgumentException("No matches for provided String found!");
    }

    private static String extractSequentialNames(String content) throws IllegalArgumentException {
        Pattern sequentialPattern = Pattern.compile("sequential_name = (.+)");
        Matcher sequentialMatcher = sequentialPattern.matcher(content);

        if (sequentialMatcher.find()) {
            String outerSequential = sequentialMatcher.group();

            String innerSequential = outerSequential
                    .replace("sequential_name = ", "")
                    .replace("\"", "")
                    .replace("{", "")
                    .replace("}", "");

            String oldKey = innerSequential.subSequence(
                    innerSequential.indexOf("%")+1,
                    innerSequential.lastIndexOf("%")
            ).toString();

            String newKey = SequentialKeys.valueOf(oldKey).label;

            return innerSequential
                    .replace(oldKey, newKey)
                    .replace("%", "$");
        } else throw new IllegalArgumentException("No matches for provided String found!");
    }

    private static String extractSequentialKey(String content) throws IllegalArgumentException {
        if (content != null && ! content.isEmpty() && ! content.isBlank()) {
            return content.substring(
                    content.indexOf("$")+1,
                    content.lastIndexOf("$")
            );
        } else throw new IllegalArgumentException("No extraction of key string possible!");
    }


    /**
     * Generates the comment header for the loc file
     * @param empireName Name of the empire
     * @param category Name of the category
     * @return String of the category header
     */
    public static String generateCategoryCommentHeader(String empireName, String category) {
        String middle = "";

        if (empireName.isBlank() || empireName.isEmpty()) {
            middle = StringConstants.category_header.replace("§§§ ", "").replace("$$$", category);
        } else {
            middle = StringConstants.category_header.replace("§§§", empireName).replace("$$$", category);
        }

        StringBuilder topAndBottom = new StringBuilder();

        int length = middle.length();

        topAndBottom.append("#".repeat(length));

        return topAndBottom + "\n" + middle + "\n" + topAndBottom;
    }

    private static String generateCategoryBody(LocEntryMap map) {
        String body = "";
        
        for (Map.Entry<String, String> entry : map.entrySet()){
            body = body.concat(LocEntryMap.getLocString(entry)).concat("\n");
        }
        
        return body;
    }


    private static LocEntryMap generateCategoryEntries(NameListCategory category, String content) {
        if (category instanceof SimpleNameListCategory) {

            return generateCategoryEntries(
                    ((SimpleNameListCategory) category).getRawPattern().matcher(content),
                    ((SimpleNameListCategory) category).getOverhead().matcher(content),
                    category.categoryName,
                    category.buildLocPrefix(),
                    ((SimpleNameListCategory) category).isMultipleEntriespossible());
        } else return null;
    }
    /**
     *
     * @param matcher Finds all relevant groups from provided content
     * @param overheadMatcher To remove eventual overhead (e.g. block keys) from content
     * @param category Category that is worked on (e.g. generic ship names)
     * @param locPrefix prefix of the localisations key
     * @return Map with all localisation entries
     */
    private static LocEntryMap generateCategoryEntries(Matcher matcher, Matcher overheadMatcher, String category, String locPrefix, boolean allGroups){
        LocEntryMap categoryEntryMap =  new LocEntryMap(category);

        // If multiple groups fall into the same category, e.g. first names, bundle them
        if (allGroups) {
            while (matcher.find()) {
                String list = matcher.group();

                if (overheadMatcher.find()) {
                    String overhead = overheadMatcher.group();
                    list = list.replace(overhead, "");
                }

                categoryEntryMap.setPrimaryRaw(list);

                ArrayList<String> namesList = extractNames(list);

                for (String name : namesList) {
                    String key = StringUtils.stripAccents(
                            name
                                    .replace("\"", "")
                                    .replace(" ", "_")
                                    .replace("'", "")
                                    .stripTrailing()
                    );
                    String value = name.replace("\"", "");

                    categoryEntryMap.put(locPrefix.concat(key.toLowerCase()), value);
                }
            }
        } else {
            if (matcher.find()) {
                String list = matcher.group();

                if (overheadMatcher.find()) {
                    String overhead = overheadMatcher.group();
                    list = list.replace(overhead, "");
                }

                categoryEntryMap.setPrimaryRaw(list);

                ArrayList<String> namesList = extractNames(list);

                for (String name : namesList) {
                    String key = StringUtils.stripAccents(
                            name
                                    .replace("\"", "")
                                    .replace(" ", "_")
                                    .replace("'", "")
                                    .stripTrailing()
                    );
                    String value = name.replace("\"", "");

                    categoryEntryMap.put(locPrefix.concat(key.toLowerCase()), value);
                }
            }
        }
        return categoryEntryMap;
    }


    /**
     * General regex to extract space divided name lists into a string array
     * @param raw the namelist
     * @return A singular name
     */
    private static ArrayList<String> extractNames(String raw) {
        Pattern namesPattern = Pattern.compile("\"(.*?)\"|(\\S+)");
        Matcher namesMatcher = namesPattern.matcher(raw);

        ArrayList<String> namesOutput = new ArrayList<>();

        while (namesMatcher.find()){
            String group = namesMatcher.group();

            if (Pattern.compile("[A-Z]+_").matcher(group).find()) continue;

            namesOutput.add(group);
        }

        return namesOutput;
    }


    // TODO: does not work with army names, need maybe extra method
    private static void replaceKeys(ArrayList<LocEntryMap> categories, String content, File original) {
        String newContent = content;

        for (LocEntryMap map : categories) {
            if (map.isEmpty()) continue;

            String toReplace = map.getPrimaryRaw();

            if (toReplace == null || toReplace.isEmpty()) {
                continue;
            }

            String keyList = "";
            int perLine = 0;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                keyList = keyList
                        .concat(entry.getKey())
                        .concat(" ");
                if (perLine == 5) {
                    keyList = keyList.concat("\n\t\t\t");
                    perLine = 0;
                }
                perLine++;
            }

            newContent = newContent.replace(toReplace, keyList);

            if (map.getSecondaryRaw() != null) {
                if (!map.getSecondaryRaw().isEmpty() || !map.getSecondaryRaw().isBlank()) {
                    String secondaryKeys = "";
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        // Check if the entry is in secondaryRaw, mind the differing SequentialKey flavour


                        for (SequentialKeys key : SequentialKeys.values()) {
                            if (entry.getValue().contains(key.label)) {
                                secondaryKeys = entry.getValue()
                                        .replace(key.label, key.name())
                                        .replace("$", "%");
                            }
                        }
                    }
                    newContent = newContent.replace(map.getSecondaryRaw(), secondaryKeys);
                }
            }
        }

        writeToNamelistFile(original, newContent);
    }


    private static void writeToNamelistFile (File nameListFile, String content) {
        try (FileOutputStream fos = new FileOutputStream(nameListFile)){
            fos.write(content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }


    private static final byte[] BOM = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
    public static void writeToBOMFile(File file, String content)  {
        if (file.exists()) {
            System.out.println("File already exists, aborting!");
            return;
        }
        try (FileOutputStream fos = new FileOutputStream(file)){
            fos.write(BOM);
            fos.write(content.getBytes(StandardCharsets.UTF_8));}
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void writeToBOMFile(File file, LocEntryMap entries)  {
        String content = "l_english:\n" + generateCategory("", "Ship Classes", entries);

        writeToBOMFile(file, content);
    }

}