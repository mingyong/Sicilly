package xyz.shaohui.sicilly.leanCloud.model;

import com.google.gson.annotations.Expose;

/**
 * Created by shaohui on 16/10/6.
 */

public class ActiveUser {

    public ActiveUser(String userId, String userName, String regId) {
        user_id = userId;
        user_name = userName;
        reg_id = regId;
    }

    @Expose
    private String user_id;

    private String user_name;

    private String reg_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
