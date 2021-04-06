package com.u1tramarinet.mp3metaeditor.ui;

import com.u1tramarinet.mp3metaeditor.model.MP3File;
import com.u1tramarinet.mp3metaeditor.model.MP3FileManager;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Button fileButton;
    @FXML
    private Button resetButton;
    @FXML
    private ToggleButton darkMode;
    @FXML
    private ListView<MP3File> fileListView;
    @FXML
    private TextField fileName;
    @FXML
    private TextField trackName;
    @FXML
    private TextField artistName;
    @FXML
    private Button artistNameFullSetButton;
    @FXML
    private TextField trackNumber;
    @FXML
    private Button trackNumberFullSetButton;
    @FXML
    private TextField albumArtistName;
    @FXML
    private Button albumArtistNameFullSetButton;
    @FXML
    private TextField albumName;
    @FXML
    private Button albumNameFullSetButton;
    @FXML
    private Button trackSetButton;
    private final ObservableList<MP3File> listData = FXCollections.observableArrayList();
    private MP3File currentSelectedFile = null;
    private final MP3FileManager mp3FileManager = MP3FileManager.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileButton.setOnMouseClicked(mouseEvent -> chooseAndUpdateFileList());
        resetButton.setOnMouseClicked(mouseEvent -> updateFileList(null));
        fileListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<MP3File> call(ListView<MP3File> fileListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(MP3File file, boolean empty) {
                        super.updateItem(file, empty);
                        setText(((file == null) || empty) ? "" : file.fileName);
                    }
                };
            }
        });
        fileListView.setOnMouseClicked(mouseEvent -> updateFileInfo(fileListView.getSelectionModel().getSelectedItem()));
        fileListView.setItems(listData);

        artistNameFullSetButton.setOnMouseClicked(mouseEvent -> {
            if (isNotTrackSelected()) return;
            String artistNameStr = artistName.getText();
            mp3FileManager.requestMP3Files(new FullSetCallback() {
                @Override
                protected MP3File modify(MP3File datum, int index) {
                    datum.artistName = artistNameStr;
                    return datum;
                }
            });
        });
        trackNumberFullSetButton.setOnMouseClicked(mouseEvent -> {
            if (isNotTrackSelected()) return;
            mp3FileManager.requestMP3Files(new FullSetCallback() {
                @Override
                protected MP3File modify(MP3File datum, int index) {
                    datum.trackNumber = index + 1;
                    return datum;
                }
            });
        });
        albumArtistNameFullSetButton.setOnMouseClicked(mouseEvent -> {
            if (isNotTrackSelected()) return;
            String albumArtistNameStr = albumArtistName.getText();
            mp3FileManager.requestMP3Files(new FullSetCallback() {
                @Override
                protected MP3File modify(MP3File datum, int index) {
                    datum.albumArtistName = albumArtistNameStr;
                    return datum;
                }
            });
        });
        albumNameFullSetButton.setOnMouseClicked(mouseEvent -> {
            if (isNotTrackSelected()) return;
            String albumNameStr = albumName.getText();
            mp3FileManager.requestMP3Files(new FullSetCallback() {
                @Override
                protected MP3File modify(MP3File datum, int index) {
                    datum.albumName = albumNameStr;
                    return datum;
                }
            });
        });
        trackSetButton.setOnMouseClicked(mouseEvent -> {
            obtainFileInfo();
            mp3FileManager.updateFile(currentSelectedFile, new MP3FileManager.Callback<>());
        });
        updateFileInfo(null);
    }

    public void onSceneCreated(Scene scene) {
        darkMode.selectedProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                String css = "dark-theme.css";
                if (t1) {
                    scene.getStylesheets().add(getClass().getResource(css).toExternalForm());
                } else {
                    scene.getStylesheets().remove(getClass().getResource(css).toExternalForm());
                }
            }
        });
    }

    private void chooseAndUpdateFileList() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("MP3ファイル選択");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP3ファイル", "*.mp3")
        );
        List<File> files = chooser.showOpenMultipleDialog(null);
        if (files == null) return;
        mp3FileManager.registerFiles(files, new MP3FileManager.Callback<>() {
            @Override
            public void onSuccess(List<MP3File> data) {
                super.onSuccess(data);
                runOnMainThread(() -> updateFileList(data));
            }
        });
    }

    private void updateFileList(List<MP3File> files) {
        updateFileInfo(null);
        listData.clear();
        if (files == null || files.isEmpty()) return;
        listData.addAll(files);
    }

    private void updateFileInfo(MP3File file) {
        currentSelectedFile = file;
        boolean empty = (currentSelectedFile == null);
        fileName.setText((empty) ? "" : currentSelectedFile.fileName);
        trackName.setText((empty) ? "" : currentSelectedFile.trackName);
        artistName.setText((empty) ? "" : currentSelectedFile.artistName);
        trackNumber.setText((empty || currentSelectedFile.trackNumber == null) ? "" : String.valueOf(currentSelectedFile.trackNumber));
        albumArtistName.setText((empty) ? "" : currentSelectedFile.albumArtistName);
        albumName.setText((empty) ? "" : currentSelectedFile.albumName);
        setDisable(empty, fileName, trackName, artistName, trackNumber,albumArtistName, albumName);
        setDisable(empty, artistNameFullSetButton, trackNumberFullSetButton, albumArtistNameFullSetButton, albumNameFullSetButton, trackSetButton);
    }

    private void obtainFileInfo() {
        currentSelectedFile.fileName = fileName.getText();
        currentSelectedFile.trackName = trackName.getText();
        currentSelectedFile.artistName = artistName.getText();
        String trackNumberStr = trackNumber.getText();
        currentSelectedFile.trackNumber = (trackNumber == null) ? null : Integer.parseInt(trackNumberStr);
        currentSelectedFile.albumArtistName = albumArtistName.getText();
        currentSelectedFile.albumName = albumName.getText();
    }

    private boolean isNotTrackSelected() {
        return (currentSelectedFile == null);
    }

    private void runOnMainThread(Runnable r) {
        Platform.runLater(r);
    }

    private void setDisable(boolean disable, Node... nodes) {
        for (Node node : nodes) {
            node.setDisable(disable);
        }
    }

    private abstract class FullSetCallback extends MP3FileManager.Callback<List<MP3File>> {
        @Override
        public void onSuccess(List<MP3File> data) {
            super.onSuccess(data);
            List<MP3File> files = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                files.add(modify(data.get(i), i));
            }
            mp3FileManager.updateFiles(files, new MP3FileManager.Callback<>() {
                @Override
                public void onSuccess(List<MP3File> data) {
                    super.onSuccess(data);
                    runOnMainThread(() -> updateFileList(data));
                }
            });
        }

        abstract protected MP3File modify(MP3File datum, int index);
    }
}
