package javasrc;

import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractLocEntryMap<C> extends TreeMap<String, C> {
    final String category;

    @Nullable
    final String empireName;
    final String locPrefix;

    public AbstractLocEntryMap(String category, @Nullable String empireName, String locPrefix){
        this.category = category;
        this.empireName = empireName;
        this.locPrefix = StringTools.generateEmpirePrefix(this.empireName).concat(locPrefix).concat("_");
    }

    protected abstract void generateCategoryEntries();

    protected String generateHeaderString() {
        String middle = "";

        if (empireName == null || empireName.isBlank() || empireName.isEmpty()) {
            middle = StringTools.category_header.replace("§§§ ", "").replace("$$$", category);
        } else {
            middle = StringTools.category_header.replace("§§§", empireName).replace("$$$", category);
        }

        StringBuilder topAndBottom = new StringBuilder();

        int length = middle.length();

        topAndBottom.append("#".repeat(length));

        return topAndBottom + "\n" + middle + "\n" + topAndBottom;
    }
    protected abstract String generateBodyString();

    public String generateCategoryString() {
        return generateHeaderString()
                .concat("\n")
                .concat(
                        generateBodyString()
                );
    }

    protected abstract String getLocString(Map.Entry<String, C> entry);
}