import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SearchForProduct {
    private static Map<String, List<String>> categories;

    public static void setSearchData(Map<String, List<String>> categoriesData) {
        categories = categoriesData;
    }

    public static void searchForProduct(Scanner scanner) {
        System.out.print("Enter the name of the product to search: ");
        String searchTerm = scanner.next();

        boolean found = false;

        for (List<String> subCategories : categories.values()) {
            for (String subCategory : subCategories) {
                if (subCategory.toLowerCase().contains(searchTerm.toLowerCase())) {
                    System.out.println("Product found in category: " + subCategory);
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("Product not found.");
        }
    }
}
