package org.dmc.services.message;

public class Message {
    private final int senderId;
    private final int recipientId;
    private final boolean isRead;
    private final boolean senderDelete;
    private final boolean recipientDelete;
    private final String text;
    private final String created_at;
    private final int id;
    
    Message() {
        this.senderId = -1;
        this.recipientId = -1;
        this.isRead = false;
        this.senderDelete = false;
        this.recipientDelete = false;
        this.text = null;
        this.created_at = null;
        this.id = -1;
    }
}