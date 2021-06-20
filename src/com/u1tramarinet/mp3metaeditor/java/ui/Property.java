package com.u1tramarinet.mp3metaeditor.java.ui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

class Property<T> implements PropertyBase<T> {
    final ObjectProperty<T> valueProperty = new SimpleObjectProperty<>(null);
    final BooleanProperty valueModifiedProperty = new SimpleBooleanProperty(false);
    protected final ObjectProperty<T> originalValueProperty = new SimpleObjectProperty<>(null);

    Property() {
        valueProperty.bind(originalValueProperty);
        valueModifiedProperty.bind(valueProperty.isNotEqualTo(originalValueProperty));
    }

    @Override
    public void set(T value) {
        originalValueProperty.set(value);
    }

    public void reset() {
        valueProperty.set(originalValueProperty.get());
    }
}
