package javasrc;

import java.util.Map;
import java.util.TreeMap;

/**
 * Use this with the following structure:
 * key: localisation key, e.g. IMP_SHIP_monitor
 * value: localisation value, e.g. "Monitor" (note, *with* the parentheses!)
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
    public static String getLocString(Map.Entry<String, String> entry) {
        return " " + entry.getKey()+ ": \"" + entry.getValue() + "\"";
    }

}
