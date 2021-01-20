/*
 * <summary></summary>
 * <author>hankcs</author>
 * <email>me@hankcs.com</email>
 * <create-date>2015/5/15 10:23</create-date>
 *
 * <copyright file="ValueArray.java">
 * Copyright (c) 2003-2015, hankcs. All Right Reserved, http://www.hankcs.com/
 * </copyright>
 */
package org.appxi.hanlp.util;

/**
 * 对值数组的包装，可以方便地取下一个
 *
 * @author hankcs
 */
public class RawArray<V> {
    private V[] values;
    private int offset = 0;

    public RawArray() {
        this(null);
    }

    public RawArray(V[] values) {
        this.values = values;
    }

    public V nextValue() {
        if (null == values) return null;
        if (offset < 0 || offset > values.length) return null;
        return values[offset++];
    }

    public int length() {
        return null == values ? 0 : values.length;
    }

    public RawArray<V> setValues(V[] values) {
        this.values = values;
        return this;
    }
}
