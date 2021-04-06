package com.u1tramarinet.mp3metaeditor.model;

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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MP3FileManager {
    private static final MP3FileManager INSTANCE = new MP3FileManager();
    private final List<File> files = new ArrayList<>();
    private final List<MP3File> mp3Files = new ArrayList<>();
    private final Executor executor = Executors.newSingleThreadExecutor();

    private MP3FileManager() {

    }

    public static MP3FileManager getInstance() {
        return INSTANCE;
    }

    public void registerFiles(List<File> files, Callback<List<MP3File>> callback) {
        runOnWorkerThread(() -> {
            MP3FileManager.this.files.clear();
            MP3FileManager.this.files.addAll(files);
            MP3FileManager.this.mp3Files.clear();
            for (File file : files) {
                MP3FileManager.this.mp3Files.add(convertToMP3FileLocked(file));
            }
            notifySuccessIfNotNull(callback, mp3Files);
        });
    }

    public void requestMP3Files(Callback<List<MP3File>> callback) {
        runOnWorkerThread(() -> notifySuccessIfNotNull(callback, mp3Files));
    }

    public void clearFiles(Callback<List<MP3File>> callback) {
        runOnWorkerThread(() -> {
            MP3FileManager.this.files.clear();
            MP3FileManager.this.mp3Files.clear();
            notifySuccessIfNotNull(callback, mp3Files);
        });
    }

    public void updateFile(MP3File mp3File, Callback<File> callback) {
        runOnWorkerThread(() -> {
            if (mp3File == null) return;
            for (int i = 0; i < files.size(); i++) {
                File file = files.get(i);
                String fileName = removeExtension(file.getName());
                if (fileName.equals(mp3File.fileName)) {
                    reflectToFileLocked(mp3File, file);
                    files.set(i, file);
                    notifySuccessIfNotNull(callback, file);
                    return;
                }
            }
        });
    }

    public void updateFiles(List<MP3File> mp3Files, Callback<List<MP3File>> callback) {
        runOnWorkerThread(() -> {
            if ((mp3Files == null) || mp3Files.isEmpty()) return;
            for (MP3File mp3File : mp3Files) {
                for (int i = 0; i < files.size(); i++) {
                    File file = files.get(i);
                    String fileName = removeExtension(file.getName());
                    if (fileName.equals(mp3File.fileName)) {
                        reflectToFileLocked(mp3File, file);
                        files.set(i, file);
                        break;
                    }
                }
            }
            notifySuccessIfNotNull(callback, mp3Files);
        });
    }

    private void reflectToFileLocked(MP3File mp3File, File file) {
        try {
            AudioFile audioFile = AudioFileIO.read(file);
            Tag tag = audioFile.getTag();
            tag.setField(FieldKey.TITLE, mp3File.trackName);
            tag.setField(FieldKey.ARTIST, mp3File.artistName);
            tag.setField(FieldKey.TRACK, String.valueOf(mp3File.trackNumber));
            tag.setField(FieldKey.ALBUM_ARTIST, mp3File.albumArtistName);
            tag.setField(FieldKey.ALBUM, mp3File.albumName);
            audioFile.setTag(tag);
            audioFile.commit();
        } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException | CannotWriteException e) {
            e.printStackTrace();
        }
    }

    private MP3File convertToMP3FileLocked(File file) {
        MP3File mp3File = new MP3File();
        mp3File.fileName = removeExtension(file.getName());
        try {
            AudioFile audioFile = AudioFileIO.read(file);
            Tag tag = audioFile.getTag();
            mp3File.trackName = tag.getFirst(FieldKey.TITLE);
            mp3File.artistName = tag.getFirst(FieldKey.ARTIST);
            String trackNumberStr = tag.getFirst(FieldKey.TRACK);
            mp3File.trackNumber = (trackNumberStr == null || "".equals(trackNumberStr)) ? null : Integer.parseInt(trackNumberStr);
            mp3File.albumArtistName = tag.getFirst(FieldKey.ALBUM_ARTIST);
            mp3File.albumName = tag.getFirst(FieldKey.ALBUM);
        } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
            e.printStackTrace();
        }
        return mp3File;
    }

    private void runOnWorkerThread(Runnable r) {
        executor.execute(r);
    }

    private <T> void notifySuccessIfNotNull(Callback<T> callback, T data) {
        if (callback != null) {
            callback.onSuccess(data);
        }
    }

    private String removeExtension(String fileName) {
        int lastDotIdx = fileName.lastIndexOf('.');
        return (lastDotIdx <= 0) ? fileName : fileName.substring(0, lastDotIdx);
    }

    public static class Callback<T> {
        public void onSuccess(T data) {

        }

        public void onFailure(Exception e) {

        }
    }
}
