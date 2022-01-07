module appxi.smartcn.shared {
    requires transitive appxi.shared;

    exports org.appxi.smartcn.chars;
    exports org.appxi.smartcn.util;
    exports org.appxi.smartcn.util.bytes;
    exports org.appxi.smartcn.util.dictionary;
    exports org.appxi.smartcn.util.trie;
}