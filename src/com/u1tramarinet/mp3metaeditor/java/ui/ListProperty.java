package com.u1tramarinet.mp3metaeditor.java.ui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

class ListProperty<T> implements PropertyBase<ObservableList<T>> {
    final javafx.beans.property.ListProperty<T> valueProperty = new SimpleListProperty<>(FXCollections.observableArrayList(new ArrayList<>()));
    final BooleanProperty valueModifiedProperty = new SimpleBooleanProperty(false);
    private final javafx.beans.property.ListProperty<T> originalValueProperty = new SimpleListProperty<>(FXCollections.observableArrayList(new ArrayList<>()));

    ListProperty() {
        valueProperty.bind(originalValueProperty);
        valueModifiedProperty.bind(originalValueProperty.isNotEqualTo(valueProperty));
    }

    @Override
    public void set(ObservableList<T> value) {
        valueProperty.clear();
        valueProperty.addAll(value);
    }

    @Override
    public void reset() {
        valueProperty.set(originalValueProperty.get());
    }
}
