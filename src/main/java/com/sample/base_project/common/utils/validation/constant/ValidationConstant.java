package com.sample.base_project.common.utils.validation.constant;

public class ValidationConstant {
    public static final String DATE_AND_FILE_PATH = "([A-Za-z0-9]{1,20}\\/)?\\d{4}\\/(?:[1-9]|1[0-9])\\/(?:[1-9]|[1-3][0-9])\\/\\d{16,20}(?:\\/.*)?";
    public final static String IMAGE_EXTENSIONS = "png|jpg|jpeg|gif";
    public final static String VIDEO_EXTENSIONS = "mp4|mov";
    public final static String SUBTITLE_EXTENSIONS = "srt";
    public final static String DOCUMENT_EXTENSIONS = "pdf|doc|docx";
    public final static String OTHER_DOCUMENT_EXTENSIONS = "xls|xlsx|ppt|pptx";
    public static final String FILE_EXTENSIONS = IMAGE_EXTENSIONS + "|" + VIDEO_EXTENSIONS + "|" + DOCUMENT_EXTENSIONS + "|" + OTHER_DOCUMENT_EXTENSIONS;

    public static final String SUBTITLE_REGEX_PATTERN = "(?i)^" + DATE_AND_FILE_PATH + "\\.(?:" + SUBTITLE_EXTENSIONS + ")$";
    public static final String SUBTITLE_REGEX_PATTERN_BLANK = "(?i)^(|" + DATE_AND_FILE_PATH + "\\.(?:" + SUBTITLE_EXTENSIONS + "))$";
    public static final String VIDEO_REGEX_PATTERN = "(?i)^" + DATE_AND_FILE_PATH + "\\.(?:" + VIDEO_EXTENSIONS + ")$";
    public static final String VIDEO_REGEX_PATTERN_BLANK = "(?i)^(|" + DATE_AND_FILE_PATH + "\\.(?:" + VIDEO_EXTENSIONS + "))$";
    public static final String IMAGE_REGEX_PATTERN = "(?i)^" + DATE_AND_FILE_PATH + "\\.(?:" + IMAGE_EXTENSIONS + ")$";
    public static final String IMAGE_REGEX_PATTERN_BLANK = "(?i)^(|" + DATE_AND_FILE_PATH + "\\.(?:" + IMAGE_EXTENSIONS + "))$";
    public static final String VIDEO_IMAGE_REGEX_PATTERN = "(?i)^" + DATE_AND_FILE_PATH + "\\.(?:" + IMAGE_EXTENSIONS + "|" + VIDEO_EXTENSIONS + ")$";
    public static final String VIDEO_IMAGE_REGEX_PATTERN_BLANK = "(?i)^(|" + DATE_AND_FILE_PATH + "\\.(?:" + IMAGE_EXTENSIONS + "|" + VIDEO_EXTENSIONS + "))$";

    public static final String FILE_REGEX_PATTERN = "(?i)^" + DATE_AND_FILE_PATH + "\\.(?:" + FILE_EXTENSIONS + ")$";
    public static final String FILE_REGEX_PATTERN_BLANK = "(?i)^(|" + DATE_AND_FILE_PATH + "\\.(?:" + FILE_EXTENSIONS + "))$";

    public static final String DOCUMENT_REGEX_PATTERN = "(?i)^" + DATE_AND_FILE_PATH + "\\.(?:" + DOCUMENT_EXTENSIONS + ")$";
    public static final String DOCUMENT_REGEX_PATTERN_BLANK = "(?i)^(|" + DATE_AND_FILE_PATH + "\\.(?:" + DOCUMENT_EXTENSIONS + "))$";

    public static final String URL_FILE_PATTERN = "(?i)^(http|https)://.*\\.(?:" + FILE_EXTENSIONS + ")(\\?.*)?$";
    public static final String URL_FILE_PATTERN_OR_BLANK = "(?i)^(http|https)://.*\\.(?:" + FILE_EXTENSIONS + ")(\\?.*)?$|^$";
    public static final String URL_IMAGE_PATTERN = "(?i)^(http|https)://.*\\.(?:" + IMAGE_EXTENSIONS + ")(\\?.*)?$";
    public static final String URL_IMAGE_PATTERN_OR_BLANK = "(?i)^(http|https)://.*\\.(?:" + IMAGE_EXTENSIONS + ")(\\?.*)?$|^$";
    public static final String URL_DOCUMENT_PATTERN = "(?i)^(http|https)://.*\\.(?:" + DOCUMENT_EXTENSIONS + ")(\\?.*)?$";
    public static final String URL_DOCUMENT_PATTERN_OR_BLANK = "(?i)^(http|https)://.*\\.(?:" + DOCUMENT_EXTENSIONS + ")(\\?.*)?$|^$";
}
