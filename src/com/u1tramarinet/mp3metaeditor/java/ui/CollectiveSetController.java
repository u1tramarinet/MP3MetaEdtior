package com.u1tramarinet.mp3metaeditor.java.ui;

import com.u1tramarinet.mp3metaeditor.java.usecase.CollectiveSetUseCase;
import com.u1tramarinet.mp3metaeditor.java.usecase.MP3FileDto;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;

public class CollectiveSetController extends ChildControllerBase {
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
    private final CollectiveSetUseCase collectiveSetUseCase = new CollectiveSetUseCase();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindDisableProperty(isDisableOrNotSelected(trackExcerptCheck),
                trackExcerptBefore, trackExcerptAfter, trackExcerptBeforeLabel, trackExcerptAfterLabel, trackExcerptBox);
        bindDisableProperty(isDisableOrNotSelected(artistCollectiveSetCheck), artistCollectiveSet, artistCandidatesBox);
        bindDisableProperty(isDisableOrNotSelected(trackNumberCollectiveSetCheck), trackNumberAscRadio, trackNumberDesRadio);
        bindDisableProperty(isDisableOrNotSelected(albumArtistCollectiveSetCheck), albumArtistCollectiveSet, albumArtistCandidatesBox);
        bindDisableProperty(isDisableOrNotSelected(albumCollectiveSetCheck), albumCollectiveSet, albumCandidatesBox);
        bindDisableProperty(fileListProperty.isNull().or(fileListProperty.emptyProperty()),
                trackExcerptCheck, artistCollectiveSetCheck, trackNumberCollectiveSetCheck,
                albumArtistCollectiveSetCheck, albumCollectiveSetCheck);
        bindDisableProperty(fileListProperty.isNull().or(fileListProperty.emptyProperty())
                        .or(((trackExcerptCheck.selectedProperty()
                                .or(artistCollectiveSetCheck.selectedProperty())
                                .or(trackNumberCollectiveSetCheck.selectedProperty())
                                .or(albumArtistCollectiveSetCheck.selectedProperty())
                                .or(albumCollectiveSetCheck.selectedProperty())).not())),
                collectiveSetButton);
        trackExcerptBefore.textProperty().addListener((observableValue, oldValue, newValue) -> excerptTrackName());
        trackExcerptAfter.textProperty().addListener((observableValue, oldValue, newValue) -> excerptTrackName());

        ToggleGroup group = new ToggleGroup();
        trackNumberAscRadio.setToggleGroup(group);
        trackNumberDesRadio.setToggleGroup(group);

        selectedFileProperty.addListener((observableValue, oldValue, newValue) -> updateMP3File(newValue));
        fileListProperty.addListener((observableValue, oldValue, newValue) -> updateMP3Files(newValue));

        collectiveSetButton.setOnMouseClicked(mouseEvent -> submitTrackInfo());
    }

    private void updateMP3Files(List<MP3FileDto> files) {
        analyzeAndUpdateCandidates(files);
    }

    private void updateMP3File(MP3FileDto file) {
        String result = excerptTrackName(file);
        trackExcerptExample.setText(result);
    }

    private void excerptTrackName() {
        MP3FileDto file = selectedFileProperty.get();
        String result = excerptTrackName(file);
        trackExcerptExample.setText(result);
    }

    private String excerptTrackName(MP3FileDto file) {
        if (null == file) {
            return "";
        }
        String originalStr = file.fileName;
        String before = trackExcerptBefore.getText();
        String after = trackExcerptAfter.getText();
        return collectiveSetUseCase.excerptAffix(originalStr, before, after);
    }

    private void analyzeAndUpdateCandidates(List<MP3FileDto> files) {
        analyzeAndUpdateCandidates(files, fileDto -> fileDto.artistName, artistCandidates, artistCollectiveSet);
        analyzeAndUpdateCandidates(files, fileDto -> fileDto.albumArtistName, albumArtistCandidates, albumArtistCollectiveSet);
        analyzeAndUpdateCandidates(files, fileDto -> fileDto.albumName, albumCandidates, albumCollectiveSet);
    }

    private void analyzeAndUpdateCandidates(List<MP3FileDto> files, Function<MP3FileDto, String> command, HBox container, TextField destination) {
        Map<String, Integer> map = new HashMap<>();
        container.getChildren().clear();

        if (null == files) return;

        for (MP3FileDto file : files) {
            String key = command.apply(file);
            if (null == key) continue;
            map.put(key, map.getOrDefault(key, 0) + 1);
        }

        if (map.size() == 0) {
            Label text = new Label();
            text.setText("候補なし");
            container.getChildren().add(text);
        } else {
            map.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .forEach((entry) -> {
                        String key = entry.getKey();
                        Label text = new Label();
                        text.setText(key);
                        text.setFont(Font.font(10));
                        text.setUnderline(true);
                        text.setOnMouseClicked(mouseEvent -> destination.setText(key));
                        container.getChildren().add(text);
                    });
        }
    }

    private void submitTrackInfo() {
        boolean trackNameEnabled = trackExcerptCheck.selectedProperty().get();
        String trackNameBefore = trackExcerptBefore.getText();
        String trackNameAfter = trackExcerptAfter.getText();
        CollectiveSetUseCase.Parametor.TrackName trackNameParam = new CollectiveSetUseCase.Parametor.TrackName(trackNameEnabled, trackNameBefore, trackNameAfter);
        boolean artistNameEnabled = artistCollectiveSetCheck.selectedProperty().get();
        String artistName = artistCollectiveSet.getText();
        CollectiveSetUseCase.Parametor.Name artistNameParam = new CollectiveSetUseCase.Parametor.Name(artistNameEnabled, artistName);
        boolean trackNumberEnabled = trackNumberCollectiveSetCheck.selectedProperty().get();
        CollectiveSetUseCase.Parametor.TrackNumber.Order order = (trackNumberAscRadio.selectedProperty().get()) ? CollectiveSetUseCase.Parametor.TrackNumber.Order.ASC : CollectiveSetUseCase.Parametor.TrackNumber.Order.DESC;
        CollectiveSetUseCase.Parametor.TrackNumber trackNumberParam = new CollectiveSetUseCase.Parametor.TrackNumber(trackNumberEnabled, order);
        boolean albumArtistNameEnabled = albumArtistCollectiveSetCheck.selectedProperty().get();
        String albumArtistName = albumArtistCollectiveSet.getText();
        CollectiveSetUseCase.Parametor.Name albumArtistNameParam = new CollectiveSetUseCase.Parametor.Name(albumArtistNameEnabled, albumArtistName);
        boolean albumNameEnabled = albumCollectiveSetCheck.selectedProperty().get();
        String albumName = albumCollectiveSet.getText();
        CollectiveSetUseCase.Parametor.Name albumNameParam = new CollectiveSetUseCase.Parametor.Name(albumNameEnabled, albumName);

        List<MP3FileDto> files = fileListProperty.get();
        CollectiveSetUseCase.Parametor parameter = new CollectiveSetUseCase.Parametor(trackNameParam, artistNameParam, trackNumberParam, albumArtistNameParam, albumNameParam);
        collectiveSetUseCase.updateTags(files, parameter);
    }
}
