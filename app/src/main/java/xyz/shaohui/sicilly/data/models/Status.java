package xyz.shaohui.sicilly.data.models;

import java.util.Date;

public class Status {
    private String repost_user_id;
    private String in_reply_to_status_id;
    private Status repost_status;
    private String created_at;
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

    public String getCreated_at() {
        return this.created_at;
    }

    public void setCreated_at(String created_at) {
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
}
