package com.u1tramarinet.mp3metaeditor.java.usecase;

import java.io.Serializable;
import java.util.Objects;

public class MP3FileDto implements Cloneable, Serializable {
    public long id;
    public String fileName;
    public String trackName;
    public String artistName;
    public String trackNumber;
    public String albumArtistName;
    public String albumName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MP3FileDto dto = (MP3FileDto) o;

        if (id != dto.id) return false;
        if (!Objects.equals(fileName, dto.fileName)) return false;
        if (!Objects.equals(trackName, dto.trackName)) return false;
        if (!Objects.equals(artistName, dto.artistName)) return false;
        if (!Objects.equals(trackNumber, dto.trackNumber)) return false;
        if (!Objects.equals(albumArtistName, dto.albumArtistName)) return false;
        return Objects.equals(albumName, dto.albumName);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + (trackName != null ? trackName.hashCode() : 0);
        result = 31 * result + (artistName != null ? artistName.hashCode() : 0);
        result = 31 * result + (trackNumber != null ? trackNumber.hashCode() : 0);
        result = 31 * result + (albumArtistName != null ? albumArtistName.hashCode() : 0);
        result = 31 * result + (albumName != null ? albumName.hashCode() : 0);
        return result;
    }

    @Override
    public MP3FileDto clone() {
        MP3FileDto dto = null;
        try {
            dto = (MP3FileDto) super.clone();
            dto.fileName = fileName;
            dto.trackName = trackName;
            dto.artistName = artistName;
            dto.trackNumber = trackNumber;
            dto.albumArtistName = albumArtistName;
            dto.albumName =albumName;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return dto;
    }
}
