package javasrc;

import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractLocEntryMap<C> extends TreeMap<String, C> {
    final String category;

    @Nullable
    final String empireName;
    final String locPrefix;

    @Nullable
    public String getSequentialRaw() {
        return sequentialRaw;
    }

    public void setSequentialRaw(@Nullable String sequentialRaw) {
        this.sequentialRaw = sequentialRaw;
    }

    @Nullable
    public String getRandomRaw() {
        return randomRaw;
    }

    public void setRandomRaw(@Nullable String randomRaw) {
        this.randomRaw = randomRaw;
    }

    @Nullable
    protected String sequentialRaw;

    @Nullable
    protected String randomRaw;

    public AbstractLocEntryMap(String category, @Nullable String empireName, String locPrefix){
        this.category = category;
        this.empireName = empireName;
        this.locPrefix = StringTools.generateEmpirePrefix(this.empireName).concat(locPrefix).concat("_");
    }

    protected abstract void generateCategoryEntries();

    protected String generateBodyString() {
        String body = "";

        for (Map.Entry<String, C> entry : this.entrySet()){
            body = body.concat(this.getLocString(entry)).concat("\n");
        }

        return body;
    };

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

    public String generateCategoryString() {
        return generateHeaderString()
                .concat("\n")
                .concat(
                        generateBodyString()
                );
    }

    protected abstract String getLocString(Map.Entry<String, C> entry);
}