package com.u1tramarinet.mp3metaeditor.java.domain;

import java.io.File;
import java.util.Objects;

public class MP3File {
    public final long id;
    public final File file;
    private MP3Tag tag;

    public MP3File(long id, String fileName, MP3Tag tag) {
        this(id, new File(fileName), tag);
    }

    public MP3File(long id, File file, MP3Tag tag) {
        this.id = id;
        this.file = file;
        this.tag = tag;
    }

    public void setTag(MP3Tag tag) {
        if (null == tag) return;
        this.tag = tag;
    }

    public MP3Tag getTag() {
        return this.tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MP3File mp3File = (MP3File) o;

        return Objects.equals(file, mp3File.file);
    }

    @Override
    public int hashCode() {
        return file != null ? file.hashCode() : 0;
    }
}
