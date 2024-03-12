package javasrc;

import java.util.Map;

public class SequentialLocEntryMap extends AbstractLocEntryMap<String>{
    final String key;
    SequentialKeys type;
    final String raw;
    public SequentialLocEntryMap(String category, String categoryLoc, String empireName, String key, String raw) {
        super(category, empireName, categoryLoc);
        this.key = key;
        this.raw = raw;
    }

    @Override
    public void generateCategoryEntries() {
        String entry = StringTools.extractSequentialNames(raw);
        this.type = SequentialKeys.valueOf(StringTools.extractSequentialKey(raw));
        this.put(key, entry);

    }

    @Override
    protected String generateBodyString() {
        return null;
    }

    @Override
    public String getLocString(Map.Entry<String, String> entry) {
        return " " + entry.getKey() + ": \"" + entry.getValue();
    }
}
