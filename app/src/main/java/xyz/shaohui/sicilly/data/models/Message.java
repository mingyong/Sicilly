package xyz.shaohui.sicilly.data.models;

import com.google.gson.JsonObject;

/**
 * Created by kpt on 16/3/13.
 */
public class Message {

    private String id;

    private String text;

    private String senderId;

    private String recipientId;

    private String createdTime;

    private String senderName;

    private String recipientName;

    private User sender;

    private User recipient;

    private boolean sending;

    public boolean isSending() {
        return sending;
    }

    public void setSending(boolean sending) {
        this.sending = sending;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public User getSender() {
        return sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public static Message toObject(JsonObject json) {
        Message message = new Message();

        message.setId(json.get("id").getAsString());
        message.setText(json.get("text").getAsString());
        message.setSenderId(json.get("sender_id").getAsString());
        message.setRecipientId(json.get("recipient_id").getAsString());
        message.setCreatedTime(json.get("created_at").getAsString());
        message.setSenderName(json.get("sender_screen_name").getAsString());
        message.setRecipientName(json.get("recipient_screen_name").getAsString());

        message.setSender(User.toObject(json.get("sender").getAsJsonObject()));
        message.setRecipient(User.toObject(json.get("recipient").getAsJsonObject()));

        return message;
    }
}
