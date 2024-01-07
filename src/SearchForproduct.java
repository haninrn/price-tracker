import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SearchForProduct {
    public static Map<String, List<String>> categories;

    public static void setSearchData(Map<String, List<String>> categoriesData) {
        categories = categoriesData;
    }

    public static void searchForProduct(Scanner scanner) {
        System.out.print("Enter the name of the product to search: ");
        String searchTerm = scanner.nextLine();
        System.out.println("Search term: " + searchTerm);
        if (categories.isEmpty()) {
            System.out.println("No categories available. Please import data first.");
            return;
        }
        boolean found = false;
        List<String> foundProducts = new ArrayList<>();
        for (List<String> subCategories : categories.values()) {
            for (String subCategory : subCategories) {
                if (subCategory.toLowerCase().contains(searchTerm.toLowerCase())) {
                    foundProducts.add(subCategory);
                    found = true;
                }
            }
        }

        if (!foundProducts.isEmpty()) {
            System.out.println("Choose a product:");

            for (int i = 0; i < foundProducts.size(); i++) {
                System.out.println((i + 1) + ". " + foundProducts.get(i));
            }

            System.out.print("Enter the number of the product you want to choose: ");
        //     int userChoice = scanner.nextInt();

        //     if (userChoice >= 1 && userChoice <= foundProducts.size()) {
        //         String chosenProduct = foundProducts.get(userChoice - 1);
        //         System.out.println("You have chosen: " + chosenProduct);
        //         // Add your logic to handle the chosen product
        //     } else {
        //         System.out.println("Invalid choice. Please try again.");
        //     }
        }

        if (!found) {
            System.out.println("Product not found.");
        }
    }
}

