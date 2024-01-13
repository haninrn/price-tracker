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
        CSVReader csvReader = new CSVReader();
        List<LookupItem> lookupItems = csvReader.readLookupItemCSV("C:\\Users\\HP\\Assignment\\price-tracker\\resources\\lookup_item_clean.csv");

        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            displayMainMenu();
            System.out.print("Enter your choice (1/2/3/4/5/6): ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    ImportData("resources/lookup_item_clean.csv");
                    break;
                case 2:
                    String csvFilePath = "C:\\Users\\HP\\Assignment\\price-tracker\\resources\\lookup_item_clean.csv";
                    ItemManager itemManager = new ItemManager(csvFilePath);

                    runItemManager(itemManager);
                    System.out.println("Program exited.");

                    break;
                case 3:
                    SearchForProduct.searchForProduct(scanner);
                    SearchForProduct.searchForProduct(scanner);
                    break;
                case 4:
                    ShoppingCart shoppingCart = new ShoppingCart();
                    shoppingCart.viewShoppingCart();
                    shoppingCart.displayInnerSCMenu();

                    int choiceSC = scanner.nextInt();
                    if(choiceSC==1){
                    }
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

        //scanner.close();
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

    private static void runItemManager(ItemManager itemManager) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Display item groups
            System.out.println("Select a category");
            itemManager.displayItemGroups();

            // Get user input for selecting item group
            System.out.print("Enter your choice (or enter 'exit' to quit): ");
            String selectedGroup = scanner.nextLine();

            if ("exit".equalsIgnoreCase(selectedGroup)) {
                break; // Exit the loop if the user enters 'exit'
            }

            // Display item categories for the selected group
            itemManager.displayItemCategories(selectedGroup);

            // Get user input for selecting item category
            System.out.print("Select subcategory: ");
            String selectedItemCategory = scanner.nextLine();

            // Process the selected item category
            itemManager.displayItemCategories(selectedItemCategory);
        }
        scanner.close();
    }
}