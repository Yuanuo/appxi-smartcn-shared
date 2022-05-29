package org.appxi.smartcn.chars;

import org.appxi.smartcn.util.SmartCNHelper;
import org.appxi.util.FileHelper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.appxi.smartcn.util.SmartCNHelper.logger;

public class StandardChars {
    private static char[] CHARS;

    static {
        reload();
    }

    public static void reload() {
        final long st = System.currentTimeMillis();
        char[] data = null;
        try {
            final Path binFile = SmartCNHelper.resolveCache("chars/data.bin");
            if (Files.exists(binFile)) {
                try {
                    data = FileHelper.readObject(binFile);
                } catch (Throwable ignore) {
                    try {
                        Files.deleteIfExists(binFile);
                    } catch (IOException ignored) {
                    }
                }
            }
            if (null == data) {
                data = new char[Character.MAX_VALUE];
                for (int i = 0; i < data.length; i++) {
                    data[i] = (char) i;
                    if (Character.isWhitespace(i) || Character.isSpaceChar(i)) {
                        data[i] = ' ';
                    }
                }
                final char[] finalMappings = data;
                FileHelper.lines(StandardChars.class.getResourceAsStream("data.txt"), StandardCharsets.UTF_8, line -> {
                    if (line.length() >= 3)
                        finalMappings[line.charAt(0)] = finalMappings[line.charAt(2)];
                    return false; // don't break
                });
                logger.info("caching standard-chars to:" + binFile);
                FileHelper.writeObject(finalMappings, binFile);
            }
        } finally {
            CHARS = data;
            logger.info("standard-chars loaded in " + (System.currentTimeMillis() - st) + " ms");
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

    public static void convertInto(String string, char[] result) {
        final char[] chars = string.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            result[i] = CHARS[chars[i]];
        }
    }

    public static void convertThis(char[] chars) {
        for (int i = 0; i < chars.length; i++) {
            chars[i] = CHARS[chars[i]];
        }
    }
}
