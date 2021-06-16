package com.u1tramarinet.mp3metaeditor.java.usecase;

import java.util.List;

public class CollectiveSetUseCase extends UseCase {

    public CollectiveSetUseCase() {
        super();
    }

    public void updateTags(List<MP3FileDto> mp3FileList) {
        manager.updateMP3Tags(Converter.convertMp3FileDtoToTagMap(mp3FileList));
    }

}
