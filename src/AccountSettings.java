import java.io.*;
import java.util.Scanner;

public class AccountSettings {

    public void runAccountSettings() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("==== Main Menu ====");
            System.out.println("1. Login");
            System.out.println("2. Exit");
            System.out.print("Enter your choice: ");

            int mainChoice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            switch (mainChoice) {
                case 1:
                    authenticateUser(scanner);
                    break;
                case 2:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void authenticateUser(Scanner scanner) {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        if (isValidCredentials(username, password)) {
            System.out.println("Authentication successful!");
            displayAccountSettings(scanner, username);
        } else {
            System.out.println("Authentication failed. Type 'exit' to go back to the main menu or press Enter to try again.");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                return;
            }
        }
    }

    private boolean isValidCredentials(String enteredUsername, String enteredPassword) {
        try {
            File file = new File("user_data.txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[0].equals(enteredUsername) && parts[1].equals(enteredPassword)) {
                    reader.close();
                    return true;
                }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void displayAccountSettings(Scanner scanner, String username) {
        while (true) {
            System.out.println("==== Account Settings ====");
            System.out.println("1. Change Username");
            System.out.println("2. Change Password");
            System.out.println("3. Change Email");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            switch (choice) {
                case 1:
                    changeField(scanner, "username", username);
                    break;
                case 2:
                    changeField(scanner, "password", username);
                    break;
                case 3:
                    changeField(scanner, "email", username);
                    break;
                case 4:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void changeField(Scanner scanner, String fieldToUpdate, String username) {
        System.out.print("Enter your new " + fieldToUpdate + ": ");
        String newValue = scanner.nextLine();

        // Read the data.txt file, update the field, and write back
        updateUserData(username, fieldToUpdate, newValue);

        System.out.println(fieldToUpdate.substring(0, 1).toUpperCase() + fieldToUpdate.substring(1) +
                " changed successfully!");
    }

    private void updateUserData(String username, String fieldToUpdate, String newValue) {
        try {
            File file = new File("user_data.txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            StringBuilder updatedContent = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[0].equals(username)) {
                    if (fieldToUpdate.equals("username")) {
                        updatedContent.append(newValue).append(",").append(parts[1]).append(",").append(parts[2]).append("\n");
                    } else if (fieldToUpdate.equals("password")) {
                        updatedContent.append(parts[0]).append(",").append(newValue).append(",").append(parts[2]).append("\n");
                    } else if (fieldToUpdate.equals("email")) {
                        updatedContent.append(parts[0]).append(",").append(parts[1]).append(",").append(newValue).append("\n");
                    }
                } else {
                    updatedContent.append(line).append("\n");
                }
            }

            reader.close();

            // Write the updated content back to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(updatedContent.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
