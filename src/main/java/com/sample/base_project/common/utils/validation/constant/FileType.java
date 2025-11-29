package com.sample.base_project.common.utils.validation.constant;

public enum FileType {
    IMAGE,
    SUBTITLE,
    VIDEO,
    DOCUMENT,
    OTHER_DOCUMENT,
    ALL_FILE,
    ;

    public static class Prefix {
        public static final String IMAGES = "images";
        public static final String DOCUMENTS = "documents";
        public static final String VIDEOS = "videos";
        public static final String SUBTITLES = "subtitles";
    }
}
