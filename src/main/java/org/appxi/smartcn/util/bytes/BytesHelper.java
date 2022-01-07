package org.appxi.smartcn.util.bytes;

import org.appxi.smartcn.util.SmartCNHelper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public interface BytesHelper {

    static ByteArray createByteArray(Path file) {
        try {
            final byte[] bytes = Files.readAllBytes(file);
            return new ByteArray(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static byte[] readBytes(Path file) {
        try {
            return Files.readAllBytes(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static int readBytes(InputStream stream, byte[] target) {
        try {
            return stream.read(target);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    static ByteArrayStream createByteArrayStream(String path) {
        return createByteArrayStream(SmartCNHelper.resolveData(path));
    }

    static ByteArrayStream createByteArrayStream(Path file) {
        try {
            return ByteArrayFileStream.createByteArrayFileStream(new FileInputStream(file.toFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        if (IOAdapter == null) return ByteArrayFileStream.createByteArrayFileStream(path);
//
//        try {
//            InputStream is = IOAdapter.open(path);
//            if (is instanceof FileInputStream)
//                return ByteArrayFileStream.createByteArrayFileStream((FileInputStream) is);
//            return ByteArrayOtherStream.createByteArrayOtherStream(is);
//        } catch (IOException e) {
//            logger.warning("打开失败：" + path);
//        }
        return null;
    }
}
