package com.u1tramarinet.mp3metaeditor.java.ui;

import com.u1tramarinet.mp3metaeditor.java.usecase.ListenFileUpdateUseCase;
import com.u1tramarinet.mp3metaeditor.java.usecase.MP3FileDto;
import com.u1tramarinet.mp3metaeditor.java.usecase.UnitSetUseCase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class UnitSetController implements Initializable {
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
    private final ListenFileUpdateUseCase listenFileUpdateUseCase = new ListenFileUpdateUseCase();
    private final MP3FileDtoProperty inputValue = new MP3FileDtoProperty();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindTextPropertyBidirectional(inputValue.fileNameProperty.valueProperty(), fileName);
        bindTextPropertyBidirectional(inputValue.trackNameProperty.valueProperty(), trackName);
        bindTextPropertyBidirectional(inputValue.artistNameProperty.valueProperty(), artistName);
        bindTextPropertyBidirectional(inputValue.trackNumberProperty.valueProperty(), trackNumber);
        bindTextPropertyBidirectional(inputValue.albumArtistNameProperty.valueProperty(), albumArtistName);
        bindTextPropertyBidirectional(inputValue.albumNameProperty.valueProperty(), albumName);
        bindDisableProperty(inputValue.valueProperty().isNull(),
                fileName, trackName, artistName, trackNumber, albumArtistName, albumName,
                fileNameLabel, trackLabel, artistLabel, trackNumberLabel, albumArtistLabel, albumLabel);
        bindDisableProperty(inputValue.valueModifiedProperty().not(),
                undoButton, unitSettingButton);
        undoButton.setOnMouseClicked(mouseEvent -> undoTrackInfo());
        unitSettingButton.setOnMouseClicked(mouseEvent -> submitTrackInfo());
        listenFileUpdateUseCase.startToListen(new ListenFileUpdateUseCase.Callback() {
            @Override
            public void onSuccess(MP3FileDto file) {
                updateMP3File(file);
            }

            @Override
            public void onFailure() {

            }
        });
    }

    public void updateMP3File(MP3FileDto fileDto) {
        if (null == fileDto) {
            clearTrackInfo();
            return;
        }
        updateTrackInfo(fileDto);
    }

    private void updateTrackInfo(MP3FileDto file) {
        inputValue.setOriginal(file);
        inputValue.set(file);
    }

    private void clearTrackInfo() {
        inputValue.setOriginal(null);
        inputValue.set(null);
    }

    private void undoTrackInfo() {
        updateTrackInfo(inputValue.getOriginal());
    }

    private void submitTrackInfo() {
        unitSetUseCase.updateTag(inputValue.get());
    }

    private void bindTextPropertyBidirectional(ObjectProperty<String> property, TextField textField) {
        textField.textProperty().bindBidirectional(property);
    }

    private void bindDisableProperty(ObservableValue<Boolean> value, Node... nodes) {
        for (Node node : nodes) {
            node.disableProperty().bind(value);
        }
    }
}
