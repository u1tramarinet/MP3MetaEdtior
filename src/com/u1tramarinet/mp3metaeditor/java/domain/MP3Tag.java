package com.u1tramarinet.mp3metaeditor.java.domain;

import java.util.Objects;

public class MP3Tag {
    public String trackName;
    public String artistName;
    public String trackNumber;
    public String albumArtistName;
    public String albumName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MP3Tag tag = (MP3Tag) o;

        if (!Objects.equals(trackName, tag.trackName)) return false;
        if (!Objects.equals(artistName, tag.artistName)) return false;
        if (!Objects.equals(trackNumber, tag.trackNumber)) return false;
        if (!Objects.equals(albumArtistName, tag.albumArtistName))
            return false;
        return Objects.equals(albumName, tag.albumName);
    }

    @Override
    public int hashCode() {
        int result = trackName != null ? trackName.hashCode() : 0;
        result = 31 * result + (artistName != null ? artistName.hashCode() : 0);
        result = 31 * result + (trackNumber != null ? trackNumber.hashCode() : 0);
        result = 31 * result + (albumArtistName != null ? albumArtistName.hashCode() : 0);
        result = 31 * result + (albumName != null ? albumName.hashCode() : 0);
        return result;
    }
}
