package javasrc;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompositeLocEntryMap extends AbstractLocEntryMap<String>{
    SequentialLocEntryMap sequentialLocEntryMap;
    RandomLocEntryMap randomLocEntryMap;

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


    }

    @Override
    protected String generateBodyString() {
        return null;
    }

    @Override
    public String getLocString(Map.Entry<String, String> entry) {
        return null;
    }
}
