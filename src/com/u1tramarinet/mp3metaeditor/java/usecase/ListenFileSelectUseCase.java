package com.u1tramarinet.mp3metaeditor.java.usecase;

import java.util.concurrent.Executor;

public class ListenFileSelectUseCase extends ListenUseCase<MP3FileDto> {

    public ListenFileSelectUseCase() {
        super();
    }

    public ListenFileSelectUseCase(Executor executor) {
        super(executor);
    }

    @Override
    public void startToListenLocked(Callback<MP3FileDto> callback) {

    }

    @Override
    public void finishToListenLocked() {

    }
}
