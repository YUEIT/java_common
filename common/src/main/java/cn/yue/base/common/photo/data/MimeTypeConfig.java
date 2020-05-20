package cn.yue.base.common.photo.data;

public class MimeTypeConfig {

    public static boolean onlyShowImages(MediaType mediaType) {
        return mediaType == MediaType.PHOTO;
    }

    public static boolean onlyShowVideos(MediaType mediaType) {
        return mediaType == MediaType.VIDEO;
    }
}
