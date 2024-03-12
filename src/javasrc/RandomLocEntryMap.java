package javasrc;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;

import static javasrc.StringTools.extractNames;

/**
 * Use this with the following structure:
 * key: localisation key, e.g. IMP_SHIP_monitor
 * value: localisation value, e.g. "Monitor" (note, <b>without</b> the parentheses!)
 */
public class RandomLocEntryMap extends AbstractLocEntryMap<String> {
    public String getRandomRaw() {
        return randomRaw;
    }
    public void setRandomRaw(String randomRaw) {
        this.randomRaw = randomRaw;
    }



    private final Matcher rawMatcher;
    private final Matcher overheadMatcher;


    private final boolean multipleEntriesPossible;
    public boolean isMultipleEntriesPossible() {
        return multipleEntriesPossible;
    }

    public RandomLocEntryMap(String category, String categoryLoc, @Nullable String empireName, Matcher rawMatcher, Matcher overheadMatcher, boolean multipleEntriesPossible) {
        super(category, empireName, categoryLoc);
        this.rawMatcher = rawMatcher;
        this.overheadMatcher = overheadMatcher;
        this.multipleEntriesPossible = multipleEntriesPossible;
    }

    public RandomLocEntryMap(@NotNull String category, String categoryLoc, String empireName, Matcher rawMatcher, Matcher overheadMatcher, String randomRaw, boolean multipleEntriesPossible) {
        super(category, empireName, categoryLoc);
        this.rawMatcher = rawMatcher;
        this.overheadMatcher = overheadMatcher;
        this.multipleEntriesPossible = multipleEntriesPossible;

        this.randomRaw = randomRaw;
    }


    /**
     * Builds the string used in the actual localisation file
     * @param entry the singular entry of the map, e.g. a collection of all ship names entries
     * @return localisation file line
     */
    @Override
    public String getLocString(Map.Entry<String, String> entry) {
        return " " + entry.getKey()+ ": \"" + entry.getValue() + "\"";
    }

    /**
     * Factory method to generate the map
     */
    @Override
    public void generateCategoryEntries(){
        // If multiple groups fall into the same category, e.g. first names, bundle them
        if (multipleEntriesPossible) {
            while (rawMatcher.find()) {
                String list = rawMatcher.group();

                if (overheadMatcher.find()) {
                    String overhead = overheadMatcher.group();
                    list = list.replace(overhead, "");
                }

                this.setRandomRaw(list);

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

                    this.put(locPrefix.concat(key.toLowerCase()), value);
                }
            }
        } else {
            if (rawMatcher.find()) {
                String list = rawMatcher.group();

                if (overheadMatcher.find()) {
                    String overhead = overheadMatcher.group();
                    list = list.replace(overhead, "");
                }

                this.setRandomRaw(list);

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

                    this.put(locPrefix.concat(key.toLowerCase()), value);
                }
            }
        }
    }
}
