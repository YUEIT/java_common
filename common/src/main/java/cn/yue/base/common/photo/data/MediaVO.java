package cn.yue.base.common.photo.data;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Description :
 * Created by yue on 2019/3/11
 */

public class MediaVO implements Parcelable {

    private long modifyTime;

    private String url;
    private String id;
    private String mimeType;
    private Uri uri;
    private long size;
    private long duration;

    public MediaVO() {
    }

    public MediaVO(long modifyTime, String url) {
        this.modifyTime = modifyTime;
        this.url = url;
    }

    protected MediaVO(Parcel in) {
        modifyTime = in.readLong();
        url = in.readString();
        id = in.readString();
        mimeType = in.readString();
        uri = in.readParcelable(Uri.class.getClassLoader());
        size = in.readLong();
        duration = in.readLong();
    }

    public static final Creator<MediaVO> CREATOR = new Creator<MediaVO>() {
        @Override
        public MediaVO createFromParcel(Parcel in) {
            return new MediaVO(in);
        }

        @Override
        public MediaVO[] newArray(int size) {
            return new MediaVO[size];
        }
    };

    public long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(modifyTime);
        dest.writeString(url);
        dest.writeString(id);
        dest.writeString(mimeType);
        dest.writeParcelable(uri, flags);
        dest.writeLong(size);
        dest.writeLong(duration);
    }

    public static boolean equals(MediaVO mediaVO, MediaVO mediaVO1) {
        if (mediaVO == null || mediaVO.getUri() == null || mediaVO1 == null || mediaVO1.getUri() == null) {
            return false;
        }
        return mediaVO.getUri().equals(mediaVO1.getUri());
    }
}
