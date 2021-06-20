package com.u1tramarinet.mp3metaeditor.java.usecase;

import java.util.concurrent.Executor;

public abstract class ListenUseCase<T> extends UseCase {
    private final Executor executor;

    public ListenUseCase() {
        this(null);
    }

    public ListenUseCase(Executor executor) {
        this.executor = executor;
    }

    public final void startToListen(Callback<T> callback) {
        runOnExecutorIfNeeded(() -> this.startToListenLocked(callback));
    }

    protected abstract void startToListenLocked(Callback<T> callback);

    public final void finishToListen() {
        runOnExecutorIfNeeded(this::finishToListenLocked);
    }

    public abstract void finishToListenLocked();

    protected void runOnExecutorIfNeeded(Runnable r) {
        if (null != executor) {
            executor.execute(r);
        } else {
            r.run();
        }
    }

    public interface Callback<D> {
        void onSuccess(D data);

        void onFailure();
    }
}
