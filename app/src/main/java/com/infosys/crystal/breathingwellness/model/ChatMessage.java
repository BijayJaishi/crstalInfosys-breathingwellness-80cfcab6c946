package com.infosys.crystal.breathingwellness.model;


import java.util.Date;

public class ChatMessage {

    private String messageType;
    private String messageText;
    private String messageUser;
    private long messageTime;
    private String image;
    private String groupname;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ChatMessage(String messageType,String messageText, String messageUser, String image ,String groupname) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.image = image;
        this.messageType = messageType;
        this.groupname = groupname;

        messageTime = new Date().getTime();
    }

    public ChatMessage() {
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}