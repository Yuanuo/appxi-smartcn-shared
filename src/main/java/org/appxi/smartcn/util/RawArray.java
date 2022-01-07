package org.appxi.smartcn.util;

/**
 * 对值数组的包装，可以方便地取下一个
 *
 * @author hankcs
 */
public class RawArray<V> {
    private final V[] values;
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
}
