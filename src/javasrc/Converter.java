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

        try (FileInputStream readerStream = new FileInputStream(namesListFile)){
            content = IOUtils.toString(readerStream);
        } catch (IOException e) {
            System.out.println("Exception: " + e.getMessage() + "; aborting!");
            throw new RuntimeException(e);
        }

        if (content.isEmpty() || content.isBlank()) {
            throw new IllegalArgumentException("Content is empty, aborting!");
        }

        ArrayList<AbstractLocEntryMap<?>> categories = new ArrayList<>();

        categories.add(
                new RandomLocEntryMap(
                        "Generic Ship Names",
                        "SHIP",
                        empireName,
                        Pattern.compile("ship_names = \\{\\s+generic = \\{\\s+(.[^\\}]+)").matcher(content),
                        Pattern.compile("ship_names = \\{\\s+generic = \\{\\s+").matcher(content),
                        false
                )
        );

        categories.add(
                new RandomLocEntryMap(
                        "Subjugator Class Ship Names",
                        "SHIP",
                        empireName,
                        Pattern.compile("swp_subjugator = \\{\\s+(.[^\\}]+)").matcher(content),
                        Pattern.compile("swp_subjugator = \\{\\s+").matcher(content),
                        false
                )
        );

        categories.add(
                new RandomLocEntryMap(
                        "Bellator Class Ship Names",
                        "SHIP",
                        empireName,
                        Pattern.compile("swp_bellator = \\{\\s+(.[^\\}]+)").matcher(content),
                        Pattern.compile("swp_bellator = \\{\\s+").matcher(content),
                        false
                )
        );

        categories.add(
                new RandomLocEntryMap(
                        "Science Ship Names",
                        "SCIENCE_SHIP",
                        empireName,
                        Pattern.compile("science = \\{\\s+(.[^\\}]+)").matcher(content),
                        Pattern.compile("science = \\{\\s+").matcher(content),
                        false
                )
        );

        categories.add(
                new RandomLocEntryMap(
                        "Colony Ship Names",
                        "COLONY_SHIP",
                        empireName,
                        Pattern.compile("\\bcolonizer = \\{\\s+(.[^\\}]+)").matcher(content),
                        Pattern.compile("\\bcolonizer = \\{\\s+").matcher(content),
                        false
                )
        );

        categories.add(
                new RandomLocEntryMap(
                        "Sponsored Colony Ship Names",
                        "SPONS_COL_SHIP",
                        empireName,
                        Pattern.compile("sponsored_colonizer = \\{\\s+(.[^\\}]+)").matcher(content),
                        Pattern.compile("sponsored_colonizer = \\{\\s+").matcher(content),
                        false
                )
        );

        categories.add(
                new RandomLocEntryMap(
                        "Constructor Ship Names",
                        "CONSTRUCTION_SHIP",
                        empireName,
                        Pattern.compile("constructor = \\{\\s+(.[^\\}]+)").matcher(content),
                        Pattern.compile("constructor = \\{\\s+").matcher(content),
                        false
                )
        );

        categories.add(
                new RandomLocEntryMap(
                        "Ground Transport Names",
                        "TRANSPORT",
                        empireName,
                        Pattern.compile("transport = \\{\\s+(.[^\\}]+)").matcher(content),
                        Pattern.compile("transport = \\{\\s+").matcher(content),
                        false
                )
        );

        categories.add(
                new RandomLocEntryMap(
                        "XQ1 Station Names",
                        "XQ1_STATION",
                        empireName,
                        Pattern.compile("military_station_xq1 = \\{\\s+(.[^\\}]+)").matcher(content),
                        Pattern.compile("military_station_xq1 = \\{\\s+").matcher(content),
                        false
                )
        );

        categories.add(
                new RandomLocEntryMap(
                        "XQ2 Station Names",
                        "XQ2_STATION",
                        empireName,
                        Pattern.compile("military_station_xq2 = \\{\\s+(.[^\\}]+)").matcher(content),
                        Pattern.compile("military_station_xq2 = \\{\\s+").matcher(content),
                        false
                )
        );

        categories.add(
                new RandomLocEntryMap(
                        "Golan 1 Station Names",
                        "GOLAN_1",
                        empireName,
                        Pattern.compile("military_station_golan1 = \\{\\s+(.[^\\}]+)").matcher(content),
                        Pattern.compile("military_station_golan1 = \\{\\s+").matcher(content),
                        false
                )
        );

        categories.add(
                new RandomLocEntryMap(
                        "Golan 2 Station Names",
                        "GOLAN_2",
                        empireName,
                        Pattern.compile("military_station_golan2 = \\{\\s+(.[^\\}]+)").matcher(content),
                        Pattern.compile("military_station_golan2 = \\{\\s+").matcher(content),
                        false
                )
        );

        categories.add(
                new RandomLocEntryMap(
                        "Golan 3 Station Names",
                        "GOLAN_3",
                        empireName,
                        Pattern.compile("military_station_golan3 = \\{\\s+(.[^\\}]+)").matcher(content),
                        Pattern.compile("military_station_golan3 = \\{\\s+").matcher(content),
                        false
                )
        );

        categories.add(
                new RandomLocEntryMap(
                        "Battle Station Names",
                        "BATTLE_STATION",
                        empireName,
                        Pattern.compile("military_station_fleetop = \\{\\s+(.[^\\}]+)").matcher(content),
                        Pattern.compile("military_station_fleetop = \\{\\s+").matcher(content),
                        false
                )
        );

        categories.add(
                new RandomLocEntryMap(
                        "Small military Station Names",
                        "XS_MIL_STATION",
                        empireName,
                        Pattern.compile("military_station_small = \\{\\s+(.[^\\}]+)").matcher(content),
                        Pattern.compile("military_station_small = \\{\\s+").matcher(content),
                        false
                )
        );

        categories.add(
                new RandomLocEntryMap(
                        "Ion Cannon Names",
                        "ION_CANNON",
                        empireName,
                        Pattern.compile("ion_cannon = \\{\\s+(.[^\\}]+)").matcher(content),
                        Pattern.compile("ion_cannon = \\{\\s+").matcher(content),
                        false
                )
        );

        // TODO: here be fleet names
        categories.add(
                new CompositeLocEntryMap(
                    "Fleet Names",
                    empireName,
                    "FLEET",
                    "",
                    "fleet_names = \\{\\s+",
                    content
            )
        );
        // TODO: here be army names

        categories.add(
                new RandomLocEntryMap(
                        "Male First Names",
                        "FIRST_MALE",
                        empireName,
                        Pattern.compile("\\sfirst_names_male = \\{\\s+(.[^\\}]+)").matcher(content),
                        Pattern.compile("\\sfirst_names_male = \\{\\s+").matcher(content),
                        true
                )
        );

        categories.add(
                new RandomLocEntryMap(
                        "Female First Names",
                        "FIRST_FEMALE",
                        empireName,
                        Pattern.compile("\\sfirst_names_female = \\{\\s+(.[^\\}]+)").matcher(content),
                        Pattern.compile("\\sfirst_names_female = \\{\\s+").matcher(content),
                        true
                )
        );

        categories.add(
                new RandomLocEntryMap(
                        "Last Names",
                        "SECOND",
                        empireName,
                        Pattern.compile("\\ssecond_names = \\{\\s+(.[^\\}]+)").matcher(content),
                        Pattern.compile("\\ssecond_names = \\{\\s+").matcher(content),
                        true
                )
        );


        if ( ! categories.isEmpty()) {
            output = output.concat("l_english:\n");
        }

        for (AbstractLocEntryMap<?> map : categories) {
            map.generateCategoryEntries();
            output = output.concat(
                    map.generateCategoryString()
            ).concat("\n\n");
        }

        //FileUtils.replaceKeys(categories, content, namesListFile);

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

    private static RandomLocEntryMap createArmyNames(String empirePrefix, String empireName, String content) throws IOException {
        String locPrefix = " " + empirePrefix + "_ARMY_";

        RandomLocEntryMap map = new RandomLocEntryMap("Armies", "_ARMY_", empireName, null, null, false);

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
        map.setRandomRaw(raw);

        return map;
    }


    /**
     * This method reads every name list file and generates the localisation entries for a singular ship class file
     * @param nameListFiles all relevant name list files
     * @return All localisation entries in a Map
     */
    public static RandomLocEntryMap createShipClassNames(ArrayList<File> nameListFiles) {
        Pattern empireShipClasses = Pattern.compile("ship_class_names = \\{(\\s+swp_.*= \\{\\s+.+})+");
        Pattern shipClassesEntry = Pattern.compile("swp_.[^=\\s]* = \\{\\s+.+\\}");
        Pattern shipClassesKeys = Pattern.compile("swp_.[^=\\s]*");
        Pattern shipClassesNames = Pattern.compile("\\{\\s+.+\\}");

        RandomLocEntryMap allShipClasses = new RandomLocEntryMap("Ship Classes", "SWFR_SHIP_CLASS_", null, null, null, false);

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