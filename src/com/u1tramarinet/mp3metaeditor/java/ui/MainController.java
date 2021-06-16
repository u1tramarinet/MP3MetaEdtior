package com.u1tramarinet.mp3metaeditor.java.ui;

import com.u1tramarinet.mp3metaeditor.java.usecase.*;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Button fileButton;
    @FXML
    private Button resetButton;
    @FXML
    private ListView<MP3FileDto> fileListView;
    private final ObservableList<MP3FileDto> listData = FXCollections.observableArrayList();
    private final ListProperty<MP3FileDto> filesProperty = new SimpleListProperty<>(listData);
    private final RegisterFilesUseCase registerFilesUseCase = new RegisterFilesUseCase();
    private final ListenFilesUpdateUseCase listenFilesUpdateUseCase = new ListenFilesUpdateUseCase();
    private final SelectFileUseCase selectFileUseCase = new SelectFileUseCase();
    private int selectedIndex = -1;
    @FXML
    private UnitSetController unitSetController;
    @FXML
    private CollectiveSetController collectiveSetController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileButton.setOnMouseClicked(mouseEvent -> chooseFileListFromExplorer());
        resetButton.setOnMouseClicked(mouseEvent -> clearFileList());
        resetButton.disableProperty().bind(filesProperty.emptyProperty());
        fileListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<MP3FileDto> call(ListView<MP3FileDto> fileListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(MP3FileDto file, boolean empty) {
                        super.updateItem(file, empty);
                        String filename = ((file == null) || empty) ? "" : file.fileName;
                        setText(filename);
                    }
                };
            }
        });
        fileListView.setOnMouseClicked(mouseEvent -> {
            int index = fileListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex != index) {
                selectedIndex = index;
                selectFile(index);
            } else {
                selectedIndex = -1;
                unselectFile();
            }
        });
        listenFilesUpdateUseCase.startToListen(new ListenUseCase.Callback<>() {
            @Override
            public void onSuccess(List<MP3FileDto> files) {
                updateFileList(files);
            }

            @Override
            public void onFailure() {

            }
        });
        fileListView.setItems(listData);
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
        unselectFile();
        listData.clear();
        if (files == null || files.isEmpty()) {
            return;
        }
        listData.addAll(files);
    }

    private void clearFileList() {
        unselectFile();
        listData.clear();
        registerFilesUseCase.unregister();
    }

    private void selectFile(int index) {
        long id = fileListView.getItems().get(index).id;
        selectFileUseCase.select(id);
    }

    private void unselectFile() {
        fileListView.getSelectionModel().clearSelection();
        selectFileUseCase.unselect();
    }
}
