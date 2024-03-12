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
    private String primaryRaw;
    public String getPrimaryRaw() {
        return primaryRaw;
    }
    public void setPrimaryRaw(String primaryRaw) {
        this.primaryRaw = primaryRaw;
    }


    private String secondaryRaw;
    public String getSecondaryRaw() {
        return secondaryRaw;
    }
    public void setSecondaryRaw(String secondaryRaw) {
        this.secondaryRaw = secondaryRaw;
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

    public RandomLocEntryMap(@NotNull String category, String categoryLoc, String empireName, Matcher rawMatcher, Matcher overheadMatcher, String primaryRaw, boolean multipleEntriesPossible) {
        super(category, empireName, categoryLoc);
        this.rawMatcher = rawMatcher;
        this.overheadMatcher = overheadMatcher;
        this.multipleEntriesPossible = multipleEntriesPossible;

        this.primaryRaw = primaryRaw;
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

    @Override
    public String generateBodyString() {
        String body = "";

        for (Map.Entry<String, String> entry : this.entrySet()){
            body = body.concat(this.getLocString(entry)).concat("\n");
        }

        return body;
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

                this.setPrimaryRaw(list);

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

                this.setPrimaryRaw(list);

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
