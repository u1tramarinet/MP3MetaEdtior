package com.u1tramarinet.mp3metaeditor.java.domain;

import com.u1tramarinet.mp3metaeditor.java.infrastructure.MP3TagEditorImpl;

import java.io.File;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class MP3FileManager {
    private static final MP3FileManager INSTANCE = new MP3FileManager();
    private final List<MP3File> mp3Files = new ArrayList<>();
    private MP3File mp3File = null;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final List<Callback<List<MP3File>>> filesCallbacks = new ArrayList<>();
    private final List<Callback<MP3File>> fileCallbacks = new ArrayList<>();
    private final MP3TagEditor mp3TagEditor = new MP3TagEditorImpl();

    private MP3FileManager() {

    }

    public static MP3FileManager getInstance() {
        return INSTANCE;
    }

    public void registerFiles(List<File> files) {
        runOnWorkerThread(() -> registerFilesLocked((null != files) ? files : Collections.emptyList()));
    }

    private void registerFilesLocked(List<File> files) {
        List<MP3File> mp3Files = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            mp3Files.add(new MP3File(i, file, mp3TagEditor.read(file.getAbsolutePath())));
        }
        registerMP3FilesLocked(mp3Files);
    }

    private void registerMP3FilesLocked(List<MP3File> mp3Files) {
        this.mp3Files.clear();
        this.mp3Files.addAll(mp3Files);
        notifyOnFilesUpdateCompleted(this.mp3Files);
    }

    public void unregisterFiles() {
        runOnWorkerThread(this::unregisterMP3FilesLocked);
    }

    private void unregisterMP3FilesLocked() {
        MP3FileManager.this.mp3Files.clear();
        notifyOnFilesUpdateCompleted(mp3Files);
    }

    public void selectMP3File(long id) {
        if (null != mp3File && id == mp3File.id) return;
        for (MP3File mp3f : mp3Files) {
            if (id == mp3f.id) {
                mp3File = mp3f;
                notifyOnFileUpdateCompleted(mp3f);
                return;
            }
        }
    }

    public void unselectMP3File() {
        if (null == mp3File) return;
        mp3File = null;
        notifyOnFileUpdateCompleted(null);
    }

    public void updateMP3Tag(long id, MP3Tag mp3Tag) {
        if (null == mp3File) return;
        runOnWorkerThread(() -> updateMP3TagLocked(id, mp3Tag));
    }

    public void updateMP3Tags(Map<Long, MP3Tag> mp3TagMap) {
        if (null == mp3TagMap) return;
        runOnWorkerThread(() -> updateMP3TagsLocked(mp3TagMap));
    }

    public void addFileCallback(Callback<MP3File> callback) {
        Optional.ofNullable(callback).ifPresent(fileCallbacks::add);
    }

    public void removeFileCallback(Callback<MP3File> callback) {
        Optional.ofNullable(callback).ifPresent(fileCallbacks::remove);
    }

    public void addFilesCallback(Callback<List<MP3File>> callback) {
        Optional.ofNullable(callback).ifPresent(filesCallbacks::add);
    }

    public void removeFilesCallback(Callback<List<MP3File>> callback) {
        Optional.ofNullable(callback).ifPresent(filesCallbacks::remove);
    }

    private void updateMP3TagLocked(long id, MP3Tag mp3Tag) {
        boolean result = false;
        for (MP3File mp3f : mp3Files) {
            if (id == mp3f.id) {
                result = updateMP3TagLockedInternal(id, mp3Tag);
            }
        }

        if (result) {
            notifyOnFileUpdateCompleted(mp3File);
            notifyOnFilesUpdateCompleted(mp3Files);
        } else {
            notifyOnFileUpdateFailed(new Exception());
            notifyOnFilesUpdateFailed(new Exception());
        }
    }

    private boolean updateMP3TagLockedInternal(long id, MP3Tag mp3Tag) {
        for (int i = 0; i < mp3Files.size(); i++) {
            MP3File f = mp3Files.get(i);
            if (id == f.id) {
                if (null != mp3Tag && !mp3Tag.equals(f.getTag())) {
                    boolean result = mp3TagEditor.write(f.file.getAbsolutePath(), mp3Tag);
                    if (result) {
                        f.setTag(mp3Tag);
                        mp3Files.set(i, f);
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    private void updateMP3TagsLocked(Map<Long, MP3Tag> mp3TagMap) {
        boolean handled = false;
        for (Long key : mp3TagMap.keySet()) {
            handled = handled || updateMP3TagLockedInternal(key, mp3TagMap.get(key));
        }

        if (handled) {
            notifyOnFilesUpdateCompleted(mp3Files);
        }
    }

    private void runOnWorkerThread(Runnable r) {
        executor.execute(r);
    }

    private void notifyOnFileUpdateCompleted(MP3File data) {
        notifyFileCallback(mp3FileCallback -> mp3FileCallback.onUpdateCompleted(data));
    }

    private void notifyOnFilesUpdateCompleted(List<MP3File> data) {
        notifyFilesCallback(mp3FilesCallback -> mp3FilesCallback.onUpdateCompleted(data));
    }

    private void notifyOnFileUpdateFailed(Exception e) {
        notifyFileCallback(mp3FileCallback -> mp3FileCallback.onUpdateFailed(e));
    }

    private void notifyOnFilesUpdateFailed(Exception e) {
        notifyFilesCallback(mp3FilesCallback -> mp3FilesCallback.onUpdateFailed(e));
    }

    private void notifyFileCallback(Consumer<Callback<MP3File>> command) {
        for (Callback<MP3File> callback : fileCallbacks) {
            command.accept(callback);
        }
    }

    private void notifyFilesCallback(Consumer<Callback<List<MP3File>>> command) {
        for (Callback<List<MP3File>> callback : filesCallbacks) {
            command.accept(callback);
        }
    }

    public static class Callback<T> {
        public void onUpdateCompleted(T data) {

        }

        public void onUpdateFailed(Exception e) {

        }
    }
}
