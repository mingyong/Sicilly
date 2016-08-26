package xyz.shaohui.sicilly.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Status implements Parcelable {
    private String repost_user_id;
    private String in_reply_to_status_id;
    private Status repost_status;
    private Date created_at;
    private boolean truncated;
    private String source;
    private String in_reply_to_screen_name;
    private String repost_status_id;
    private int rawid;
    private String in_reply_to_user_id;
    private String repost_screen_name;
    private String id;
    private String text;
    private boolean favorited;
    private StatusPhoto photo;
    private User user;

    public StatusPhoto getPhoto() {
        return photo;
    }

    public void setPhoto(StatusPhoto photo) {
        this.photo = photo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRepost_user_id() {
        return this.repost_user_id;
    }

    public void setRepost_user_id(String repost_user_id) {
        this.repost_user_id = repost_user_id;
    }

    public String getIn_reply_to_status_id() {
        return this.in_reply_to_status_id;
    }

    public void setIn_reply_to_status_id(String in_reply_to_status_id) {
        this.in_reply_to_status_id = in_reply_to_status_id;
    }

    public Status getRepost_status() {
        return this.repost_status;
    }

    public void setRepost_status(Status repost_status) {
        this.repost_status = repost_status;
    }

    public Date getCreated_at() {
        return this.created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public boolean getTruncated() {
        return this.truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getIn_reply_to_screen_name() {
        return this.in_reply_to_screen_name;
    }

    public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
        this.in_reply_to_screen_name = in_reply_to_screen_name;
    }

    public String getRepost_status_id() {
        return this.repost_status_id;
    }

    public void setRepost_status_id(String repost_status_id) {
        this.repost_status_id = repost_status_id;
    }

    public int getRawid() {
        return this.rawid;
    }

    public void setRawid(int rawid) {
        this.rawid = rawid;
    }

    public String getIn_reply_to_user_id() {
        return this.in_reply_to_user_id;
    }

    public void setIn_reply_to_user_id(String in_reply_to_user_id) {
        this.in_reply_to_user_id = in_reply_to_user_id;
    }

    public String getRepost_screen_name() {
        return this.repost_screen_name;
    }

    public void setRepost_screen_name(String repost_screen_name) {
        this.repost_screen_name = repost_screen_name;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean getFavorited() {
        return this.favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.repost_user_id);
        dest.writeString(this.in_reply_to_status_id);
        dest.writeParcelable(this.repost_status, flags);
        dest.writeLong(this.created_at != null ? this.created_at.getTime() : -1);
        dest.writeByte(this.truncated ? (byte) 1 : (byte) 0);
        dest.writeString(this.source);
        dest.writeString(this.in_reply_to_screen_name);
        dest.writeString(this.repost_status_id);
        dest.writeInt(this.rawid);
        dest.writeString(this.in_reply_to_user_id);
        dest.writeString(this.repost_screen_name);
        dest.writeString(this.id);
        dest.writeString(this.text);
        dest.writeByte(this.favorited ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.photo, flags);
        dest.writeParcelable(this.user, flags);
    }

    public Status() {
    }

    protected Status(Parcel in) {
        this.repost_user_id = in.readString();
        this.in_reply_to_status_id = in.readString();
        this.repost_status = in.readParcelable(Status.class.getClassLoader());
        long tmpCreated_at = in.readLong();
        this.created_at = tmpCreated_at == -1 ? null : new Date(tmpCreated_at);
        this.truncated = in.readByte() != 0;
        this.source = in.readString();
        this.in_reply_to_screen_name = in.readString();
        this.repost_status_id = in.readString();
        this.rawid = in.readInt();
        this.in_reply_to_user_id = in.readString();
        this.repost_screen_name = in.readString();
        this.id = in.readString();
        this.text = in.readString();
        this.favorited = in.readByte() != 0;
        this.photo = in.readParcelable(StatusPhoto.class.getClassLoader());
        this.user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Parcelable.Creator<Status> CREATOR = new Parcelable.Creator<Status>() {
        @Override
        public Status createFromParcel(Parcel source) {
            return new Status(source);
        }

        @Override
        public Status[] newArray(int size) {
            return new Status[size];
        }
    };
}
