package xyz.shaohui.sicilly.data.models;

import com.google.gson.JsonObject;

/**
 * Created by kpt on 16/2/22.
 */
public class User {

    private String id;

    private String name;

    private String nickName;

    private String location;

    private String gender;

    private String birthday;

    private String description;

    private String profileImageUrl;

    private String profileImageLargeUrl;

    private String url;

    private boolean secreted;

    private int followersCount;

    private int friendCount;

    private int statusesCount;

    private boolean following;

    private String createdAt;

    private int utcoffset;

    private Status firstStatus;

    public Status getFirstStatus() {
        return firstStatus;
    }

    public void setFirstStatus(Status firstStatus) {
        this.firstStatus = firstStatus;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setProfileImageLargeUrl(String profileImageLargeUrl) {
        this.profileImageLargeUrl = profileImageLargeUrl;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setSecreted(boolean secreted) {
        this.secreted = secreted;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public void setFriendCount(int friendCount) {
        this.friendCount = friendCount;
    }

    public void setStatusesCount(int statusesCount) {
        this.statusesCount = statusesCount;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUtcoffset(int utcoffset) {
        this.utcoffset = utcoffset;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNickName() {
        return nickName;
    }

    public String getLocation() {
        return location;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getDescription() {
        return description;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getProfileImageLargeUrl() {
        return profileImageLargeUrl;
    }

    public String getUrl() {
        return url;
    }

    public boolean isSecreted() {
        return secreted;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFriendCount() {
        return friendCount;
    }

    public int getStatusesCount() {
        return statusesCount;
    }

    public boolean isFollowing() {
        return following;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getUtcoffset() {
        return utcoffset;
    }

    public static User toObject(JsonObject json) {
        User user = new User();

        user.setId(json.get("id").getAsString());
        user.setName(json.get("name").getAsString());
        user.setNickName(json.get("screen_name").getAsString());
        user.setLocation(json.get("location").getAsString());
        user.setGender(json.get("gender").getAsString());
        user.setBirthday(json.get("birthday").getAsString());
        user.setDescription(json.get("description").getAsString());
        user.setProfileImageUrl(json.get("profile_image_url").getAsString());
        user.setProfileImageLargeUrl(json.get("profile_image_url_large").getAsString());
        user.setUrl(json.get("url").getAsString());
        user.setSecreted(json.get("protected").getAsBoolean());

        user.setFollowersCount(json.get("followers_count").getAsInt());
        user.setFriendCount(json.get("friends_count").getAsInt());
        user.setStatusesCount(json.get("statuses_count").getAsInt());

        user.setFollowing(json.get("following").getAsBoolean());

        return user;
    }
}
