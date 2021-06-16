package com.u1tramarinet.mp3metaeditor.java.usecase;

import com.u1tramarinet.mp3metaeditor.java.domain.MP3File;
import com.u1tramarinet.mp3metaeditor.java.domain.MP3FileManager;

import java.util.Objects;

public abstract class ListenUseCase<D> extends UseCase {
    public abstract void startToListen(Callback<D> callback);

    public abstract void finishToListen();

    public interface Callback<D> {
        void onSuccess(D data);

        void onFailure();
    }
}
