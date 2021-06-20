package com.u1tramarinet.mp3metaeditor.java.ui;

import com.u1tramarinet.mp3metaeditor.java.usecase.MP3FileDto;
import com.u1tramarinet.mp3metaeditor.java.usecase.UnitSetUseCase;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class UnitSetController extends ChildControllerBase {
    @FXML
    private TextField fileName;
    @FXML
    private TextField trackName;
    @FXML
    private TextField artistName;
    @FXML
    private TextField trackNumber;
    @FXML
    private TextField albumArtistName;
    @FXML
    private TextField albumName;
    @FXML
    private Button undoButton;
    @FXML
    private Button unitSettingButton;
    @FXML
    private Label fileNameLabel;
    @FXML
    private Label trackLabel;
    @FXML
    private Label artistLabel;
    @FXML
    private Label trackNumberLabel;
    @FXML
    private Label albumArtistLabel;
    @FXML
    private Label albumLabel;
    private final UnitSetUseCase unitSetUseCase = new UnitSetUseCase();
    private final MP3FileDtoDiffProperty inputValue = new MP3FileDtoDiffProperty();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindTextPropertyBidirectional(inputValue.value.fileNameProperty, fileName);
        bindTextPropertyBidirectional(inputValue.value.trackNameProperty, trackName);
        bindTextPropertyBidirectional(inputValue.value.artistNameProperty, artistName);
        bindTextPropertyBidirectional(inputValue.value.trackNumberProperty, trackNumber);
        bindTextPropertyBidirectional(inputValue.value.albumArtistNameProperty, albumArtistName);
        bindTextPropertyBidirectional(inputValue.value.albumNameProperty, albumName);
        bindDisableProperty(inputValue.value.isNull(),
                fileName, trackName, artistName, trackNumber, albumArtistName, albumName,
                fileNameLabel, trackLabel, artistLabel, trackNumberLabel, albumArtistLabel, albumLabel);
        bindDisableProperty(inputValue.valueModifiedProperty.not(),
                undoButton, unitSettingButton);
        undoButton.setOnMouseClicked(mouseEvent -> undoTrackInfo());
        unitSettingButton.setOnMouseClicked(mouseEvent -> submitTrackInfo());
        this.selectedFileProperty.addListener((observableValue, oldValue, newValue) -> updateMP3File(newValue));
    }

    @Override
    protected void bindFileProperties(ObjectProperty<MP3FileDto> selectedFileProperty, ListProperty<MP3FileDto> fileListProperty) {
        super.bindFileProperties(selectedFileProperty, fileListProperty);
    }

    private void updateMP3File(MP3FileDto fileDto) {
        inputValue.set(fileDto);
    }

    private void undoTrackInfo() {
        inputValue.reset();
    }

    private void submitTrackInfo() {
        unitSetUseCase.updateTag(inputValue.value.get());
    }
}
