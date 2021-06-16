package com.u1tramarinet.mp3metaeditor.java.usecase;

import java.io.File;
import java.util.List;

public class RegisterFilesUseCase extends UseCase {

    public void register(List<File> files) {
        manager.registerFiles(files);
    }

    public void unregister() {
        manager.unregisterFiles();
    }
}
