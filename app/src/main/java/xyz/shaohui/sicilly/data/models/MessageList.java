package xyz.shaohui.sicilly.data.models;

import java.io.Serializable;

/**
 * Created by kpt on 16/3/13.
 */
public class MessageList implements Serializable, Comparable {

    private Message message;

    private String otherId;

    private int msgNum;

    private boolean haveNew;

    private long updateTime;

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void setOtherId(String otherId) {
        this.otherId = otherId;
    }

    public void setMsgNum(int msgNum) {
        this.msgNum = msgNum;
    }

    public void setHaveNew(boolean haveNew) {
        this.haveNew = haveNew;
    }

    public Message getMessage() {
        return message;
    }

    public String getOtherId() {
        return otherId;
    }

    public int getMsgNum() {
        return msgNum;
    }

    public boolean isHaveNew() {
        return haveNew;
    }

    @Override
    public int compareTo(Object another) {
        if (this.updateTime > ((MessageList) another).updateTime) {
            return 1;
        } else {
            return -1;
        }
    }
}
