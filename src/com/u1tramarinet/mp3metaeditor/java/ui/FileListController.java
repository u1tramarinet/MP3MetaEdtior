package com.u1tramarinet.mp3metaeditor.java.ui;

import com.u1tramarinet.mp3metaeditor.java.usecase.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class FileListController extends ChildControllerBase {
    @FXML
    private ListView<MP3FileDto> fileListView;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<MP3FileDto> call(ListView<MP3FileDto> fileListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(MP3FileDto file, boolean empty) {
                        super.updateItem(file, empty);
                        String filename = ((file == null) || empty) ? "" : file.fileName;
                        runOnUiThread(() -> setText(filename));
                    }
                };
            }
        });
        fileListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MP3FileDto>() {
            @Override
            public void changed(ObservableValue<? extends MP3FileDto> observableValue, MP3FileDto oldItem, MP3FileDto newItem) {
                if (null == newItem) {
                    unselectItem();
                    return;
                }
                boolean changed = (null == oldItem) || (oldItem.id != newItem.id);
                if (changed) {
                    selectItem(newItem);
                } else {
                    unselectItem();
                }
            }
        });
        fileListView.setItems(fileListProperty);
        fileListProperty.addListener((ListChangeListener<MP3FileDto>) change -> unselectItem());
    }

    private void selectItem(MP3FileDto item) {
        selectedFileProperty.set(item);
    }

    private void unselectItem() {
        fileListView.getSelectionModel().clearSelection();
        selectedFileProperty.set(null);
    }
}
