package com.u1tramarinet.mp3metaeditor.java.infrastructure;

import com.u1tramarinet.mp3metaeditor.java.domain.MP3Tag;
import com.u1tramarinet.mp3metaeditor.java.domain.MP3TagEditor;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;

public class MP3TagEditorImpl implements MP3TagEditor {
    public MP3TagEditorImpl() {

    }

    @Override
    public MP3Tag read(String fileName) {
        File file = new File(fileName);
        MP3Tag mp3Tag = new MP3Tag();
        try {
            AudioFile audioFile = AudioFileIO.read(file);
            Tag tag = audioFile.getTag();
            mp3Tag.trackName = getField(tag, FieldKey.TITLE);
            mp3Tag.artistName = getField(tag, FieldKey.ARTIST);
            mp3Tag.trackNumber = getField(tag, FieldKey.TRACK);
            mp3Tag.albumArtistName = getField(tag, FieldKey.ALBUM_ARTIST);
            mp3Tag.albumName = getField(tag, FieldKey.ALBUM);
        } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
            e.printStackTrace();
        }
        return mp3Tag;
    }

    @Override
    public boolean write(String fileName, MP3Tag mp3tag) {
        File file = new File(fileName);
        try {
            AudioFile audioFile = AudioFileIO.read(file);
            Tag tag = audioFile.getTag();
            tag.setField(FieldKey.TITLE, mp3tag.trackName);
            tag.setField(FieldKey.ARTIST, mp3tag.artistName);
            tag.setField(FieldKey.TRACK, mp3tag.trackNumber);
            tag.setField(FieldKey.ALBUM_ARTIST, mp3tag.albumArtistName);
            tag.setField(FieldKey.ALBUM, mp3tag.albumName);
            audioFile.setTag(tag);
            audioFile.commit();
        } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException | CannotWriteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private String getField(Tag tag, FieldKey key) {
        return tag.getFirst(key);
    }

}
