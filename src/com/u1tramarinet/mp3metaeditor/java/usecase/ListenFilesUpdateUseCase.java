package com.u1tramarinet.mp3metaeditor.java.usecase;

import com.u1tramarinet.mp3metaeditor.java.domain.MP3File;
import com.u1tramarinet.mp3metaeditor.java.domain.MP3FileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

public class ListenFilesUpdateUseCase extends ListenUseCase<List<MP3FileDto>> {
    private MP3FileManager.Callback<List<MP3File>> callback;
    private final Executor callbackExecutor;

    public ListenFilesUpdateUseCase() {
        this(null);
    }

    public ListenFilesUpdateUseCase(Executor callbackExecutor) {
        super();
        this.callbackExecutor = callbackExecutor;
    }

    @Override
    public void startToListen(Callback<List<MP3FileDto>> callback) {
        this.callback = new MP3FileManager.Callback<>() {
            @Override
            public void onUpdateCompleted(List<MP3File> data) {
                if (Objects.nonNull(callbackExecutor)) {
                    callbackExecutor.execute(() -> notifyOnSuccess(data, callback));
                } else {
                    notifyOnSuccess(data, callback);
                }
            }

            @Override
            public void onUpdateFailed(Exception e) {
                notifyOnFailure(callback);
            }
        };
        manager.addFilesCallback(this.callback);
    }

    @Override
    public void finishToListen() {
        manager.removeFilesCallback(callback);
    }

    private void notifyOnSuccess(List<MP3File> files, Callback<List<MP3FileDto>> callback) {
        if (Objects.nonNull(callback)) {
            callback.onSuccess(Converter.convertMP3FileToDtoAll(files));
        }
    }

    private void notifyOnFailure(Callback<List<MP3FileDto>> callback) {
        if (Objects.nonNull(callback)) {
            callback.onFailure();
        }
    }
}
