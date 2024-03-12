package javasrc;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompositeLocEntryMap extends AbstractLocEntryMap<String>{
    // order of sequential and random names does not matter
    private final String combinedPattern = "(?:sequential_name = (.+)\\s+random_names = \\{\\s+(.*)\\s+\\}|random_names = \\{\\s+(.*)\\s+\\}\\s+sequential_name = (.+))";
    private final String sequentialOnlyPattern = "sequential_name = (.+)";
    private final String randomOnlyPattern = "random_names = \\{\\s+(.*)";

    final String key;
    final String identifierPattern;

    final String content;

    public CompositeLocEntryMap(String category, String empireName, String locPrefix, String key, String identifierPattern, String content) {
        super(category, empireName, locPrefix);
        this.key = key;
        this.identifierPattern = identifierPattern;
        this.content = content;
    }

    @Override
    public void generateCategoryEntries() {
        String compositePattern = identifierPattern + combinedPattern;
        String sequentialPattern = identifierPattern + sequentialOnlyPattern;
        String randomPattern = identifierPattern + randomOnlyPattern;

        Matcher compositeMatcher = Pattern.compile(compositePattern).matcher(content);
        Matcher sequentialMathcer = Pattern.compile(sequentialPattern).matcher(content);
        Matcher randomMatcher = Pattern.compile(randomPattern).matcher(content);

        boolean isProcessed = false;

        while (compositeMatcher.find()) {
            String result = compositeMatcher.group();
            sequentialRaw = result;

            insertSequentialNames(result);


            String randomNames = StringTools.extractRandomNames(result);
            randomRaw = randomNames;

            insertRandomNames(randomNames);

            isProcessed = true;
        }

        while (sequentialMathcer.find() && ! isProcessed) {
            String result = sequentialMathcer.group();

            sequentialRaw = result;

            insertSequentialNames(result);
        }

        while (randomMatcher.find() && ! isProcessed) {
            String result = randomMatcher.group();
            randomRaw = result;

            insertRandomNames(result);
        }
    }

    private void insertSequentialNames(String result) {
        String locValue = StringTools.extractSequentialNames(result);
        String sequentialKey = StringTools.extractSequentialKey(locValue);

        String key = locPrefix.concat(sequentialKey);

        put(key, locValue);
    }

    private void insertRandomNames(String raw) {
        ArrayList<String> names = StringTools.extractNames(raw);

        names.forEach(name -> {
            String cleanName = name.replace("\"", "");
            String key = locPrefix
                    .concat(
                            StringUtils.stripAccents(cleanName)
                                    .replace(" ", "_")
                                    .toLowerCase()
                    );

            put(key, cleanName);
        });
    }

    @Override
    public String getLocString(Map.Entry<String, String> entry) {
        return " " + entry.getKey()+ ": \"" + entry.getValue() + "\"";
    }
}
