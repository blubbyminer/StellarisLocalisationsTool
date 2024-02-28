package javasrc;

abstract class NameListCategory {
    final String empireName;
    final String empirePrefix;
    final String categoryName;
    final String categoryLoc;

    NameListCategory(String empireName, String empirePrefix, String categoryName, String categoryLoc){
        this.empireName = empireName;
        this.empirePrefix = empirePrefix;
        this.categoryName = categoryName;
        this.categoryLoc = categoryLoc;
    }
    public String buildLocPrefix() {
        return empirePrefix + "_" + categoryLoc + "_";
    }
}
