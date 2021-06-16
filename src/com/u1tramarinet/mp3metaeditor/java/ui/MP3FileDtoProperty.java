package com.u1tramarinet.mp3metaeditor.java.ui;

import com.u1tramarinet.mp3metaeditor.java.usecase.MP3FileDto;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;

import java.util.Objects;

public class MP3FileDtoProperty extends Property<MP3FileDto> {
    public final Property<String> fileNameProperty = new Property<>();
    public final Property<String> trackNameProperty = new Property<>();
    public final Property<String> artistNameProperty = new Property<>();
    public final Property<String> trackNumberProperty = new Property<>();
    public final Property<String> albumArtistNameProperty = new Property<>();
    public final Property<String> albumNameProperty = new Property<>();

    @Override
    public ObjectProperty<MP3FileDto> valueProperty() {
        return super.valueProperty();
    }

    @Override
    public void set(MP3FileDto value) {
        super.set(value);
        if (Objects.nonNull(value)) {
            setValues(value);
        } else {
            clearValues();
        }
    }

    @Override
    public void setOriginal(MP3FileDto value) {
        super.setOriginal(value);
        if (Objects.nonNull(value)) {
            setOriginalValues(value);
        } else {
            clearOriginalValues();
        }
    }

    @Override
    protected ObservableValue<Boolean> getValueModified() {
        return ((BooleanBinding) super.getValueModified())
                .or(fileNameProperty.valueModifiedProperty())
                .or(trackNameProperty.valueModifiedProperty())
                .or(artistNameProperty.valueModifiedProperty())
                .or(trackNumberProperty.valueModifiedProperty())
                .or(albumArtistNameProperty.valueModifiedProperty())
                .or(albumNameProperty.valueModifiedProperty());
    }

    private void setValues(MP3FileDto value) {
        fileNameProperty.set(value.fileName);
        trackNameProperty.set(value.trackName);
        artistNameProperty.set(value.artistName);
        trackNumberProperty.set(value.trackNumber);
        albumArtistNameProperty.set(value.albumArtistName);
        albumNameProperty.set(value.albumName);
    }

    private void clearValues() {
        fileNameProperty.set(null);
        trackNameProperty.set(null);
        artistNameProperty.set(null);
        trackNumberProperty.set(null);
        albumArtistNameProperty.set(null);
        albumNameProperty.set(null);
    }

    private void setOriginalValues(MP3FileDto value) {
        fileNameProperty.setOriginal(value.fileName);
        trackNameProperty.setOriginal(value.trackName);
        artistNameProperty.setOriginal(value.artistName);
        trackNumberProperty.setOriginal(value.trackNumber);
        albumArtistNameProperty.setOriginal(value.albumArtistName);
        albumNameProperty.setOriginal(value.albumName);
    }

    private void clearOriginalValues() {
        fileNameProperty.setOriginal(null);
        trackNameProperty.setOriginal(null);
        artistNameProperty.setOriginal(null);
        trackNumberProperty.setOriginal(null);
        albumArtistNameProperty.setOriginal(null);
        albumNameProperty.setOriginal(null);
    }
}
