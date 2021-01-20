package org.appxi.hanlp.chars;

import org.appxi.hanlp.util.HanlpHelper;
import org.appxi.util.FileHelper;

import java.nio.file.Path;

public class StandardChars {
    private static final String pathTxt = "/appxi/hanlpChars/data.txt";
    private static final String pathBin = pathTxt.replace(".txt", ".bin").substring(1);

    private static char[] CHARS;

    static {
        reload();
    }

    public static void reload() {
        final long st = System.currentTimeMillis();
        char[] data = null;
        try {
            final Path fileBin = HanlpHelper.resolveCache(pathBin);
            if (FileHelper.exists(fileBin)) {
                try {
                    data = FileHelper.readObject(fileBin);
                } catch (Exception ignore) {
                    FileHelper.delete(fileBin);
                }
            }
            if (null == data) {
                data = new char[Character.MAX_VALUE + 1];
                for (int i = 0; i < data.length; i++) {
                    data[i] = (char) i;
                }
                final char[] finalMappings = data;
                FileHelper.lines(StandardChars.class.getResourceAsStream(pathTxt), line -> {
                    if (line.length() == 3)
                        finalMappings[line.charAt(0)] = finalMappings[line.charAt(2)];
                    return false; // don't break
                });
                // loadSpace
                for (int i = Character.MIN_CODE_POINT; i <= Character.MAX_CODE_POINT; i++) {
                    if (Character.isWhitespace(i) || Character.isSpaceChar(i)) {
                        finalMappings[i] = ' ';
                    }
                }
                HanlpHelper.LOG.info("caching chars to:" + fileBin);
                FileHelper.writeObject(finalMappings, fileBin);
            }
        } finally {
            CHARS = data;
            HanlpHelper.LOG.info("standard chars loaded in " + (System.currentTimeMillis() - st) + " ms");
        }
    }

    public static char[] chars() {
        return CHARS;
    }

    private StandardChars() {
    }

    public static char convert(char c) {
        return CHARS[c];
    }

    public static char[] convert(char[] chars) {
        final char[] result = new char[chars.length];
        for (int i = 0; i < chars.length; i++) {
            result[i] = CHARS[chars[i]];
        }
        return result;
    }

    public static String convert(String string) {
        return new String(convert(string.toCharArray()));
    }

    public static void convertTo(String string, char[] result) {
        final char[] chars = string.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            result[i] = CHARS[chars[i]];
        }
    }

    public static void convertMe(char[] chars) {
        for (int i = 0; i < chars.length; i++) {
            chars[i] = CHARS[chars[i]];
        }
    }
}
