package xyz.shaohui.sicilly.data.models;

import java.util.Date;

/**
 * Created by shaohui on 16/8/19.
 */
public class Message {

    private String id;
    private String text;
    private String recipient_id;
    private String recipient_screen_name;
    private String sender_screen_name;
    private Date created_at;
    private User sender;
    private User recipient;
    private Message in_reply_to;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRecipient_id() {
        return recipient_id;
    }

    public void setRecipient_id(String recipient_id) {
        this.recipient_id = recipient_id;
    }

    public String getRecipient_screen_name() {
        return recipient_screen_name;
    }

    public void setRecipient_screen_name(String recipient_screen_name) {
        this.recipient_screen_name = recipient_screen_name;
    }

    public String getSender_screen_name() {
        return sender_screen_name;
    }

    public void setSender_screen_name(String sender_screen_name) {
        this.sender_screen_name = sender_screen_name;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public Message getIn_reply_to() {
        return in_reply_to;
    }

    public void setIn_reply_to(Message in_reply_to) {
        this.in_reply_to = in_reply_to;
    }
}
