package com.u1tramarinet.mp3metaeditor.java.ui;

import com.u1tramarinet.mp3metaeditor.java.usecase.ListenFilesUpdateUseCase;
import com.u1tramarinet.mp3metaeditor.java.usecase.ListenUseCase;
import com.u1tramarinet.mp3metaeditor.java.usecase.MP3FileDto;
import com.u1tramarinet.mp3metaeditor.java.usecase.RegisterFilesUseCase;
import com.u1tramarinet.mp3metaeditor.java.util.UiThreadExecutor;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController extends ControllerBase {
    @FXML
    private Button fileButton;
    @FXML
    private Button resetButton;
    private final ObjectProperty<MP3FileDto> selectedFileProperty = new SimpleObjectProperty<>();
    private final ObservableList<MP3FileDto> fileList = FXCollections.observableArrayList();
    private final ListProperty<MP3FileDto> fileListProperty = new SimpleListProperty<>(fileList);
    private final RegisterFilesUseCase registerFilesUseCase = new RegisterFilesUseCase();
    private final ListenFilesUpdateUseCase listenFilesUpdateUseCase = new ListenFilesUpdateUseCase(new UiThreadExecutor());
    @FXML
    private UnitSetController unitSettingController;
    @FXML
    private CollectiveSetController collectiveSettingController;
    @FXML
    private FileListController fileListController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileButton.setOnMouseClicked(mouseEvent -> chooseFileListFromExplorer());
        resetButton.setOnMouseClicked(mouseEvent -> clearFileList());
        resetButton.disableProperty().bind(fileListProperty.emptyProperty());
        listenFilesUpdateUseCase.startToListen(new ListenUseCase.Callback<>() {
            @Override
            public void onSuccess(List<MP3FileDto> files) {
                updateFileList(files);
            }

            @Override
            public void onFailure() {
                updateFileList(null);
            }
        });
        unitSettingController.bindFileProperties(selectedFileProperty, fileListProperty);
        collectiveSettingController.bindFileProperties(selectedFileProperty, fileListProperty);
        fileListController.bindFileProperties(selectedFileProperty, fileListProperty);
    }

    private void chooseFileListFromExplorer() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("MP3ファイル選択");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP3ファイル", "*.mp3")
        );
        List<File> files = chooser.showOpenMultipleDialog(null);
        if (files == null) return;
        registerFilesUseCase.register(files);
    }

    private void updateFileList(List<MP3FileDto> files) {
        selectedFileProperty.set(null);
        fileList.clear();
        if (files == null || files.isEmpty()) {
            return;
        }
        fileList.addAll(files);
    }

    private void clearFileList() {
        registerFilesUseCase.unregister();
    }
}
