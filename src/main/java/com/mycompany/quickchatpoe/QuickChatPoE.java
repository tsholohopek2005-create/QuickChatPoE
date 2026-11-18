package com.mycompany.quickchatpoe;

import java.util.*;
import java.util.stream.Collectors;

public class QuickChatPoE {

    private static final Scanner sc = new Scanner(System.in);
    private static int messageCount  = 0;
    private static int messageNumber = 0;

    public static void main(String[] args) {
        Login login = new Login();

        System.out.println("=== QuickChat Registration ===");
        System.out.print("First name: ");           String fn = sc.nextLine();
        System.out.print("Last name: ");            String ln = sc.nextLine();
        System.out.print("Username: ");             String un = sc.nextLine();
        System.out.print("Password: ");             String pw = sc.nextLine();
        System.out.print("Cell phone (+27...): ");  String cp = sc.nextLine();

        String regResult = login.registerUser(fn, ln, un, pw, cp);
        System.out.println("\n" + regResult);

        System.out.println("\n=== Login ===");
        System.out.print("Username: "); String lu = sc.nextLine();
        System.out.print("Password: "); String lp = sc.nextLine();
        System.out.println(login.returnLoginStatus(lu, lp));

        if (!login.loginUser(lu, lp)) return;

        System.out.println("\nWelcome to QuickChat.");
        System.out.print("How many messages do you wish to enter? ");
        int capacity = Integer.parseInt(sc.nextLine());

        boolean running = true;
        while (running) {
            System.out.println("\n1) Send Messages\n2) Show recently sent messages\n3) Quit\n4) Stored Messages");
            System.out.print("Choose: ");
            int choice = Integer.parseInt(sc.nextLine());
            switch (choice) {
                case 1: handleSendMessage(capacity); break;
                case 2: System.out.println("Coming Soon."); break;
                case 3: running = false; break;
                case 4: handleStoredMenu(); break;
                default: System.out.println("Invalid choice.");
            }
        }
        System.out.println("Goodbye!");
    }

    private static void handleSendMessage(int capacity) {
        if (messageCount >= capacity) {
            System.out.println("You have reached the message limit (" + capacity + ").");
            return;
        }
        System.out.print("Recipient (+27...): ");    String recipient = sc.nextLine();
        System.out.print("Message (<=250 chars): "); String text     = sc.nextLine();

        String id = String.format("%010d", (long)(new Random().nextDouble() * 10_000_000_000L));
        Message m = new Message(id, messageNumber, recipient, text);

        System.out.println(m.checkMessageLength());
        if (text.length() > 250) return;

        System.out.println(m.checkRecipientCell(recipient));
        if (!recipient.matches("^\\+\\d{1,3}\\d{9}$")) return;

        System.out.println("Message ID generated: " + id);
        System.out.println("Message Hash: " + m.getMessageHash());

        System.out.println("\n1) Send Message\n2) Disregard Message\n3) Store Message");
        System.out.print("Choose: ");
        int action = Integer.parseInt(sc.nextLine());
        System.out.println(m.SentMessage(action));

        if (action == 1) {
            System.out.println("\n--- Message Sent ---\nID: " + id + "\nHash: " + m.getMessageHash() + "\nTo: " + recipient + "\nMsg: " + text);
            messageCount++;
            messageNumber++;
        } else if (action == 3) {
            JSONHandler.save(Message.getStoredMessages());
        }
    }

    private static void handleStoredMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\na) Sender & recipient\nb) Longest message\nc) Search by ID\nd) Search by recipient\ne) Delete by hash\nf) Full report\nx) Back");
            System.out.print("Choose: ");
            String opt = sc.nextLine().toLowerCase();
            List<StoredMessage> stored = JSONHandler.load();

            switch (opt) {
                case "a": 
                    if (stored.isEmpty()) { System.out.println("No stored messages."); break; }
                    stored.forEach(s -> System.out.println("Sender: You | Recipient: " + s.getRecipient())); 
                    break;
                case "b": 
                    stored.stream().max(Comparator.comparingInt(s -> s.getMessage().length())).ifPresent(System.out::println); 
                    break;
                case "c":
                    System.out.print("Enter ID: "); String id = sc.nextLine();
                    stored.stream().filter(s -> s.getMessageID().equals(id)).forEach(System.out::println); 
                    break;
                case "d":
                    System.out.print("Enter recipient: "); String rec = sc.nextLine();
                    stored.stream().filter(s -> s.getRecipient().equals(rec)).forEach(System.out::println); 
                    break;
                case "e":
                    System.out.print("Enter hash: "); String hash = sc.nextLine();
                    System.out.println(JSONHandler.deleteByHash(hash) ? "Deleted." : "Not found."); 
                    break;
                case "f": 
                    if (stored.isEmpty()) System.out.println("No stored messages.");
                    else stored.forEach(System.out::println); 
                    break;
                case "x": back = true; break;
                default: System.out.println("Invalid option.");
            }
        }
    }
}