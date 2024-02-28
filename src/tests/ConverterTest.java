package tests;

import javasrc.LocEntryMap;
import junit.framework.TestCase;
import javasrc.Converter;

import static tests.TestStringConstants.*;

public class ConverterTest extends TestCase {
    LocEntryMap testMap = new LocEntryMap(TEST_CATEGORY_NAME);

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        for (int i = 1; i <= 50; i++) {
            String value = "test" + i;
            String key = Converter.generatePrefix(TEST_EMPIRE_NAME, "Test", 3) + value.toLowerCase();
            testMap.put(key, value);
        }
    }
    private LocEntryMap setupMap(int length, String categoryKey, String category) {
        LocEntryMap map = new LocEntryMap(category);
        for (int i = 1; i <= length; i++) {
            String value = "Test" + i;
            String key = Converter.generatePrefix(TEST_EMPIRE_NAME, categoryKey, 3) + value.toLowerCase();
            map.put(key, value);
        }

        return map;
    }

    public void testGenerateCategoryHeadDefault() {
        assertEquals(
                SAMPLE_TEST_CATEGORY,
                Converter.generateCategoryCommentHeader(
                        TEST_EMPIRE_NAME,
                        TEST_CATEGORY_NAME)
        );
    }

    public void testGeneratePrefixDefault() {
        assertEquals(TEST_KEY_PREFIX, Converter.generatePrefix(TEST_EMPIRE_NAME, "Test", 3));
    }
}
