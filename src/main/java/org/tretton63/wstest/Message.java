package org.tretton63.wstest;

import java.time.OffsetDateTime;

public class Message {

    private String name;
    private OffsetDateTime createdAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Message(String name, OffsetDateTime createdAt) {
        this.name = name;
        this.createdAt = createdAt;
    }
}
