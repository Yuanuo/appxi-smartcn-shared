package org.appxi.hanlp.util.trie;

import org.appxi.hanlp.util.HanlpHelper;

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
            loadDictionaries(trie = new DoubleArrayTrieByAhoCorasick<>());
            HanlpHelper.LOG.info("getDictionaryTrie used times: " + (System.currentTimeMillis() - st));
        }
        return trie;
    }

    protected abstract void loadDictionaries(DoubleArrayTrieByAhoCorasick<V> trie);

    public final V getValue(String key) {
        return getDictionaryTrie().get(key);
    }
}
