import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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


        ArrayList<LocEntryMap> allCategories = new ArrayList<>();
        try {
            LocEntryMap genericShipNames = createGenericShipNames(empireName, content);
            allCategories.add(genericShipNames);
            
            LocEntryMap bellatorNames = createBellatorShipNames(empireName, content);
            allCategories.add(bellatorNames);
            
            LocEntryMap scienceShipNames = createScienceShipNames(empireName, content);
            allCategories.add(scienceShipNames);
            
            LocEntryMap colonyShipNames = createColonyShipNames(empireName, content);
            allCategories.add(colonyShipNames);
            
            LocEntryMap constructorShipNames = createConstructorShipNames(empireName, content);
            allCategories.add(constructorShipNames);
            
            LocEntryMap transportShipNames = createTransportShipNames(empireName, content);
            allCategories.add(transportShipNames);
            
            LocEntryMap xq1StationNames = createXQ1Names(empireName, content);
            allCategories.add(xq1StationNames);
            
            LocEntryMap xq2StationNames = createXQ2Names(empireName, content);
            allCategories.add(xq2StationNames);
            
            LocEntryMap golan1StationNames = createGolan1Names(empireName, content);
            allCategories.add(golan1StationNames);
            
            LocEntryMap golan2StationNames = createGolan2Names(empireName, content);
            allCategories.add(golan2StationNames);
            
            LocEntryMap golan3StationNames = createGolan3Names(empireName, content);
            allCategories.add(golan3StationNames);
            
            LocEntryMap battleStationNames = createBattleStationNames(empireName, content);
            allCategories.add(battleStationNames);
            
            LocEntryMap smallMilStationnames = createSmallMilitaryStationNames(empireName, content);
            allCategories.add(smallMilStationnames);
            
            LocEntryMap ionCannonNames = createIonCannonNames(empireName, content);
            allCategories.add(ionCannonNames);
            
            LocEntryMap maleFirstNames = createMaleFirstNames(empireName, content);
            allCategories.add(maleFirstNames);
            
            LocEntryMap femaleFirstNames = createFemaleFirstNames(empireName, content);
            allCategories.add(femaleFirstNames);
            
            LocEntryMap secondNames = createSecondNames(empireName, content);
            allCategories.add(secondNames);

//            LocEntryMap fullNames = createFullNames(empireName, content);
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

        //System.out.println(output);

        // Disarmed for the moment; TODO: Rearm
        writeToBOMFile(locFile, output);
    }


    private static LocEntryMap createGenericShipNames(String empireName, String content) throws IOException {
        final String category = "Generic Ships";
        final String locPrefix = " " + empireName.substring(0, 3).toUpperCase() + "_SHIP_";
        
        if (content.isEmpty() || content.isBlank()) {
            System.out.println("Empty file, presuming error, aborting!");
            throw new IOException("Empty content");
        }

        Pattern topLevelNamesPattern = Pattern.compile("ship_names = \\{\\s+generic = \\{\\s+(.[^\\}]+)");
        Matcher overheadMatcher = Pattern.compile("ship_names = \\{\\s+generic = \\{\\s+").matcher(content);
        Matcher matcher = topLevelNamesPattern.matcher(content);

        return generateCategoryEntries(matcher, overheadMatcher, category, locPrefix, false);
    }

    private static LocEntryMap createBellatorShipNames(String empireName, String content) throws IOException {
        final String category = "Bellator Class Ships";
        final String locPrefix = " " + empireName.substring(0, 3).toUpperCase() + "_SHIP_";

        if (content.isEmpty() || content.isBlank()) {
            System.out.println("Empty file, presuming error, aborting!");
            throw new IOException("Empty content");
        }

        Pattern topLevelNamesPattern = Pattern.compile("swp_bellator = \\{\\s+(.[^\\}]+)");
        Matcher overheadMatcher = Pattern.compile("swp_bellator = \\{\\s+").matcher(content);
        Matcher matcher = topLevelNamesPattern.matcher(content);

        return generateCategoryEntries(matcher, overheadMatcher, category, locPrefix,  false);
    }

    private static LocEntryMap createScienceShipNames(String empireName, String content) throws IOException {
        final String category = "Science Ships";
        final String locPrefix = " " + empireName.substring(0, 3).toUpperCase() + "_SCIENCE_SHIP_";

        if (content.isEmpty() || content.isBlank()) {
            System.out.println("Empty file, presuming error, aborting!");
            throw new IOException("Empty content");
        }

        Pattern topLevelNamesPattern = Pattern.compile("science = \\{\\s+(.[^\\}]+)");
        Matcher overheadMatcher = Pattern.compile("science = \\{\\s+").matcher(content);
        Matcher matcher = topLevelNamesPattern.matcher(content);

        return generateCategoryEntries(matcher, overheadMatcher, category, locPrefix,  false);
    }

    //TODO split sponsored_colonizers off
    private static LocEntryMap createColonyShipNames(String empireName, String content) throws IOException {
        final String category = "Colony Ships";
        final String locPrefix = " " + empireName.substring(0, 3).toUpperCase() + "_COLONY_SHIP_";

        if (content.isEmpty() || content.isBlank()) {
            System.out.println("Empty file, presuming error, aborting!");
            throw new IOException("Empty content");
        }

        Pattern topLevelNamesPattern = Pattern.compile("colonizer = \\{\\s+(.[^\\}]+)");
        Matcher overheadMatcher = Pattern.compile("colonizer = \\{\\s+").matcher(content);
        Matcher matcher = topLevelNamesPattern.matcher(content);

        return generateCategoryEntries(matcher, overheadMatcher, category, locPrefix,  false);
    }

    private static LocEntryMap createConstructorShipNames(String empireName, String content) throws IOException {
        final String category = "Constructor Ships";
        final String locPrefix = " " + empireName.substring(0, 3).toUpperCase() + "_CONSTR_SHIP_";

        if (content.isEmpty() || content.isBlank()) {
            System.out.println("Empty file, presuming error, aborting!");
            throw new IOException("Empty content");
        }

        Pattern topLevelNamesPattern = Pattern.compile("constructor = \\{\\s+(.[^\\}]+)");
        Matcher overheadMatcher = Pattern.compile("constructor = \\{\\s+").matcher(content);
        Matcher matcher = topLevelNamesPattern.matcher(content);

        return generateCategoryEntries(matcher, overheadMatcher, category, locPrefix, false);
    }

    private static LocEntryMap createTransportShipNames(String empireName, String content) throws IOException {
        final String category = "Transport Ships";
        final String locPrefix = " " + empireName.substring(0, 3).toUpperCase() + "_GROUND_SHIP_";

        if (content.isEmpty() || content.isBlank()) {
            System.out.println("Empty file, presuming error, aborting!");
            throw new IOException("Empty content");
        }

        Pattern topLevelNamesPattern = Pattern.compile("transport = \\{\\s+(.[^\\}]+)");
        Matcher overheadMatcher = Pattern.compile("transport = \\{\\s+").matcher(content);
        Matcher matcher = topLevelNamesPattern.matcher(content);

        return generateCategoryEntries(matcher, overheadMatcher, category, locPrefix, false);
    }

    private static LocEntryMap createXQ1Names(String empireName, String content) throws IOException {
        final String category = "Military Station XQ1";
        final String locPrefix = " " + empireName.substring(0, 3).toUpperCase() + "_XQ1_STATION_";

        if (content.isEmpty() || content.isBlank()) {
            System.out.println("Empty file, presuming error, aborting!");
            throw new IOException("Empty content");
        }

        Pattern topLevelNamesPattern = Pattern.compile("military_station_xq1 = \\{\\s+(.[^\\}]+)");
        Matcher overheadMatcher = Pattern.compile("military_station_xq1 = \\{\\s+").matcher(content);
        Matcher matcher = topLevelNamesPattern.matcher(content);

        return generateCategoryEntries(matcher, overheadMatcher, category, locPrefix, false);
    }

    private static LocEntryMap createXQ2Names(String empireName, String content) throws IOException {
        final String category = "Military Station XQ2";
        final String locPrefix = " " + empireName.substring(0, 3).toUpperCase() + "_XQ2_STATION_";

        if (content.isEmpty() || content.isBlank()) {
            System.out.println("Empty file, presuming error, aborting!");
            throw new IOException("Empty content");
        }

        Pattern topLevelNamesPattern = Pattern.compile("military_station_xq2 = \\{\\s+(.[^\\}]+)");
        Matcher overheadMatcher = Pattern.compile("military_station_xq2 = \\{\\s+").matcher(content);
        Matcher matcher = topLevelNamesPattern.matcher(content);

        return generateCategoryEntries(matcher, overheadMatcher, category, locPrefix, false);
    }

    private static LocEntryMap createGolan1Names(String empireName, String content) throws IOException {
        final String category = "Golan 1 Station";
        final String locPrefix = " " + empireName.substring(0, 3).toUpperCase() + "_GOLAN1_STATION_";

        if (content.isEmpty() || content.isBlank()) {
            System.out.println("Empty file, presuming error, aborting!");
            throw new IOException("Empty content");
        }

        Pattern topLevelNamesPattern = Pattern.compile("military_station_golan1 = \\{\\s+(.[^\\}]+)");
        Matcher overheadMatcher = Pattern.compile("military_station_golan1 = \\{\\s+").matcher(content);
        Matcher matcher = topLevelNamesPattern.matcher(content);

        return generateCategoryEntries(matcher, overheadMatcher, category, locPrefix, false);
    }

    private static LocEntryMap createGolan2Names(String empireName, String content) throws IOException {
        final String category = "Golan 2 Station";
        final String locPrefix = " " + empireName.substring(0, 3).toUpperCase() + "_GOLAN2_STATION_";

        if (content.isEmpty() || content.isBlank()) {
            System.out.println("Empty file, presuming error, aborting!");
            throw new IOException("Empty content");
        }

        Pattern topLevelNamesPattern = Pattern.compile("military_station_golan2 = \\{\\s+(.[^\\}]+)");
        Matcher overheadMatcher = Pattern.compile("military_station_golan2 = \\{\\s+").matcher(content);
        Matcher matcher = topLevelNamesPattern.matcher(content);

        return generateCategoryEntries(matcher, overheadMatcher, category, locPrefix, false);
    }

    private static LocEntryMap createGolan3Names(String empireName, String content) throws IOException {
        final String category = "Golan 3 Station";
        final String locPrefix = " " + empireName.substring(0, 3).toUpperCase() + "_GOLAN3_STATION_";

        if (content.isEmpty() || content.isBlank()) {
            System.out.println("Empty file, presuming error, aborting!");
            throw new IOException("Empty content");
        }

        Pattern topLevelNamesPattern = Pattern.compile("military_station_golan3 = \\{\\s+(.[^\\}]+)");
        Matcher overheadMatcher = Pattern.compile("military_station_golan3 = \\{\\s+").matcher(content);
        Matcher matcher = topLevelNamesPattern.matcher(content);

        return generateCategoryEntries(matcher, overheadMatcher, category, locPrefix, false);
    }

    private static LocEntryMap createBattleStationNames(String empireName, String content) throws IOException {
        final String category = "Battle Station";
        final String locPrefix = " " + empireName.substring(0, 3).toUpperCase() + "_BATTLE_STATION_";

        if (content.isEmpty() || content.isBlank()) {
            System.out.println("Empty file, presuming error, aborting!");
            throw new IOException("Empty content");
        }

        Pattern topLevelNamesPattern = Pattern.compile("military_station_fleetop = \\{\\s+(.[^\\}]+)");
        Matcher overheadMatcher = Pattern.compile("military_station_fleetop = \\{\\s+").matcher(content);
        Matcher matcher = topLevelNamesPattern.matcher(content);

        return generateCategoryEntries(matcher, overheadMatcher, category, locPrefix, false);
    }

    private static LocEntryMap createSmallMilitaryStationNames(String empireName, String content) throws IOException {
        final String category = "small military Station";
        final String locPrefix = " " + empireName.substring(0, 3).toUpperCase() + "_XS_MIL_STATION_";

        if (content.isEmpty() || content.isBlank()) {
            System.out.println("Empty file, presuming error, aborting!");
            throw new IOException("Empty content");
        }

        Pattern topLevelNamesPattern = Pattern.compile("military_station_small = \\{\\s+(.[^\\}]+)");
        Matcher overheadMatcher = Pattern.compile("military_station_small = \\{\\s+").matcher(content);
        Matcher matcher = topLevelNamesPattern.matcher(content);

        return generateCategoryEntries(matcher, overheadMatcher, category, locPrefix, false);
    }

    private static LocEntryMap createIonCannonNames(String empireName, String content) throws IOException {
        final String category = "Ion Cannon";
        final String locPrefix = " " + empireName.substring(0, 3).toUpperCase() + "_ION_CANNON_";

        if (content.isEmpty() || content.isBlank()) {
            System.out.println("Empty file, presuming error, aborting!");
            throw new IOException("Empty content");
        }

        Pattern topLevelNamesPattern = Pattern.compile("ion_cannon = \\{\\s+(.[^\\}]+)");
        Matcher overheadMatcher = Pattern.compile("ion_cannon = \\{\\s+").matcher(content);
        Matcher matcher = topLevelNamesPattern.matcher(content);

        return generateCategoryEntries(matcher, overheadMatcher, category, locPrefix, false);
    }

    private static LocEntryMap createMaleFirstNames(String empireName, String content) throws IOException {
        final String category = "Male First Names";
        final String locPrefix = " " + empireName.substring(0, 3).toUpperCase() + "_FIRST_MALE_";

        if (content.isEmpty() || content.isBlank()) {
            System.out.println("Empty file, presuming error, aborting!");
            throw new IOException("Empty content");
        }

        Pattern topLevelNamesPattern = Pattern.compile("\\sfirst_names_male = \\{\\s+(.[^\\}]+)");
        Matcher overheadMatcher = Pattern.compile("\\sfirst_names_male = \\{\\s+").matcher(content);
        Matcher matcher = topLevelNamesPattern.matcher(content);

        return generateCategoryEntries(matcher, overheadMatcher, category, locPrefix, true);
    }

    private static LocEntryMap createFemaleFirstNames(String empireName, String content) throws IOException {
        final String category = "Female First Names";
        final String locPrefix = " " + empireName.substring(0, 3).toUpperCase() + "_FIRST_FEMALE_";

        if (content.isEmpty() || content.isBlank()) {
            System.out.println("Empty file, presuming error, aborting!");
            throw new IOException("Empty content");
        }

        Pattern topLevelNamesPattern = Pattern.compile("\\sfirst_names_female = \\{\\s+(.[^\\}]+)");
        Matcher overheadMatcher = Pattern.compile("\\sfirst_names_female = \\{\\s+").matcher(content);
        Matcher matcher = topLevelNamesPattern.matcher(content);

        return generateCategoryEntries(matcher, overheadMatcher, category, locPrefix, true);
    }

    private static LocEntryMap createSecondNames(String empireName, String content) throws IOException {
        final String category = "Second Names";
        final String locPrefix = " " + empireName.substring(0, 3).toUpperCase() + "_SECOND_";

        if (content.isEmpty() || content.isBlank()) {
            System.out.println("Empty file, presuming error, aborting!");
            throw new IOException("Empty content");
        }

        Pattern topLevelNamesPattern = Pattern.compile("\\ssecond_names = \\{\\s+(.[^\\}]+)");
        Matcher overheadMatcher = Pattern.compile("\\ssecond_names = \\{\\s+").matcher(content);
        Matcher matcher = topLevelNamesPattern.matcher(content);

        return generateCategoryEntries(matcher, overheadMatcher, category, locPrefix, true);
    }

    //TODO: regnal names

