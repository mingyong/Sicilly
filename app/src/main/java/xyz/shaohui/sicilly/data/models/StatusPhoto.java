package xyz.shaohui.sicilly.data.models;

import android.os.Parcel;
import android.os.Parcelable;

public class StatusPhoto implements Parcelable {
    private String imageurl;
    private String largeurl;
    private String thumburl;

    public String getImageurl() {
        return this.imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getLargeurl() {
        return this.largeurl;
    }

    public void setLargeurl(String largeurl) {
        this.largeurl = largeurl;
    }

    public String getThumburl() {
        return this.thumburl;
    }

    public void setThumburl(String thumburl) {
        this.thumburl = thumburl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageurl);
        dest.writeString(this.largeurl);
        dest.writeString(this.thumburl);
    }

    public StatusPhoto() {
    }

    protected StatusPhoto(Parcel in) {
        this.imageurl = in.readString();
        this.largeurl = in.readString();
        this.thumburl = in.readString();
    }

    public static final Parcelable.Creator<StatusPhoto> CREATOR = new Parcelable.Creator<StatusPhoto>() {
        @Override
        public StatusPhoto createFromParcel(Parcel source) {
            return new StatusPhoto(source);
        }

        @Override
        public StatusPhoto[] newArray(int size) {
            return new StatusPhoto[size];
        }
    };
}
