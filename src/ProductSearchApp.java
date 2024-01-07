import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ProductSearchApp {
    private static Map<String, List<String>> categories = new HashMap<>();

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            displayMainMenu();
            System.out.print("Enter your choice (1/2/3/4/5/6): ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    ImportData("../resources/lookup_item_clean.csv");
                case 2:
                    browseByCategories(scanner);
                    break;
                case 3:
                    SearchForProduct.setSearchData(categories);
                    SearchForProduct.searchForProduct(scanner);
                    break;
                case 4:
                    ShoppingCart.viewShoppingCart();
                    break;
                case 5:
                    AccountSettings.displayAccountSettings(scanner);
                    break;
                case 6:
                    System.out.println("Exiting the application. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 6);

        scanner.close();
    }

    public static void ImportData(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            System.out.println("data before import");
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String itemGroup = parts[0].trim();
                String itemCategory = parts[1].trim();
                String subCategory = parts[2].trim();
                categories.computeIfAbsent(itemGroup, k -> new ArrayList<>()).add(itemCategory + " - " + subCategory);
            }
                        System.out.println("data after import");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void displayMainMenu() {
        System.out.println("Welcome to Product Search and Selection");
        System.out.println("1. Import Data");
        System.out.println("2. Browse by Categories");
        System.out.println("3. Search for a Product");
        System.out.println("4. View Shopping Cart");
        System.out.println("5. Account Settings");
        System.out.println("6. Exit");
    }

    public static void browseByCategories(Scanner scanner) {
        int choice;

        do {
            displayCategories();
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            if (choice >= 1 && choice <= categories.size()) {
                List<String> subCategories = new ArrayList<>(categories.values()).get(choice - 1);

                do {
                    displaySubCategories(subCategories);
                    System.out.print("Enter your choice: ");
                    choice = scanner.nextInt();

                    if (choice >= 1 && choice <= subCategories.size()) {
                        System.out.println("You have selected '" + subCategories.get(choice - 1) + "'.");
                        // Now you can further browse or search for specific items
                        // Implement your logic here
                    } else if (choice != subCategories.size() + 1) {
                        System.out.println("Invalid choice. Please try again.");
                    }
                } while (choice != subCategories.size() + 1);
            } else if (choice != categories.size() + 1) {
                System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != categories.size() + 1);
    }

    public static void displayCategories() {
        int index = 1;
        //import category  from vsc file 
        System.out.println("Select a Category:");
        for (String itemGroup : categories.keySet()) {
            System.out.println(index + ". " + itemGroup);
            index++;
        }
        System.out.println(index + ". Back to Main Menu");
    }

    public static void displaySubCategories(List<String> subCategories) {
        int index = 1;
        System.out.println("Select sub-category:");
        for (String subCategory : subCategories) {
            System.out.println(index + ". " + subCategory);
            index++;
        }
        System.out.println(index + ". Back to Menu");
    }
}