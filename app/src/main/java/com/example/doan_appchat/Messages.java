package com.example.doan_appchat;



public class Messages {
    private String from, to, message, type, messageID;

    // Constructor mặc định (có thể không cần thiết nếu không sử dụng)
    public Messages() {
    }

    // Constructor với các tham số cần thiết
    public Messages(String from, String to, String message, String type) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.type = type;
        this.messageID = ""; // Hoặc bạn có thể tạo một messageID ngẫu nhiên ở đây
    }

    // Getter và Setter cho các trường
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = (from != null) ? from : "";
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = (to != null) ? to : "";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = (message != null) ? message : "";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = (type != null) ? type : "";
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = (messageID != null) ? messageID : "";
    }
}
