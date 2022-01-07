package org.appxi.smartcn.util;

import org.appxi.prefs.UserPrefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public final class SmartCNHelper {
    public static final Logger logger = LoggerFactory.getLogger("appxi.smartcn");

    public static Path resolveData(String other) {
        return UserPrefs.dataDir().resolve(".smartcn-data").resolve(other);
    }

    public static Path resolveCache(String other) {
        return UserPrefs.dataDir().resolve(".smartcn-cache").resolve(other);
    }

    private SmartCNHelper() {
    }
}
