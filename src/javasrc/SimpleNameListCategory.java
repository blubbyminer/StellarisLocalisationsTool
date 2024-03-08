package javasrc;

import java.util.regex.Pattern;

public class SimpleNameListCategory extends NameListCategory{
    public Pattern getRawPattern() {
        return rawPattern;
    }

    public Pattern getOverhead() {
        return overhead;
    }

    private final Pattern rawPattern;
    private final Pattern overhead;

    public boolean isMultipleEntriesPossible() {
        return multipleEntriesPossible;
    }
    private final boolean multipleEntriesPossible;
    public SimpleNameListCategory(String empireName, String empirePrefix, String categoryName, String categoryLoc, Pattern rawPattern, Pattern overhead, boolean multipleEntriespossible){
        super(empireName, empirePrefix, categoryName, categoryLoc);
        this.rawPattern = rawPattern;
        this.overhead = overhead;
        this.multipleEntriesPossible = multipleEntriespossible;
    }

}
