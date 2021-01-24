package org.appxi.hanlp.util;

import org.appxi.prefs.UserPrefs;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

public final class HanlpHelper {
    public static final Logger LOG = Logger.getLogger("HANLP");

    public static Path dataDir() {
        return UserPrefs.dataDir().resolve(".hanlp-data");
    }

    public static Path cacheDir() {
        return UserPrefs.dataDir().resolve(".hanlp-cache");
    }

    public static Path resolveData(String other) {
        return dataDir().resolve(other);
    }

    public static Path resolveCache(String other) {
        return cacheDir().resolve(other);
    }

    private HanlpHelper() {
    }

    public static InputStream ensureStream(Path path) {
        try {
            return Files.newInputStream(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
