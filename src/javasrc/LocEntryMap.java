package javasrc;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;

import static javasrc.StringTools.extractNames;

/**
 * Use this with the following structure:
 * key: localisation key, e.g. IMP_SHIP_monitor
 * value: localisation value, e.g. "Monitor" (note, <b>without</b> the parentheses!)
 */
public class LocEntryMap extends TreeMap<String, String> {
    final String category;

    public String getPrimaryRaw() {
        return primaryRaw;
    }
    public void setPrimaryRaw(String primaryRaw) {
        this.primaryRaw = primaryRaw;
    }

    private String primaryRaw;


    public String getSecondaryRaw() {
        return secondaryRaw;
    }
    public void setSecondaryRaw(String secondaryRaw) {
        this.secondaryRaw = secondaryRaw;
    }

    private String secondaryRaw;

    public LocEntryMap (String category) {
        this.category = category;
    }

    public LocEntryMap (String category, String primaryRaw) {
        this.category = category;
        this.primaryRaw = primaryRaw;
    }

    /**
     * Builds the string used in the actual localisation file
     * @param entry the singular entry of the map, e.g. a collection of all ship names entries
     * @return localisation file line
     */
    private String getLocString(Map.Entry<String, String> entry) {
        return " " + entry.getKey()+ ": \"" + entry.getValue() + "\"";
    }

    public String generateCategoryBody() {
        String body = "";

        for (Map.Entry<String, String> entry : this.entrySet()){
            body = body.concat(this.getLocString(entry)).concat("\n");
        }

        return body;
    }

    public static LocEntryMap generateCategoryEntries(NameListCategory category, String content) {
        if (category instanceof SimpleNameListCategory) {
            return generateCategoryEntries(
                    ((SimpleNameListCategory) category).getRawPattern().matcher(content),
                    ((SimpleNameListCategory) category).getOverhead().matcher(content),
                    category.categoryName,
                    category.buildLocPrefix(),
                    ((SimpleNameListCategory) category).isMultipleEntriesPossible());
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
}
