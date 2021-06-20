package com.u1tramarinet.mp3metaeditor.java.usecase;

import com.u1tramarinet.mp3metaeditor.java.domain.MP3File;
import com.u1tramarinet.mp3metaeditor.java.domain.MP3FileManager;

import java.util.Objects;
import java.util.concurrent.Executor;

public class ListenFileUpdateUseCase extends ListenUseCase<MP3FileDto> {
    private MP3FileManager.Callback<MP3File> callback;

    public ListenFileUpdateUseCase() {
        super();
    }

    public ListenFileUpdateUseCase(Executor callbackExecutor) {
        super(callbackExecutor);
    }

    @Override
    public void startToListenLocked(Callback<MP3FileDto> callback) {
        this.callback = new MP3FileManager.Callback<>() {
            @Override
            public void onUpdateCompleted(MP3File data) {
                runOnExecutorIfNeeded(() -> notifyOnSuccess(data, callback));
            }

            @Override
            public void onUpdateFailed(Exception e) {
                runOnExecutorIfNeeded(() -> notifyOnFailure(callback));
            }
        };
        manager.addFileCallback(this.callback);
    }

    @Override
    public void finishToListenLocked() {
        manager.removeFileCallback(this.callback);
    }

    private void notifyOnSuccess(MP3File file, Callback<MP3FileDto> callback) {
        if (Objects.nonNull(callback)) {
            callback.onSuccess(convertToDto(file));
        }
    }

    private void notifyOnFailure(Callback<MP3FileDto> callback) {
        if (Objects.nonNull(callback)) {
            callback.onFailure();
        }
    }

    private MP3FileDto convertToDto(MP3File file) {
        if (null == file) return null;
        MP3FileDto dto = new MP3FileDto();
        dto.id = file.id;
        dto.fileName = file.file.getName();
        dto.albumArtistName = file.getTag().albumArtistName;
        dto.albumName = file.getTag().albumName;
        dto.trackName = file.getTag().trackName;
        dto.trackNumber = file.getTag().trackNumber;
        dto.artistName = file.getTag().artistName;
        return dto;
    }
}
