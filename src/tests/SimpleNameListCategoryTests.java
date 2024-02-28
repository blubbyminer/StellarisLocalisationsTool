package tests;

import javasrc.SimpleNameListCategory;
import junit.framework.TestCase;

import java.util.regex.Pattern;

public class SimpleNameListCategoryTests extends TestCase {
    public void testBuildLocPrefix() {
        SimpleNameListCategory category = new SimpleNameListCategory(
                "Test",
                "TES",
                "Test Category",
                "SHIP",
                Pattern.compile(""),
                Pattern.compile(""),
                false);

        assertEquals("TES_SHIP_", category.buildLocPrefix());
    }
}
