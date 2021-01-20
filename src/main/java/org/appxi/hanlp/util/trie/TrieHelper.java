package org.appxi.hanlp.util.trie;

import org.appxi.hanlp.util.HanlpHelper;
import org.appxi.hanlp.util.bytes.ByteArray;
import org.appxi.hanlp.util.bytes.BytesHelper;
import org.appxi.util.FileHelper;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

public interface TrieHelper {
    static boolean loadObject(Path file, DoubleArrayTrieByAhoCorasick<String> trie) {
        final long st = System.currentTimeMillis();
        try {
            final ByteArray byteArray = BytesHelper.createByteArray(file);
            if (null != byteArray) {
                trie.load(byteArray, byteArray.nextStrings(byteArray.nextInt()));
                return true;
            }
        } finally {
            HanlpHelper.LOG.info("loadBin used times: " + (System.currentTimeMillis() - st));
        }
        return false;
    }

    static boolean saveObject(DoubleArrayTrieByAhoCorasick<String> trie, TreeMap<String, String> vals, Path file) {
        final long st = System.currentTimeMillis();
        if (trie.size() == vals.size()) {
            FileHelper.makeParents(file);
            try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(Files.newOutputStream(file)))) {
                out.writeInt(vals.size());
                String value;
                for (Map.Entry<String, String> entry : vals.entrySet()) {
                    value = entry.getValue();
                    out.writeInt(value.length());
                    out.writeChars(value);
                }
                trie.save(out);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        HanlpHelper.LOG.info("saveBin used times: " + (System.currentTimeMillis() - st));
        return false;
    }
}
