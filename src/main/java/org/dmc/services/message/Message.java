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
    
    Message(int id, int senderId, int recipientId,
            boolean isRead, boolean senderDelete, boolean recipientDelete,
            String text, String created_at) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.isRead = isRead;
        this.senderDelete = senderDelete;
        this.recipientDelete = recipientDelete;
        this.text = text;
        this.created_at = created_at;
        this.id = id;
    }
    
    public int getSenderId() {
        return senderId;
    }
    
    public int getRecipientId() {
        return recipientId;
    }
    
    public boolean getIsRead() {
        return isRead;
    }
    
    public boolean getSenderDelete() {
        return senderDelete;
    }
    
    public boolean getRecipientDelete() {
        return recipientDelete;
    }
    
    public String getText() {
        return text;
    }
    
    public String getCreated_at() {
        return created_at;
    }
    
    public int id() {
        return id;
    }
}