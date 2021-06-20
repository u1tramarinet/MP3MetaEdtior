package com.u1tramarinet.mp3metaeditor.java.ui;

import com.u1tramarinet.mp3metaeditor.java.usecase.MP3FileDto;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

class MP3FileDtoProperty extends SimpleObjectProperty<MP3FileDto> {
    final ObjectProperty<String> fileNameProperty = new SimpleObjectProperty<>("");
    final ObjectProperty<String> trackNameProperty = new SimpleObjectProperty<>("");
    final ObjectProperty<String> artistNameProperty = new SimpleObjectProperty<>("");
    final ObjectProperty<String> trackNumberProperty = new SimpleObjectProperty<>("");
    final ObjectProperty<String> albumArtistNameProperty = new SimpleObjectProperty<>("");
    final ObjectProperty<String> albumNameProperty = new SimpleObjectProperty<>("");
    private boolean applying = false;

    MP3FileDtoProperty() {
        super();
        bindValues();
    }

    MP3FileDtoProperty(MP3FileDto value) {
        super(value);
        applyValue(value);
        bindValues();
    }

    @Override
    public void set(MP3FileDto value) {
        super.set(value);
        applyValue(value);
    }

    private void bindValues() {
        fileNameProperty.addListener((observableValue, oldValue, newValue) -> reflectValue());
        trackNameProperty.addListener((observableValue, oldValue, newValue) -> reflectValue());
        artistNameProperty.addListener((observableValue, oldValue, newValue) -> reflectValue());
        trackNumberProperty.addListener((observableValue, oldValue, newValue) -> reflectValue());
        albumArtistNameProperty.addListener((observableValue, oldValue, newValue) -> reflectValue());
        albumNameProperty.addListener((observableValue, oldValue, newValue) -> reflectValue());
    }

    private void applyValue(MP3FileDto value) {
        applying = true;
        if (null != value) {
            setProperties(value);
        } else {
            clearProperties();
        }
        applying = false;
    }

    private void setProperties(MP3FileDto value) {
        fileNameProperty.set(value.fileName);
        trackNameProperty.set(value.trackName);
        artistNameProperty.set(value.artistName);
        trackNumberProperty.set(value.trackNumber);
        albumArtistNameProperty.set(value.albumArtistName);
        albumNameProperty.set(value.albumName);
    }

    private void clearProperties() {
        fileNameProperty.set(null);
        trackNameProperty.set(null);
        artistNameProperty.set(null);
        trackNumberProperty.set(null);
        albumArtistNameProperty.set(null);
        albumNameProperty.set(null);
    }

    private void reflectValue() {
        if (applying) return;
        get().fileName = fileNameProperty.get();
        get().trackName = trackNameProperty.get();
        get().artistName = artistNameProperty.get();
        get().trackNumber = trackNumberProperty.get();
        get().albumArtistName = albumArtistNameProperty.get();
        get().albumName = albumNameProperty.get();
    }
}
