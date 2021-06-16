package com.u1tramarinet.mp3metaeditor.java.usecase;

import com.u1tramarinet.mp3metaeditor.java.domain.MP3FileManager;

class UseCase {
    protected final MP3FileManager manager;

    UseCase() {
        manager = MP3FileManager.getInstance();
    }
}
