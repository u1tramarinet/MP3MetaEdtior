package com.u1tramarinet.mp3metaeditor.java.ui;

import com.u1tramarinet.mp3metaeditor.java.util.UiThreadExecutor;
import com.u1tramarinet.mp3metaeditor.java.usecase.ListenFileUpdateUseCase;
import com.u1tramarinet.mp3metaeditor.java.usecase.ListenFilesUpdateUseCase;
import com.u1tramarinet.mp3metaeditor.java.usecase.MP3FileDto;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class CollectiveSetController implements Initializable {
    @FXML
    private CheckBox trackExcerptCheck;
    @FXML
    private CheckBox artistCollectiveSetCheck;
    @FXML
    private CheckBox trackNumberCollectiveSetCheck;
    @FXML
    private CheckBox albumArtistCollectiveSetCheck;
    @FXML
    private CheckBox albumCollectiveSetCheck;
    @FXML
    private TextField trackExcerptBefore;
    @FXML
    private TextField trackExcerptAfter;
    @FXML
    private TextField artistCollectiveSet;
    @FXML
    private TextField albumArtistCollectiveSet;
    @FXML
    private TextField albumCollectiveSet;
    @FXML
    private Label trackExcerptExample;
    @FXML
    private HBox artistCandidates;
    @FXML
    private HBox albumArtistCandidates;
    @FXML
    private HBox albumCandidates;
    @FXML
    private RadioButton trackNumberAscRadio;
    @FXML
    private RadioButton trackNumberDesRadio;
    @FXML
    private Button collectiveSetButton;
    @FXML
    private Label trackExcerptBeforeLabel;
    @FXML
    private Label trackExcerptAfterLabel;
    @FXML
    private HBox trackExcerptBox;
    @FXML
    private HBox artistCandidatesBox;
    @FXML
    private HBox albumArtistCandidatesBox;
    @FXML
    private HBox albumCandidatesBox;
    private final MP3FileDtoProperty fileProperty = new MP3FileDtoProperty();
    private final Property<List<MP3FileDto>> filesProperty = new Property<>();
    private final Map<String, Integer> artistCandidateList = new HashMap<>();
    private final Map<String, Integer> albumArtistCandidateList = new HashMap<>();
    private final Map<String, Integer> albumCandidateList = new HashMap<>();
    private final ListenFileUpdateUseCase listenFileUpdateUseCase = new ListenFileUpdateUseCase(new UiThreadExecutor());
    private final ListenFilesUpdateUseCase listenFilesUpdateUseCase = new ListenFilesUpdateUseCase(new UiThreadExecutor());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindDisableProperty(isDisableOrNotSelected(trackExcerptCheck),
                trackExcerptBefore, trackExcerptAfter, trackExcerptBeforeLabel, trackExcerptAfterLabel, trackExcerptBox);
        bindDisableProperty(isDisableOrNotSelected(artistCollectiveSetCheck), artistCollectiveSet, artistCandidatesBox);
        bindDisableProperty(isDisableOrNotSelected(trackNumberCollectiveSetCheck), trackNumberAscRadio, trackNumberDesRadio);
        bindDisableProperty(isDisableOrNotSelected(albumArtistCollectiveSetCheck), albumArtistCollectiveSet, albumArtistCandidatesBox);
        bindDisableProperty(isDisableOrNotSelected(albumCollectiveSetCheck), albumCollectiveSet, albumCandidatesBox);
        bindDisableProperty(filesProperty.valueProperty().isNull(),
                trackExcerptCheck, artistCollectiveSetCheck, trackNumberCollectiveSetCheck,
                albumArtistCollectiveSetCheck, albumCollectiveSetCheck);
        bindDisableProperty(filesProperty.valueProperty().isNull()
                        .or(((trackExcerptCheck.selectedProperty()
                                .or(artistCollectiveSetCheck.selectedProperty())
                                .or(trackNumberCollectiveSetCheck.selectedProperty())
                                .or(albumArtistCollectiveSetCheck.selectedProperty())
                                .or(albumCollectiveSetCheck.selectedProperty())).not())),
                collectiveSetButton);
        fileProperty.valueProperty().addListener(((observableValue, oldFile, newFile) -> excerptTrackNumber()));
        trackExcerptBefore.textProperty().addListener((observableValue, oldValue, newValue) -> excerptTrackNumber());
        trackExcerptAfter.textProperty().addListener((observableValue, oldValue, newValue) -> excerptTrackNumber());
        filesProperty.valueProperty().addListener(((observableValue, oldFiles, newFiles) -> updateCandidates(newFiles)));

        ToggleGroup group = new ToggleGroup();
        trackNumberAscRadio.setToggleGroup(group);
        trackNumberDesRadio.setToggleGroup(group);

        listenFileUpdateUseCase.startToListen(new ListenFileUpdateUseCase.Callback() {
            @Override
            public void onSuccess(MP3FileDto file) {
                updateMP3File(file);
            }

            @Override
            public void onFailure() {

            }
        });
        listenFilesUpdateUseCase.startToListen(new ListenFilesUpdateUseCase.Callback() {
            @Override
            public void onSuccess(List<MP3FileDto> file) {
                updateMP3Files(file);
            }

            @Override
            public void onFailure() {

            }
        });
    }

    public void updateMP3Files(List<MP3FileDto> fileDtos) {
        filesProperty.set(fileDtos);
        analyzeCandidates(fileDtos);
    }

    public void updateMP3File(MP3FileDto fileDto) {
        fileProperty.set(fileDto);
    }

    public void clearMP3Files() {
        filesProperty.set(null);
        clearCandidates();
    }

    public void clearMP3File() {
        fileProperty.set(null);
    }

    private void excerptTrackNumber() {
        MP3FileDto file = fileProperty.get();
        if (null == file) {
            trackExcerptExample.setText("");
            return;
        }
        String originalStr = file.fileName;
        String before = trackExcerptBefore.getText();
        String after = trackExcerptAfter.getText();
        String result = excerpt(originalStr, before, after);
        trackExcerptExample.setText(result);
    }

    private String excerpt(String original, String before, String after) {
        if (isNullOrEmpty(original)) return "";
        String output = original;
        int beforeIndex = (isNullOrEmpty(output) || isNullOrEmpty(before)) ? -1 : original.indexOf(before);
        if (beforeIndex != -1) {
            output = output.substring(beforeIndex + 1);
        }
        int afterIndex = (isNullOrEmpty(output) || isNullOrEmpty(after)) ? -1 : output.lastIndexOf(after);
        if (afterIndex != -1) {
            output = output.substring(0, afterIndex);
        }
        return output;
    }

    private void updateCandidates(List<MP3FileDto> fileDtos) {
        clearCandidates();
        if (null == fileDtos) return;
        analyzeCandidates(fileDtos);
    }

    private void analyzeCandidates(List<MP3FileDto> fileDtos) {
        clearCandidates();
        for (MP3FileDto dto : fileDtos) {
            addOrCountUp(artistCandidateList, dto.artistName);
            addOrCountUp(albumArtistCandidateList, dto.albumArtistName);
            addOrCountUp(albumCandidateList, dto.albumName);
        }
        setCandidates(artistCandidates, artistCandidateList, artistCollectiveSet);
        setCandidates(albumArtistCandidates, albumArtistCandidateList, albumArtistCollectiveSet);
        setCandidates(albumCandidates, albumCandidateList, albumCollectiveSet);
    }

    private void clearCandidates() {
        artistCandidateList.clear();
        artistCandidates.getChildren().clear();
        albumArtistCandidateList.clear();
        albumArtistCandidates.getChildren().clear();
        albumCandidateList.clear();
        albumCandidates.getChildren().clear();
    }

    private void addOrCountUp(Map<String, Integer> map, String key) {
        if (isNullOrEmpty(key)) return;
        map.put(key, map.getOrDefault(key, 0) + 1);
    }

    private boolean isNullOrEmpty(String input) {
        return (null == input) || input.equals("");
    }

    private void setCandidates(HBox list, Map<String, Integer> map, TextField output) {
        if (map.size() == 0) {
            Label text = new Label();
            text.setText("候補なし");
            list.getChildren().add(text);
        } else {
            map.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .forEach((entry) -> {
                        String key = entry.getKey();
                        Label text = new Label();
                        text.setText(key);
                        text.setFont(Font.font(10));
                        text.setUnderline(true);
                        text.setOnMouseClicked(mouseEvent -> output.setText(key));
                        list.getChildren().add(text);
                    });
        }
    }

    private ObservableValue<Boolean> isDisableOrNotSelected(CheckBox checkBox) {
        return checkBox.disableProperty()
                .or(checkBox.selectedProperty().not());
    }

    private void bindDisableProperty(ObservableValue<Boolean> value, Node... nodes) {
        for (Node node : nodes) {
            node.disableProperty().bind(value);
        }
    }
}
