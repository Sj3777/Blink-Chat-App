package com.example.chatzone;

public class Helper {
    private String Id;
    private String Name;
    private String Image;
    private String message;
    private String Receiver,Sender;
    private String senderName;
    private String senderId;
    private String Email;

    private String textMsg;
    private String Key;
    private long timeStamp;
    private String lastMsg;


    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }




    public String getTextMsg() {
        return textMsg;
    }

    public void setTextMsg(String textMsg) {
        this.textMsg = textMsg;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }




    public String getLastMsg() {
        return lastMsg;
    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return Receiver;
    }

    public void setReceiver(String receiver) {
        Receiver = receiver;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Helper() {
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Helper(String id, String name, String image, String email) {
        this.Id = id;
        Name = name;
        Image = image;
        Email=email;

    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }


}
