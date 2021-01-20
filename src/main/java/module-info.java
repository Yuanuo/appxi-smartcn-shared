module appxi.hanlp.shared {
    requires java.logging;
    requires appxi.shared;

    exports org.appxi.hanlp.chars;
    exports org.appxi.hanlp.util;
    exports org.appxi.hanlp.util.bytes;
    exports org.appxi.hanlp.util.dictionary;
    exports org.appxi.hanlp.util.trie;
}