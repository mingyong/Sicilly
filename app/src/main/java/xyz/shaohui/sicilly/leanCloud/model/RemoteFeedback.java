package xyz.shaohui.sicilly.leanCloud.model;

import com.google.auto.value.AutoValue;

/**
 * Created by shaohui on 16/10/15.
 */

public class RemoteFeedback {

    public RemoteFeedback(String userId, String userName, String regId, String text,
            String phoneMode, String phoneBrands, String versionSDK, String versionRelease,
            String versionApp) {
        this.userId = userId;
        this.userName = userName;
        this.regId = regId;
        this.text = text;
        this.phoneMode = phoneMode;
        this.phoneBrands = phoneBrands;
        this.versionSDK = versionSDK;
        this.versionRelease = versionRelease;
        this.versionApp = versionApp;
    }

    public static RemoteFeedback create(String userId, String userName, String regId, String text,
            String phoneMode, String phoneBrands, String versionSDK, String versionRelease,
            String versionApp) {
        return new RemoteFeedback(userId, userName, regId, text, phoneMode, phoneBrands,
                versionSDK, versionRelease, versionApp);
    }

    private String userId;

    private String userName;

    private String regId;

    private String text;

    private String phoneMode;

    private String phoneBrands;

    private String versionSDK;

    private String versionRelease;

    private String versionApp;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPhoneMode() {
        return phoneMode;
    }

    public void setPhoneMode(String phoneMode) {
        this.phoneMode = phoneMode;
    }

    public String getPhoneBrands() {
        return phoneBrands;
    }

    public void setPhoneBrands(String phoneBrands) {
        this.phoneBrands = phoneBrands;
    }

    public String getVersionSDK() {
        return versionSDK;
    }

    public void setVersionSDK(String versionSDK) {
        this.versionSDK = versionSDK;
    }

    public String getVersionRelease() {
        return versionRelease;
    }

    public void setVersionRelease(String versionRelease) {
        this.versionRelease = versionRelease;
    }

    public String getVersionApp() {
        return versionApp;
    }

    public void setVersionApp(String versionApp) {
        this.versionApp = versionApp;
    }
}
