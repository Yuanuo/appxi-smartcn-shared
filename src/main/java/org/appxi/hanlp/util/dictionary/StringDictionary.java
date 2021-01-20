/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/11/1 19:53</create-date>
 *
 * <copyright file="StringDictionary.java" company="上海林原信息科技有限公司">
 * Copyright (c) 2003-2014, 上海林原信息科技有限公司. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信息科技有限公司 to get more information.
 * </copyright>
 */
package org.appxi.hanlp.util.dictionary;


import org.appxi.hanlp.util.trie.BinTrie;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.appxi.hanlp.util.HanlpHelper.LOG;

/**
 * 满足 key=value 格式的词典，其中“=”可以自定义
 *
 * @author hankcs
 */
public class StringDictionary extends SimpleDictionary<String> {
    /**
     * key value之间的分隔符
     */
    protected String separator;

    public StringDictionary() {
        this("=");
    }

    public StringDictionary(String separator) {
        this.separator = separator;
    }

    @Override
    protected void parseLineIntoTrie(String line, BinTrie<String> trie) {
        final String[] tmpArr = line.split(separator, 2);
        if (tmpArr.length == 2)
            trie.put(tmpArr[0], tmpArr[1]);
    }

    /**
     * 保存词典
     *
     * @param path
     * @return 是否成功
     */
    public boolean save(Path path) {
        try (BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            for (Map.Entry<String, String> entry : trie.entrySet()) {
                bw.write(entry.getKey());
                bw.write(separator);
                bw.write(entry.getValue());
                bw.newLine();
            }
        } catch (Exception e) {
            LOG.warning("保存词典到" + path + "失败");
            return true;
        }
        return false;
    }

    /**
     * 将自己逆转过来返回
     *
     * @return
     */
    public StringDictionary reverse() {
        final StringDictionary result = new StringDictionary(separator);
        this.walkEntries((k, v) -> result.trie.put(v, k));
        return result;
    }
}
