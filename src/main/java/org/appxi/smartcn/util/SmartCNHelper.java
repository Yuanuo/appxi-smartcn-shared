package org.appxi.smartcn.util;

import org.appxi.prefs.UserPrefs;
import org.appxi.util.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public final class SmartCNHelper {
    public static final Logger logger = LoggerFactory.getLogger("appxi.smartcn");

    static {
        // 删除旧版数据
        FileHelper.deleteDirectory(SmartCNHelper.resolveData("appxi"));
        FileHelper.deleteDirectory(SmartCNHelper.resolveCache("appxi"));
    }

    public static Path resolveData(String other) {
        return UserPrefs.dataDir().resolve(".smartcn-data").resolve(other);
    }

    public static Path resolveCache(String other) {
        return UserPrefs.dataDir().resolve(".smartcn-cache").resolve(other);
    }

    private SmartCNHelper() {
    }
}
