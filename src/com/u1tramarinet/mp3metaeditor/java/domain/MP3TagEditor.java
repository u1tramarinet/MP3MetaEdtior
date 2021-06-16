package com.u1tramarinet.mp3metaeditor.java.domain;

public interface MP3TagEditor {
    MP3Tag read(String fileName);
    boolean write(String fileName, MP3Tag tag);
}
