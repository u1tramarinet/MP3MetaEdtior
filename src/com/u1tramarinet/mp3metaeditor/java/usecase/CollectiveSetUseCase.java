package com.u1tramarinet.mp3metaeditor.java.usecase;

import java.util.List;

public class CollectiveSetUseCase extends UseCase {

    public CollectiveSetUseCase() {
        super();
    }

    public void updateTags(List<MP3FileDto> mp3FileList) {
        manager.updateMP3Tags(Converter.convertMp3FileDtoToTagMap(mp3FileList));
    }

    public void updateTags(List<MP3FileDto> mp3FileList, Parametor parametor) {
        manager.updateMP3Tags(Converter.convertMp3FileDtoToTagMap(modifyInfo(mp3FileList, parametor)));
    }

    private List<MP3FileDto> modifyInfo(List<MP3FileDto> mp3FileList, Parametor parametor) {
        int number = 1;
        int diff = 1;
        if (parametor.trackNumber.order == Parametor.TrackNumber.Order.DESC) {
            number = mp3FileList.size();
            diff = -1;
        }
        for (MP3FileDto file : mp3FileList) {
            if (parametor.trackName.enabled) {
                file.trackName = excerptAffix(file.fileName, parametor.trackName.before, parametor.trackName.after);
            }
            if (parametor.artistName.enabled) {
                file.artistName = parametor.artistName.value;
            }
            if (parametor.trackNumber.enabled) {
                file.trackNumber = String.valueOf(number);
                number += diff;
            }
            if (parametor.albumArtistName.enabled) {
                file.albumArtistName = parametor.albumArtistName.value;
            }
            if (parametor.albumName.enabled) {
                file.albumName = parametor.albumName.value;
            }
        }
        return mp3FileList;
    }

    public String excerptAffix(String fileName, String before, String after) {
        if (isNullOrEmpty(fileName)) return "";
        String output = fileName;
        int beforeIndex = isNullOrEmpty(before) ? -1 : fileName.indexOf(before);
        if (beforeIndex != -1) {
            output = output.substring(beforeIndex + 1);
        }
        int afterIndex = (isNullOrEmpty(output) || isNullOrEmpty(after)) ? -1 : output.lastIndexOf(after);
        if (afterIndex != -1) {
            output = output.substring(0, afterIndex);
        }
        return output;
    }

    private boolean isNullOrEmpty(String value) {
        return (null == value) || value.equals("");
    }

    public static class Parametor {
        final TrackName trackName;
        final Name artistName;
        final TrackNumber trackNumber;
        final Name albumArtistName;
        final Name albumName;

        public Parametor(TrackName trackName, Name artistName, TrackNumber trackNumber, Name albumArtistName, Name albumName) {
            this.trackName = trackName;
            this.artistName = artistName;
            this.trackNumber = trackNumber;
            this.albumArtistName = albumArtistName;
            this.albumName = albumName;
        }

        public static class TrackName extends Base {
            private final String before;
            private final String after;

            public TrackName(boolean enabled, String before, String after) {
                super(enabled);
                this.before = before;
                this.after = after;
            }
        }

        public static class Name extends Base {
            private final String value;

            public Name(boolean enabled, String value) {
                super(enabled);
                this.value = value;
            }
        }

        public static class TrackNumber extends Base {
            private final Order order;

            public enum Order {
                DESC,
                ASC
            }

            public TrackNumber(boolean enabled, Order order) {
                super(enabled);
                this.order = order;
            }
        }

        public static class Base {
            final boolean enabled;

            public Base() {
                this(false);
            }

            public Base(boolean enabled) {
                this.enabled = enabled;
            }
        }
    }
}
