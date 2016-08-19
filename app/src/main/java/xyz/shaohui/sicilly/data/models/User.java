package xyz.shaohui.sicilly.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class User {
    private String birthday;
    private int utc_offset;
    private int friends_count;
    private String gender;
    private String profile_background_image_url;
    private int favourites_count;
    private String description;
    private Date created_at;
    @SerializedName("protected") private boolean is_protected;
    private String screen_name;
    private String profile_link_color;
    private String id;
    private String profile_background_color;
    private String profile_image_url_large;
    private String profile_sidebar_border_color;
    private String profile_text_color;
    private String profile_image_url;
    private String url;
    private boolean profile_background_tile;
    private int statuses_count;
    private int followers_count;
    private boolean following;
    private String name;
    private String location;
    private String profile_sidebar_fill_color;
    private boolean notifications;
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getUtc_offset() {
        return this.utc_offset;
    }

    public void setUtc_offset(int utc_offset) {
        this.utc_offset = utc_offset;
    }

    public int getFriends_count() {
        return this.friends_count;
    }

    public void setFriends_count(int friends_count) {
        this.friends_count = friends_count;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfile_background_image_url() {
        return this.profile_background_image_url;
    }

    public void setProfile_background_image_url(String profile_background_image_url) {
        this.profile_background_image_url = profile_background_image_url;
    }

    public int getFavourites_count() {
        return this.favourites_count;
    }

    public void setFavourites_count(int favourites_count) {
        this.favourites_count = favourites_count;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreated_at() {
        return this.created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public boolean getIs_protected() {
        return this.is_protected;
    }

    public void setIs_protected(boolean is_protected) {
        this.is_protected = is_protected;
    }

    public String getScreen_name() {
        return this.screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getProfile_link_color() {
        return this.profile_link_color;
    }

    public void setProfile_link_color(String profile_link_color) {
        this.profile_link_color = profile_link_color;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfile_background_color() {
        return this.profile_background_color;
    }

    public void setProfile_background_color(String profile_background_color) {
        this.profile_background_color = profile_background_color;
    }

    public String getProfile_image_url_large() {
        return this.profile_image_url_large;
    }

    public void setProfile_image_url_large(String profile_image_url_large) {
        this.profile_image_url_large = profile_image_url_large;
    }

    public String getProfile_sidebar_border_color() {
        return this.profile_sidebar_border_color;
    }

    public void setProfile_sidebar_border_color(String profile_sidebar_border_color) {
        this.profile_sidebar_border_color = profile_sidebar_border_color;
    }

    public String getProfile_text_color() {
        return this.profile_text_color;
    }

    public void setProfile_text_color(String profile_text_color) {
        this.profile_text_color = profile_text_color;
    }

    public String getProfile_image_url() {
        return this.profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean getProfile_background_tile() {
        return this.profile_background_tile;
    }

    public void setProfile_background_tile(boolean profile_background_tile) {
        this.profile_background_tile = profile_background_tile;
    }

    public int getStatuses_count() {
        return this.statuses_count;
    }

    public void setStatuses_count(int statuses_count) {
        this.statuses_count = statuses_count;
    }

    public int getFollowers_count() {
        return this.followers_count;
    }

    public void setFollowers_count(int followers_count) {
        this.followers_count = followers_count;
    }

    public boolean getFollowing() {
        return this.following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfile_sidebar_fill_color() {
        return this.profile_sidebar_fill_color;
    }

    public void setProfile_sidebar_fill_color(String profile_sidebar_fill_color) {
        this.profile_sidebar_fill_color = profile_sidebar_fill_color;
    }

    public boolean getNotifications() {
        return this.notifications;
    }

    public void setNotifications(boolean notifications) {
        this.notifications = notifications;
    }
}
