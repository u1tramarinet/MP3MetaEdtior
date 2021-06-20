package com.u1tramarinet.mp3metaeditor.java.ui;

import com.u1tramarinet.mp3metaeditor.java.usecase.MP3FileDto;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;

abstract class ChildControllerBase extends ControllerBase {
    protected final ObjectProperty<MP3FileDto> selectedFileProperty = new SimpleObjectProperty<>();
    protected final javafx.beans.property.ListProperty<MP3FileDto> fileListProperty = new SimpleListProperty<>();

    protected void bindFileProperties(ObjectProperty<MP3FileDto> selectedFileProperty, javafx.beans.property.ListProperty<MP3FileDto> fileListProperty) {
        this.selectedFileProperty.bindBidirectional(selectedFileProperty);
        this.fileListProperty.bindBidirectional(fileListProperty);
    }
}
