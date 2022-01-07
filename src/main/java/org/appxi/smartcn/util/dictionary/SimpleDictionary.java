/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/9/9 22:30</create-date>
 *
 * <copyright file="CommonDictioanry.java" company="上海林原信息科技有限公司">
 * Copyright (c) 2003-2014, 上海林原信息科技有限公司. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信息科技有限公司 to get more information.
 * </copyright>
 */
package org.appxi.smartcn.util.dictionary;

import org.appxi.smartcn.util.trie.BinTrie;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import static org.appxi.smartcn.util.SmartCNHelper.logger;

/**
 * 可以调整大小的词典
 *
 * @author hankcs
 */
public abstract class SimpleDictionary<V> {
    public final BinTrie<V> trie = new BinTrie<>();

    public boolean load(InputStream stream) {
        if (null == stream) return false;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) parseLineIntoTrie(line, trie);
        } catch (Exception e) {
            logger.warn("load stream failed", e);
            return false;
        }
        return true;
    }

    protected abstract void parseLineIntoTrie(String line, BinTrie<V> trie);

    /**
     * 查询一个单词
     *
     * @param key
     * @return 单词对应的条目
     */
    public V get(String key) {
        return trie.get(key);
    }

    /**
     * 以我为主词典，合并一个副词典，我有的词条不会被副词典覆盖
     *
     * @param other 副词典
     */
    public void combine(SimpleDictionary<V> other) {
        for (Map.Entry<String, V> entry : other.trie.entrySet()) {
            if (trie.containsKey(entry.getKey())) continue;
            trie.put(entry.getKey(), entry.getValue());
        }
    }

    public void walkEntries(BiConsumer<String, V> action) {
        trie.walkEntries(action);
    }

    /**
     * 获取键值对集合
     *
     * @return
     */
    public Set<Map.Entry<String, V>> entrySet() {
        return trie.entrySet();
    }

    /**
     * 键集合
     *
     * @return
     */
    public Set<String> keySet() {
        return trie.keySet();
    }

    /**
     * 过滤部分词条
     *
     * @param filter 过滤器
     * @return 删除了多少条
     */
    public int remove(EntryFilter<V> filter) {
        int size = trie.size();
        for (Map.Entry<String, V> entry : entrySet()) {
            if (filter.accept(entry)) {
                trie.remove(entry.getKey());
            }
        }
        return size - trie.size();
    }

    /**
     * 向中加入单词
     *
     * @param key
     * @param value
     */
    public void add(String key, V value) {
        trie.put(key, value);
    }

    public int size() {
        return trie.size();
    }

    public interface EntryFilter<V> {
        boolean accept(Map.Entry<String, V> entry);
    }
}
