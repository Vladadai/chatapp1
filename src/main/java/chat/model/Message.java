package chat.model;

import java.sql.Timestamp;

public class Message {

    private int id;
    private String sender;
    private String message;
    private Timestamp createdAt;

    public Message() {
    }

    public Message(
            int id,
            String sender,
            String message,
            Timestamp createdAt) {

        this.id = id;
        this.sender = sender;
        this.message = message;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}