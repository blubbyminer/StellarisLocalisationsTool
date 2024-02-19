import java.util.Map;
import java.util.TreeMap;

/**
 * Use this with the following structure:
 * key: localisation key, e.g. IMP_SHIP_monitor
 * value: localisation value, e.g. "Monitor" (note, *with* the parentheses!)
 */
public class LocEntryMap extends TreeMap<String, String> {
    final String category;

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    private String raw;

    public LocEntryMap (String category) {
        this.category = category;
    }

    public LocEntryMap (String category, String raw) {
        this.category = category;
        this.raw = raw;
    }

    /**
     * Builds the string used in the actual localisation file
     * @param entry the singular entry of the map, e.g. a collection of all ship names entries
     * @return localisation file line
     */
    public static String GetLocString(Map.Entry<String, String> entry) {
        return " " + entry.getKey()+ ": \"" + entry.getValue() + "\"";
    }

}
