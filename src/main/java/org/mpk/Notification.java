package org.mpk;

import java.time.LocalDateTime;

public class Notification {
    private int id;
    private String content;
    private String type;
    private LocalDateTime sentDate;

    public Notification(int id, String content, String type, LocalDateTime sentDate) {
        this.id = id;
        this.content = content;
        this.type = type;
        this.sentDate = sentDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getSentDate() {
        return sentDate;
    }

    public void setSentDate(LocalDateTime sentDate) {
        this.sentDate = sentDate;
    }
}
