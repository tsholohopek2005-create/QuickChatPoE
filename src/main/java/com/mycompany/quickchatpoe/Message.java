package com.mycompany.quickchatpoe;

import java.util.ArrayList;
import java.util.List;

public class Message {
    private final String messageID;
    private final int messageNumber;
    private final String recipient;
    private final String messageText;
    private final String messageHash;

    private static final List<Message> sentMessages        = new ArrayList<>();
    private static final List<Message> disregardedMessages = new ArrayList<>();
    private static final List<Message> storedMessages      = new ArrayList<>();

    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID     = messageID;
        this.messageNumber = messageNumber;
        this.recipient     = recipient;
        this.messageText   = messageText;
        this.messageHash   = createMessageHash();
    }

    public boolean checkMessageID() { 
        return messageID != null && messageID.length() <= 10; 
    }

    public String checkRecipientCell(String cell) {
        if (cell != null && cell.matches("^\\+\\d{1,3}\\d{9}$") && cell.length() <= 13) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    public String checkMessageLength() {
        if (messageText.length() > 250) {
            int over = messageText.length() - 250;
            return "Message exceeds 250 characters by " + over + "; please reduce the size.";
        }
        return "Message ready to send.";
    }

    public String createMessageHash() {
        if (messageID == null || messageID.length() < 2) return "";
        String firstTwo = messageID.substring(0, 2);
        String[] words = messageText.trim().split("\\s+");
        String first = words[0].replaceAll("[^a-zA-Z]", "").toUpperCase();
        String last  = words[words.length - 1].replaceAll("[^a-zA-Z]", "").toUpperCase();
        return (firstTwo + ":" + messageNumber + ":" + first + last).toUpperCase();
    }

    public String SentMessage(int action) {
        switch (action) {
            case 1: sentMessages.add(this);        return "Message successfully sent.";
            case 2: disregardedMessages.add(this); return "Press 0 to delete the message.";
            case 3: storedMessages.add(this);      return "Message successfully stored.";
            default: return "Invalid option.";
        }
    }

    public int returnTotalMessagess() { 
        return sentMessages.size(); 
    }

    public String getMessageID()   { return messageID; }
    public String getRecipient()   { return recipient; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }

    public static List<Message> getStoredMessages() { return storedMessages; }
    public static void resetStore() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
    }
}
