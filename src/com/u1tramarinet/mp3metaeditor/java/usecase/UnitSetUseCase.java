package com.u1tramarinet.mp3metaeditor.java.usecase;

public class UnitSetUseCase extends UseCase {

    public UnitSetUseCase() {
        super();
    }

    public void updateTag(MP3FileDto dto) {
        manager.updateMP3Tag(dto.id, Converter.convertMp3FileDtoToTag(dto));
    }
}
