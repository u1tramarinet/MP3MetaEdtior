package com.u1tramarinet.mp3metaeditor.java.usecase;

import com.u1tramarinet.mp3metaeditor.java.domain.MP3File;
import com.u1tramarinet.mp3metaeditor.java.domain.MP3Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Converter {

    private Converter() {

    }

    static MP3File convertMP3FileDtoToFile(MP3FileDto dto) {
        MP3Tag tag = convertMp3FileDtoToTag(dto);
        return new MP3File(dto.id, dto.fileName, tag);
    }

    static List<MP3File> convertMP3FileDtoToFileAll(List<MP3FileDto> dtoList) {
        return convertAll(dtoList, Converter::convertMP3FileDtoToFile);
    }

    static MP3Tag convertMp3FileDtoToTag(MP3FileDto dto) {
        MP3Tag tag = new MP3Tag();
        tag.albumArtistName = dto.albumArtistName;
        tag.trackNumber = dto.trackNumber;
        tag.artistName = dto.artistName;
        tag.trackName = dto.trackName;
        tag.albumName = dto.albumName;
        return tag;
    }

    static List<MP3Tag> convertMp3FileDtoToTagAll(List<MP3FileDto> dtoList) {
        return convertAll(dtoList, Converter::convertMp3FileDtoToTag);
    }

    static Map<Long, MP3Tag> convertMp3FileDtoToTagMap(List<MP3FileDto> dtoList) {
        Map<Long, MP3Tag> map = new HashMap<>();
        for (MP3FileDto dto : dtoList) {
            map.put(dto.id, convertMp3FileDtoToTag(dto));
        }
        return map;
    }

    static MP3FileDto convertMP3FileToDto(MP3File file) {
        MP3FileDto dto = new MP3FileDto();
        dto.id = file.id;
        dto.fileName = file.file.getName();
        dto.albumArtistName = file.getTag().albumArtistName;
        dto.albumName = file.getTag().albumName;
        dto.trackName = file.getTag().trackName;
        dto.trackNumber = file.getTag().trackNumber;
        dto.artistName = file.getTag().artistName;
        return dto;
    }

    static List<MP3FileDto> convertMP3FileToDtoAll(List<MP3File> fileList) {
        return convertAll(fileList, Converter::convertMP3FileToDto);
    }

    private static <Input, Output> List<Output> convertAll(List<Input> inputs, Function<Input, Output> command) {
        List<Output> outputs = new ArrayList<>();
        for (Input input : inputs) {
            outputs.add(command.apply(input));
        }
        return outputs;
    }
}
