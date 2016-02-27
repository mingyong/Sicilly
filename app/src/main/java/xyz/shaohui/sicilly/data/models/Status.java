package xyz.shaohui.sicilly.data.models;

/**
 * Created by kpt on 16/2/22.
 */
public class Status {

    private String createdAt;

    private String id;

    private int rawid;

    private String text;

    private String source;

    private String replyStatusId;

    private String replyUserId;

    private String repostStatusId;

    private String repostStatus;

    private String repostUserId;

    private boolean favorited;

    private String replyUserName;

    private String userId;

    private User user;

    private String imageUrl;

    private String imageThumbUrl;

    private String imageLargeUrl;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRawid(int rawid) {
        this.rawid = rawid;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setReplyStatusId(String replyStatusId) {
        this.replyStatusId = replyStatusId;
    }

    public void setReplyUserId(String replyUserId) {
        this.replyUserId = replyUserId;
    }

    public void setRepostStatusId(String repostStatusId) {
        this.repostStatusId = repostStatusId;
    }

    public void setRepostStatus(String repostStatus) {
        this.repostStatus = repostStatus;
    }

    public void setRepostUserId(String repostUserId) {
        this.repostUserId = repostUserId;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public void setReplyUserName(String replyUserName) {
        this.replyUserName = replyUserName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setImageThumbUrl(String imageThumbUrl) {
        this.imageThumbUrl = imageThumbUrl;
    }

    public void setImageLargeUrl(String imageLargeUrl) {
        this.imageLargeUrl = imageLargeUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getId() {
        return id;
    }

    public int getRawid() {
        return rawid;
    }

    public String getText() {
        return text;
    }

    public String getSource() {
        return source;
    }

    public String getReplyStatusId() {
        return replyStatusId;
    }

    public String getReplyUserId() {
        return replyUserId;
    }

    public String getRepostStatusId() {
        return repostStatusId;
    }

    public String getRepostStatus() {
        return repostStatus;
    }

    public String getRepostUserId() {
        return repostUserId;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public String getReplyUserName() {
        return replyUserName;
    }

    public String getUserId() {
        return userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageThumbUrl() {
        return imageThumbUrl;
    }

    public String getImageLargeUrl() {
        return imageLargeUrl;
    }
}
