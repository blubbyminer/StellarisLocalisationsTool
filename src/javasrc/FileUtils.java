package javasrc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

public class FileUtils {

    // TODO: does not work with army names, need maybe extra method
    public static void replaceKeys(ArrayList<LocEntryMap> categories, String content, File original) {
        String newContent = content;

        for (LocEntryMap map : categories) {
            if (map.isEmpty()) continue;

            String toReplace = map.getPrimaryRaw();

            if (toReplace == null || toReplace.isEmpty()) {
                continue;
            }

            String keyList = "";
            int perLine = 0;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                keyList = keyList
                        .concat(entry.getKey())
                        .concat(" ");
                if (perLine == 5) {
                    keyList = keyList.concat("\n\t\t\t");
                    perLine = 0;
                }
                perLine++;
            }

            newContent = newContent.replace(toReplace, keyList);

            if (map.getSecondaryRaw() != null) {
                if (!map.getSecondaryRaw().isEmpty() || !map.getSecondaryRaw().isBlank()) {
                    String secondaryKeys = "";
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        // Check if the entry is in secondaryRaw, mind the differing SequentialKey flavour

                        for (SequentialKeys key : SequentialKeys.values()) {
                            if (entry.getValue().contains(key.label)) {
                                secondaryKeys = entry.getValue()
                                        .replace(key.label, key.name())
                                        .replace("$", "%");
                            }
                        }
                    }
                    newContent = newContent.replace(map.getSecondaryRaw(), secondaryKeys);
                }
            }
        }

        writeToNamelistFile(original, newContent);
    }


    public static void writeToNamelistFile (File nameListFile, String content) {
        try (FileOutputStream fos = new FileOutputStream(nameListFile)){
            fos.write(content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }


    private static final byte[] BOM = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
    public static void writeToBOMFile(File file, String content)  {
        if (file.exists()) {
            System.out.println("File already exists, aborting!");
            return;
        }
        try (FileOutputStream fos = new FileOutputStream(file)){
            fos.write(BOM);
            fos.write(content.getBytes(StandardCharsets.UTF_8));}
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void writeToBOMFile(File file, LocEntryMap entries)  {
        String content = "l_english:\n" + StringTools.generateCategory("", "Ship Classes", entries);

        writeToBOMFile(file, content);
    }
}
