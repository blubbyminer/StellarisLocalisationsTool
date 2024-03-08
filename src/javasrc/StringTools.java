package javasrc;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTools {
    public static final String category_header = "# §§§ $$$ #";

    /**
     * General regex to extract space divided name lists into a string array
     * @param raw the namelist
     * @return A singular name
     */
    public static ArrayList<String> extractNames(String raw) {
        Pattern namesPattern = Pattern.compile("\"(.*?)\"|(\\S+)");
        Matcher namesMatcher = namesPattern.matcher(raw);

        ArrayList<String> namesOutput = new ArrayList<>();

        while (namesMatcher.find()){
            String group = namesMatcher.group();

            if (Pattern.compile("[A-Z]+_").matcher(group).find()) continue;

            namesOutput.add(group);
        }

        return namesOutput;
    }


    public static String extractRandomNames(String content) throws IllegalArgumentException {
        Pattern randomPattern = Pattern.compile("random_names = \\{\\s+(.+)\\s+\\}");
        Matcher randomMatcher = randomPattern.matcher(content);

        if (randomMatcher.find()) {
            return randomMatcher.group()
                    .replace("random_names = {", "")
                    .replace("}", "");
        } else throw new IllegalArgumentException("No matches for provided String found!");
    }


    public static String extractSequentialNames(String content) throws IllegalArgumentException {
        Pattern sequentialPattern = Pattern.compile("sequential_name = (.+)");
        Matcher sequentialMatcher = sequentialPattern.matcher(content);

        if (sequentialMatcher.find()) {
            String outerSequential = sequentialMatcher.group();

            String innerSequential = outerSequential
                    .replace("sequential_name = ", "")
                    .replace("\"", "")
                    .replace("{", "")
                    .replace("}", "");

            String oldKey = innerSequential.subSequence(
                    innerSequential.indexOf("%")+1,
                    innerSequential.lastIndexOf("%")
            ).toString();

            String newKey = SequentialKeys.valueOf(oldKey).label;

            return innerSequential
                    .replace(oldKey, newKey)
                    .replace("%", "$");
        } else throw new IllegalArgumentException("No matches for provided String found!");
    }

    public static String extractSequentialKey(String content) throws IllegalArgumentException {
        if (content != null && ! content.isEmpty() && ! content.isBlank()) {
            return content.substring(
                    content.indexOf("$")+1,
                    content.lastIndexOf("$")
            );
        } else throw new IllegalArgumentException("No extraction of key string possible!");
    }


    /**
     * Generates the comment header for the loc file
     * @param empireName Name of the empire
     * @param category Name of the category
     * @return String of the category header
     */
    public static String generateCategoryCommentHeader(String empireName, String category) {
        String middle = "";

        if (empireName.isBlank() || empireName.isEmpty()) {
            middle = StringTools.category_header.replace("§§§ ", "").replace("$$$", category);
        } else {
            middle = StringTools.category_header.replace("§§§", empireName).replace("$$$", category);
        }

        StringBuilder topAndBottom = new StringBuilder();

        int length = middle.length();

        topAndBottom.append("#".repeat(length));

        return topAndBottom + "\n" + middle + "\n" + topAndBottom;
    }

    public static String generateCategory(String empireName, String category, LocEntryMap entries) {
        if (entries.isEmpty()) return "";

        return generateCategoryCommentHeader(empireName, category)
                .concat("\n")
                .concat(
                        entries.generateCategoryBody()
                );
    }
}
