package org.appxi.hanlp.util;

import org.appxi.prefs.UserPrefs;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;

import static org.appxi.util.FileHelper.exists;
import static org.appxi.util.FileHelper.makeParents;

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

    public static Path ensureFileExtracted(Function<String, InputStream> streamGetter, String dataFile) {
        final Path target = resolveData(dataFile);
        if (exists(target))
            return target;
        makeParents(target);
        //
        final Path localFile = Paths.get(dataFile);
        if (exists(localFile)) {
            try {
                Files.copy(localFile, target);
                return target;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //
        try (InputStream stream = streamGetter.apply(dataFile)) {
            if (null != stream) {
                Files.copy(stream, target);
                return target;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Path> ensureFilesExtracted(Function<String, InputStream> streamGetter, String... dataFiles) {
        final List<Path> result = new ArrayList<>(dataFiles.length);
        Path extracted;
        for (String dataFile : dataFiles) {
            extracted = ensureFileExtracted(streamGetter, dataFile);
            if (null != extracted)
                result.add(extracted);
        }
        return result;
    }

    public static List<Path> ensureFilesExtracted(Function<String, InputStream> streamGetter, Collection<String> dataFiles) {
        return ensureFilesExtracted(streamGetter, dataFiles.toArray(new String[0]));
    }
}