// TODO: something here is fucked up

//    private static LocEntryMap createFullNames(String empireName, String content) throws IOException {
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

        // Cover all cases
        Pattern sequentialRandomPattern = Pattern.compile("fleet_names = \\{\\s+random_names = \\{\\s+(.*)\\s+\\}\\s+sequential_name = (.+)");
        Pattern onlySequentialPattern = Pattern.compile("fleet_names = \\{\\s+sequential_name = (.+)");
        Pattern onlyRandomPattern = Pattern.compile("fleet_names = \\{\\s+random_names = \\{\\s+(.*)\\s+\\}");

        Matcher sequentialRandomMatcher = sequentialRandomPattern.matcher(content);
        Matcher onlySequentialMatcher = onlySequentialPattern.matcher(content);
        Matcher onlyRandomMatcher = onlyRandomPattern.matcher(content);

        Pattern sequentialNamesPattern = Pattern.compile("sequential_name = (.+)");
        Pattern randomNamesPattern = Pattern.compile("random_names = \\{\\s+(.+)\\s+\\}");

        Matcher sequentialNamesMatcher;
        Matcher randomNamesMatcher;

        boolean isProcessed = false;

        while (sequentialRandomMatcher.find()) {
            String result = sequentialRandomMatcher.group();

            sequentialNamesMatcher = sequentialNamesPattern.matcher(result);
            randomNamesMatcher = randomNamesPattern.matcher(result);

            String outerSequential = "";
            if (sequentialNamesMatcher.find()) {
                outerSequential = sequentialNamesMatcher.group();
                String innerSequential = outerSequential
                        .replace("sequential_name = ", "")
                        .replace("\"", "");

                String sequentialOld = innerSequential.subSequence(
                        innerSequential.indexOf("%")+1,
                        innerSequential.lastIndexOf("%")
                ).toString();
                String sequentialKey = SequentialKeys.valueOf(sequentialOld).label;
                
                String locKey = locPrefix.concat(sequentialKey);
                String locValue = innerSequential
                        .replace(sequentialOld, sequentialKey)
                        .replace("%", "$");
                
                categoryEntries.put(locKey, locValue);
            }

            String outerRandom = "";
            if (randomNamesMatcher.find()) {
                outerRandom = randomNamesMatcher.group();
                String innerRandom = outerRandom
                        .replace("random_names = ", "")
                        .replace("{", "")
                        .replace("}", "");

                ArrayList<String> randomNames = extractNames(innerRandom);

                for (String name : randomNames) {
                    String cleanName = name.replace("\"", "");
                    String key = locPrefix.concat(StringUtils.stripAccents(cleanName).replace(" ", "_").toLowerCase());

                    categoryEntries.put(key, cleanName);
                }
            }

            isProcessed = true;
        }
        while (onlySequentialMatcher.find() && ! isProcessed) {

            String result = onlySequentialMatcher.group();

            sequentialNamesMatcher = sequentialNamesPattern.matcher(result);

            String outerSequential = "";
            if (sequentialNamesMatcher.find()) {
                outerSequential = sequentialNamesMatcher.group();
                String innerSequential = outerSequential
                        .replace("sequential_name = ", "")
                        .replace("\"", "")
                        .replace("{", "")
                        .replace("}", "");

                String sequentialOld = innerSequential.subSequence(
                        innerSequential.indexOf("%") + 1,
                        innerSequential.lastIndexOf("%")
                ).toString();
                String sequentialKey = SequentialKeys.valueOf(sequentialOld).label;

                String locKey = locPrefix.concat(sequentialKey);
                String locValue = "\"".concat(
                        innerSequential
                                .replace(sequentialOld, sequentialKey)
                                .replace("%", "$")
                                .replace("\"", ""))
                        .concat("\"");

                categoryEntries.put(locKey, locValue);
            }
        }
        while (onlyRandomMatcher.find() &&  ! isProcessed) {
            String result = onlyRandomMatcher.group();

            randomNamesMatcher = randomNamesPattern.matcher(result);

            String outerRandom = "";
            if (randomNamesMatcher.find()) {
                outerRandom = randomNamesMatcher.group();
                String innerRandom = outerRandom
                        .replace("random_name = ", "")
                        .replace("\"", "")
                        .replace("{", "")
                        .replace("}", "");

                ArrayList<String> randomNames = extractNames(innerRandom);

                for (String name : randomNames) {
                    String cleanName = name.replace("\"", "");
                    String key = locPrefix.concat(StringUtils.stripAccents(cleanName).replace(" ", "_").toLowerCase());

                    categoryEntries.put(key, cleanName);
                }
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
                Pattern valuePattern = Pattern.compile("sequential_name = \\\"(.+)(?=\\\")");
                Matcher valueMatcher = valuePattern.matcher(outer);

                String value = "";

                if (valueMatcher.find()) {
                    value = valueMatcher.group();
                    value = value
                            .replace("sequential_name = ", "")
                            .replace("\"", "");
                }

                String locValue = "";

                if (!value.isBlank() && value.contains("%")) {
                    String sequentialOld = value.subSequence(
                            value.indexOf("%") + 1,
                            value.lastIndexOf("%")
                    ).toString();

                    String sequentialKey = SequentialKeys.valueOf(sequentialOld).label;
                    locValue = "\"".concat(
                                    value
                                            .replace(sequentialOld, sequentialKey)
                                            .replace("%", "$")
                                            .replace("\"", ""))     // I want to control where those " go to
                            .concat("\"");
                }

                String locKey = "";

                if (!armyKey.isBlank()) {
                    locKey = locPrefix.concat(armyKey.toLowerCase());
                }

                if (!locKey.isEmpty() && !locValue.isEmpty()) {
                    map.put(locKey, locValue);
                }
            } else {
                Pattern sequentialPattern = Pattern.compile("sequential_name = \\\"(.+)(?=\\\")");
                Pattern randomPattern = Pattern.compile("random_names = \\{\\s+(.+)\\s+\\}");

                Matcher sequentialMatcher = sequentialPattern.matcher(outer);
                Matcher randomMatcher = randomPattern.matcher(outer);

                String sequentialInner = "";
                String randomInner = "";

                if (sequentialMatcher.find()) {
                    sequentialInner = sequentialMatcher.group();
                }

                if (randomMatcher.find()) {
                    randomInner = randomMatcher.group();

                    randomInner = randomInner
                            .replace("random_names = {", "")
                            .replace("}", "");
                }

                ArrayList<String> randomNames = new ArrayList<>();

                if ( ! randomInner.isEmpty() && ! randomInner.isBlank()) {
                    randomNames = extractNames(randomInner);

                    for (String name : randomNames) {
                        String cleanName = name.replace("\"", "");
                        String key = locPrefix.concat(StringUtils.stripAccents(cleanName).replace(" ", "_").toLowerCase());

                        map.put(key, cleanName);
                    }
                }

                // TODO: where do those additional " come from for ORD entries
                if (! sequentialInner.isEmpty() && ! sequentialInner.isBlank()) {
                    String innerSequential = sequentialInner
                            .replace("sequential_name = ", "")
                            .replace("\"", "")
                            .replace("{", "")
                            .replace("}", "");

                    String sequentialOld = innerSequential.subSequence(
                            innerSequential.indexOf("%") + 1,
                            innerSequential.lastIndexOf("%")
                    ).toString();
                    String sequentialKey = SequentialKeys.valueOf(sequentialOld).label;

                    String locKey = locPrefix.concat(sequentialKey);
                    String locValue = "\"".concat(
                                    innerSequential
                                            .replace(sequentialOld, sequentialKey)
                                            .replace("%", "$"))
                            .concat("\"");

                    map.put(locKey, locValue);
                }

            }
        }

        // TODO: Unterschied zwischen %O% und $ORD$ bedenken
        map.setRaw(raw);

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

        for (File file : nameListFiles) {
            try (FileInputStream readerStream = new FileInputStream(file)){
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
                        if ( shipClassKeyMatcher.find() && shipClassNameMatcher.find()) {
                            String key = shipClassKeyMatcher.group();
                            String value = shipClassNameMatcher.group()
                                    .replace("{ ", "")
                                    .replace(" }", "")
                                    .replace("\"", "");

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

        return allShipClasses;
    }



    // Utility methods
    private static String generateCategory(String empireName, String category, LocEntryMap entries) {
        if (entries.isEmpty()) return "";

        return generateCategoryCommentHeader(empireName, category)
                .concat("\n")
                .concat(
                        generateCategoryBody(entries)
                );
    }
    
    private static String generateCategoryCommentHeader(String empireName, String category) {
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
            body = body.concat(LocEntryMap.GetLocString(entry)).concat("\n");
        }
        
        return body;
    }

    /**
     *
     * @param matcher Finds all relevant groups from provided content
     * @param overheadMatcher To remove eventual overhead (e.g. block keys) from content
     * @param category Category that is worked on (e.g. generic ship names)
     * @param locPrefix prefix of the localisations key
     * @param allGroups are there entries strewn in for several groups (e.g. character names)
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

                categoryEntryMap.setRaw(list);

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

                categoryEntryMap.setRaw(list);

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

            namesOutput.add(group);
        }

        return namesOutput;
    }


    private static void replaceKeys(ArrayList<LocEntryMap> categories, String content, File original) {
        //TODO: Namen aus zwei Worten werden z.T. geteilt. Prüfen
        String newContent = content;

        for (LocEntryMap map : categories) {
            String toReplace = map.getRaw();

            if (toReplace == null || toReplace.isEmpty()) {
                break;
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