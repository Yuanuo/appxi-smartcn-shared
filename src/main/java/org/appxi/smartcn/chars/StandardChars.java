package org.appxi.smartcn.chars;

import org.appxi.smartcn.util.SmartCNHelper;
import org.appxi.util.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class StandardChars {
    private static final Logger logger = LoggerFactory.getLogger(StandardChars.class);

    private static char[] CHARS;

    static {
        // 删除旧版数据
        FileHelper.deleteDirectory(SmartCNHelper.resolveCache("chars"));
        reload();
    }

    public static void reload() {
        final long st = System.currentTimeMillis();
        char[] data = null;
        try {
            // default
            URLConnection txtFileDefault = null;
            try {
                txtFileDefault = StandardChars.class.getResource("data.txt").openConnection();
            } catch (Exception e) {
                logger.warn("should never here", e);
            }
            // user managed
            final Path txtFileManaged = SmartCNHelper.resolveData("chars.txt");
            // cache file
            final Path binFile = SmartCNHelper.resolveCache("chars.bin");
            // 检查缓存bin文件是否需要重建
            if (!FileHelper.isTargetFileUpdatable(binFile, txtFileDefault, txtFileManaged)) {
                try {
                    // load from bin
                    data = FileHelper.readObject(binFile);
                } catch (Throwable ignore) {
                    try {
                        Files.deleteIfExists(binFile);
                    } catch (IOException ignored) {
                    }
                } finally {
                    logger.info("loadBin used time: " + (System.currentTimeMillis() - st));
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
                // 加载默认数据
                _load(finalMappings, txtFileDefault);
                // 加载管理的数据，可以覆盖默认数据
                _load(finalMappings, txtFileManaged);

                logger.info("caching standard-chars to:" + binFile);
                FileHelper.writeObject(finalMappings, binFile);
            }
        } finally {
            CHARS = data;
            logger.info("standard-chars loaded in " + (System.currentTimeMillis() - st) + " ms");
        }
    }

    private static void _load(char[] data, Object source) {
        if (null == source) {
            logger.warn("source is null");
            return;
        }

        String sourcePath = null;
        InputStream stream = null;
        if (source instanceof Path path && FileHelper.exists(path)) {
            sourcePath = path.toString();
            try {
                stream = Files.newInputStream(path);
            } catch (IOException e) {
                logger.warn("load chars from Path failed", e);
            }
        } else if (source instanceof URLConnection urlConn) {
            sourcePath = urlConn.getURL().toString();
            try {
                stream = urlConn.getInputStream();
            } catch (IOException e) {
                logger.warn("load chars from URL failed", e);
            }
        }

        if (null != stream) {
            try (InputStream inputStream = stream) {
                FileHelper.lines(inputStream, StandardCharsets.UTF_8, line -> {
                    if (line.length() >= 3)
                        data[line.charAt(0)] = data[line.charAt(2)];
                    return false; // don't break
                });
            } catch (Exception e) {
                logger.warn(sourcePath, e);
            }
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
