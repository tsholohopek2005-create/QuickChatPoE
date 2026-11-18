package com.mycompany.quickchatpoe;

public class StoredMessage {
    private final String messageID;
    private final String recipient;
    private final String message;
    private final String hash;

    public StoredMessage(String id, String rec, String msg, String hash) {
        this.messageID = id;
        this.recipient = rec;
        this.message   = msg;
        this.hash      = hash;
    }

    public String getMessageID() { return messageID; }
    public String getRecipient() { return recipient; }
    public String getMessage()   { return message; }
    public String getHash()      { return hash; }

    @Override
    public String toString() {
        return String.format("ID: %s | Recipient: %s | Msg: %s | Hash: %s",
                messageID, recipient, message, hash);
    }
}
