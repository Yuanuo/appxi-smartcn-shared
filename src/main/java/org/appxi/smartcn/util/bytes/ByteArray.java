package org.appxi.smartcn.util.bytes;

import static org.appxi.smartcn.util.SmartCNHelper.logger;

/**
 * 对字节数组进行封装，提供方便的读取操作
 *
 * @author hankcs
 */
public class ByteArray {
    /**
     * 当前字节数组，不一定是全部字节，可能只是一个片段
     */
    byte[] bytes;
    /**
     * 当前已读取的字节数，或下一个字节的指针
     */
    int offset;

    public ByteArray(byte[] bytes) {
        this.bytes = bytes;
    }

    /**
     * 获取全部字节
     *
     * @return
     */
    public byte[] getBytes() {
        return bytes;
    }

    /**
     * 读取一个int
     *
     * @return
     */
    public int nextInt() {
        int result = ByteHelper.bytesHighFirstToInt(bytes, offset);
        offset += 4;
        return result;
    }

    public double nextDouble() {
        double result = ByteHelper.bytesHighFirstToDouble(bytes, offset);
        offset += 8;
        return result;
    }

    /**
     * 读取一个char，对应于writeChar
     *
     * @return
     */
    public char nextChar() {
        char result = ByteHelper.bytesHighFirstToChar(bytes, offset);
        offset += 2;
        return result;
    }

    public char[] nextChars(int size) {
        final char[] result = new char[size];
        for (int i = 0; i < size; i++) {
            result[i] = ByteHelper.bytesHighFirstToChar(bytes, offset);
            offset += 2;
        }
        return result;
    }

    /**
     * 读取一个字节
     *
     * @return
     */
    public byte nextByte() {
        return bytes[offset++];
    }

    /**
     * 读取一个布尔值
     *
     * @return
     */
    public boolean nextBoolean() {
        return nextByte() == 1;
    }

    public boolean hasMore() {
        return offset < bytes.length;
    }

    /**
     * 读取一个String，注意这个String是双字节版的，在字符之前有一个整型表示长度
     *
     * @return
     */
    public String nextString() {
//        char[] buffer = new char[nextInt()];
//        for (int i = 0; i < buffer.length; ++i)
//        {
//            buffer[i] = nextChar();
//        }
        return new String(nextChars(nextInt()));
    }

    public String[] nextStrings(int size) {
        final String[] result = new String[size];
        for (int i = 0; i < size; ++i) {
            result[i] = new String(nextChars(nextInt()));
        }
        return result;
    }

    public float nextFloat() {
        float result = ByteHelper.bytesHighFirstToFloat(bytes, offset);
        offset += 4;
        return result;
    }

    /**
     * 读取一个无符号短整型
     *
     * @return
     */
    public int nextUnsignedShort() {
        byte a = nextByte();
        byte b = nextByte();
        return (((a & 0xff) << 8) | (b & 0xff));
    }

    /**
     * 读取一个UTF字符串
     *
     * @return
     */
    public String nextUTF() {
        int utflen = nextUnsignedShort();
        byte[] bytearr = null;
        char[] chararr = null;
        bytearr = new byte[utflen];
        chararr = new char[utflen];

        int c, char2, char3;
        int count = 0;
        int chararr_count = 0;

        for (int i = 0; i < utflen; ++i) {
            bytearr[i] = nextByte();
        }

        while (count < utflen) {
            c = (int) bytearr[count] & 0xff;
            if (c > 127) break;
            count++;
            chararr[chararr_count++] = (char) c;
        }

        while (count < utflen) {
            c = (int) bytearr[count] & 0xff;
            switch (c >> 4) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    /* 0xxxxxxx*/
                    count++;
                    chararr[chararr_count++] = (char) c;
                    break;
                case 12:
                case 13:
                    /* 110x xxxx   10xx xxxx*/
                    count += 2;
                    if (count > utflen)
                        logger.error("malformed input: partial character at end");
                    char2 = bytearr[count - 1];
                    if ((char2 & 0xC0) != 0x80)
                        logger.error("malformed input around byte " + count);
                    chararr[chararr_count++] = (char) (((c & 0x1F) << 6) |
                            (char2 & 0x3F));
                    break;
                case 14:
                    /* 1110 xxxx  10xx xxxx  10xx xxxx */
                    count += 3;
                    if (count > utflen)
                        logger.error("malformed input: partial character at end");
                    char2 = bytearr[count - 2];
                    char3 = bytearr[count - 1];
                    if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80))
                        logger.error("malformed input around byte " + (count - 1));
                    chararr[chararr_count++] = (char) (((c & 0x0F) << 12) |
                            ((char2 & 0x3F) << 6) |
                            ((char3 & 0x3F) << 0));
                    break;
                default:
                    /* 10xx xxxx,  1111 xxxx */
                    logger.error("malformed input around byte " + count);
            }
        }
        // The number of chars produced may be less than utflen
        return new String(chararr, 0, chararr_count);
    }

    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return bytes.length;
    }

    /**
     * 通知执行关闭/销毁操作
     */
    public void close() {
        bytes = null;
    }

    @Override
    protected void finalize() throws Throwable {
        // 如果忘记close，则在垃圾回收器释放内存的时候close，总好过完全不close
        close();
    }
}