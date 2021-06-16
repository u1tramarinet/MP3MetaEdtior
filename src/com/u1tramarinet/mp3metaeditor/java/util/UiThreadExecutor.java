package com.u1tramarinet.mp3metaeditor.java.util;

import javafx.application.Platform;

import java.util.concurrent.Executor;

public class UiThreadExecutor implements Executor {
    @Override
    public void execute(Runnable command) {
        Platform.runLater(command);
    }
}
