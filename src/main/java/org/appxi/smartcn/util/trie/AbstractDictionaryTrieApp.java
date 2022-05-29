package org.appxi.smartcn.util.trie;

import org.appxi.smartcn.util.SmartCNHelper;

public abstract class AbstractDictionaryTrieApp<V> {
    private final Object syncLock = new Object();
    private DoubleArrayTrieByAhoCorasick<V> trie;

    public final DoubleArrayTrieByAhoCorasick<V> getDictionaryTrie() {
        if (null != trie)
            return trie;
        synchronized (syncLock) {
            if (null != trie)
                return trie;
            final long st = System.currentTimeMillis();
            final DoubleArrayTrieByAhoCorasick<V> dataTrie = new DoubleArrayTrieByAhoCorasick<>();
            loadDictionaries(dataTrie);
            this.trie = dataTrie;
            SmartCNHelper.logger.info("getDictionaryTrie used after: " + (System.currentTimeMillis() - st));
        }
        return trie;
    }

    protected abstract void loadDictionaries(DoubleArrayTrieByAhoCorasick<V> trie);

    public final V getValue(String key) {
        return getDictionaryTrie().get(key);
    }
}
