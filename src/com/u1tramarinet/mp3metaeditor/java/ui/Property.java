package com.u1tramarinet.mp3metaeditor.java.ui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;

public class Property<T> {
    private final ObjectProperty<T> valueProperty = new SimpleObjectProperty<>(null);
    private final BooleanProperty valueModified = new SimpleBooleanProperty(false);
    private final ObjectProperty<T> originalProperty = new SimpleObjectProperty<>(null);

    public ObjectProperty<T> valueProperty() {
        return valueProperty;
    }

    public T get() {
        return valueProperty.get();
    }

    public void set(T value) {
        valueProperty.set(value);
    }

    public T getOriginal() {
        return originalProperty.get();
    }

    public void setOriginal(T value) {
        originalProperty.set(value);
        valueProperty.set(value);
    }

    public BooleanProperty valueModifiedProperty() {
        valueModified.bind(getValueModified());
        return valueModified;
    }

    protected ObservableValue<Boolean> getValueModified() {
        return originalProperty.isNotEqualTo(valueProperty);
    }
}
