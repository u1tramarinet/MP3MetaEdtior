package com.u1tramarinet.mp3metaeditor.java.usecase;

public class SelectFileUseCase extends UseCase {

    public SelectFileUseCase() {
        super();
    }

    public void select(long id) {
        manager.selectMP3File(id);
    }

    public void unselect() {
        manager.unselectMP3File();
    }
}
