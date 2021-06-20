package com.u1tramarinet.mp3metaeditor.java.ui;

import com.u1tramarinet.mp3metaeditor.java.usecase.MP3FileDto;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.function.Function;

class MP3FileDtoDiffProperty implements PropertyBase<MP3FileDto>{
    final MP3FileDtoProperty value = new MP3FileDtoProperty();
    final BooleanProperty valueModifiedProperty = new SimpleBooleanProperty(false);
    final MP3FileDtoProperty originalValue = new MP3FileDtoProperty();

    public MP3FileDtoDiffProperty() {
        valueModifiedProperty.bind(isNotEqualTo(v -> v.fileNameProperty)
                .or(isNotEqualTo(v -> v.trackNameProperty)
                .or(isNotEqualTo(v -> v.artistNameProperty)
                .or(isNotEqualTo(v -> v.trackNumberProperty)
                .or(isNotEqualTo(v -> v.albumArtistNameProperty)
                .or(isNotEqualTo(v -> v.albumNameProperty)))))));
    }

    private BooleanBinding isNotEqualTo(Function<MP3FileDtoProperty, ObjectProperty<String>> command) {
        return command.apply(value).isNotEqualTo(command.apply(originalValue));
    }

    @Override
    public void set(MP3FileDto value) {
        originalValue.set(value);
        this.value.set(value);
    }

    @Override
    public void reset() {
        value.set(originalValue.get());
    }
}
