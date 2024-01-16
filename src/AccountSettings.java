import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AccountSettings {
    private static Map<String, String> userAccounts = new HashMap<>();
    private static final String DATA_FILE = "user_data.txt";

    public void loadUserAccounts() {
        try (BufferedReader buffer = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = buffer.readLine()) != null) {
                String[] parts = line.split(",");
                String username = parts[0].trim();
                String password = parts[1].trim();
                userAccounts.put(username, password);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveUserAccounts() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (Map.Entry<String, String> entry : userAccounts.entrySet()) {
                bw.write(entry.getKey() + "," + entry.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void changePassword(String username, String newPassword) {
        if (userAccounts.containsKey(username)) {
            userAccounts.put(username, newPassword);
            saveUserAccounts();
            System.out.println("Password changed successfully.");
        } else {
            System.out.println("Username not found. Please try again.");
        }
    }

    public static void changeUsername(String currentUsername, String newUsername) {
        if (userAccounts.containsKey(currentUsername)) {
            String password = userAccounts.remove(currentUsername);
            userAccounts.put(newUsername, password);
            saveUserAccounts();
            System.out.println("Username changed successfully.");
        } else {
            System.out.println("Username not found. Please try again.");
        }
    }

    public static void displayAccountSettings(Scanner scanner) {
        System.out.print("Enter your current username: ");
        String currentUsername = scanner.next();

        System.out.print("Enter your current password: ");
        String currentPassword = scanner.next();

        if (userAccounts.containsKey(currentUsername) && userAccounts.get(currentUsername).equals(currentPassword)) {
            System.out.println("Account Settings:");
            System.out.println("1. Change Password");
            System.out.println("2. Change Username");
            System.out.println("3. Back to Main Menu");

            System.out.print("Enter your choice (1/2/3): ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter your new password: ");
                    String newPassword = scanner.next();
                    changePassword(currentUsername, newPassword);
                    break;
                case 2:
                    System.out.print("Enter your new username: ");
                    String newUsername = scanner.next();
                    changeUsername(currentUsername, newUsername);
                    break;
                case 3:
                    System.out.println("Returning to the main menu.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }

    public boolean authenticateUser(String username, String password) {
        loadUserAccounts();
    
        if (userAccounts.containsKey(username) && userAccounts.get(username).equals(password)) {
            return true; // Authentication successful
        } else {
            return false; // Authentication failed
        }
    }
    
    public void modifyUserDetails(String currentUsername, String currentPassword, String newUsername, String newPassword) {
        loadUserAccounts();

        if (userAccounts.containsKey(currentUsername) && userAccounts.get(currentUsername).equals(currentPassword)) {
            // Authentication successful
            if (!newUsername.equals(currentUsername)) {
                // Change username
                if (!userAccounts.containsKey(newUsername)) {
                    String password = userAccounts.remove(currentUsername);
                    userAccounts.put(newUsername, password);
                } else {
                    System.out.println("Username already exists. Please choose a different username.");
                    return;
                }
            }

            if (!newPassword.equals(currentPassword)) {
                // Change password
                userAccounts.put(currentUsername, newPassword);
            }

            saveUserAccounts();
            System.out.println("Details modified successfully.");

        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }
    
}
