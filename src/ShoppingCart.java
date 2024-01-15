import java.util.*;

public class ShoppingCart {
    private static Map<String, Integer> shoppingCart = new HashMap<>();

    CSVReader csvReader = new CSVReader();

    List<LookupItem> lookupItems = csvReader.readLookupItemCSV("lookup_item.csv");
    List<LookupPremise> lookupPremises = csvReader.readLookupPremiseCSV("lookup_premise.csv");
    List<PriceCatcher> priceCatchers = csvReader.readPriceCatcherCSV("pricecatcher_2023-08.csv");

    public void displayInnerSCMenu(){
        System.out.println("Shopping Cart");
        System.out.println("1. View cheapest seller for all selected items");
        System.out.println("2. Find shops to buy items in cart");

    }

    public void addToCart(String selecteditem) {
        shoppingCart.put(selecteditem, shoppingCart.getOrDefault(selecteditem, 0) + 1);
        System.out.println("Product added to the shopping cart: " + selecteditem);
    }

    public void viewShoppingCart() {
        if (shoppingCart.isEmpty()) {
            System.out.println("Shopping cart is empty.");
        } else {
            System.out.println("Shopping Cart:");
            for (Map.Entry<String, Integer> entry : shoppingCart.entrySet()) {
                System.out.println(entry.getKey() + " - Quantity: " + entry.getValue());
            }
        }
    }

    public Map<String, List<String>> getItems() {
        // Assuming there is a method to get details for each item
        Map<String, List<String>> itemsDetails = new HashMap<>();

        for (Map.Entry<String, Integer> entry : shoppingCart.entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();
            List<String> itemDetails = getItemDetails(itemName, quantity);
        }

        return itemsDetails;
    }

    public LookupItem findItemByName(String itemName) {
        for (LookupItem item : lookupItems) {
            if (item.getItem().equals(itemName)) {
                return item;
            }
        }
        return null;
    }

    private List<String> getItemDetails(String itemName, int quantity) {
        LookupItem lookupItem = findItemByName(itemName);
    
        if (lookupItem != null) {
            String itemCode = lookupItem.getItemCode();
            String unit = lookupItem.getUnit();
            String itemGroup = lookupItem.getItemGroup();
            String itemCategory = lookupItem.getItemCategory();
    
            return List.of(itemCode, unit, itemGroup, itemCategory, "Quantity: " + quantity);
        } else {
            return List.of("Item not found");
        }
    }
    
    public void viewCheapestSellerForCart() {
        double totalCost = 0.0;

        ItemDetails itemDetailsVCSFC = new ItemDetails();

        for (Map.Entry<String, Integer> entry : shoppingCart.entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();

            String itemsFilePath = "resources/lookup_item_clean.csv";
            String premiseFilePath="resources/lookup_premise_clean.csv";
            String pricesFilePath="resources/pricecatcher_2023-08.csv";

            List<String[]> cheapestPrices = itemDetailsVCSFC.findCheapestPrices(itemName, itemsFilePath, pricesFilePath, premiseFilePath); //findCheapestPrices - in ItemDetails.java
            if (!cheapestPrices.isEmpty()) {
                // Assuming the cheapest seller is the first one in the list
                String cheapestPrice = cheapestPrices.get(0)[3];
                totalCost += Double.parseDouble(cheapestPrice) * quantity;
            }
        }
 
        if (totalCost > 0) {
            System.out.println("Cheapest Seller for the entire cart:");
            System.out.println("Total Cost: RM" + totalCost);
        } else {
            System.out.println("No prices found for the entire cart.");
        }

    }

    public Map<String, Integer> getShoppingCart() {
        return shoppingCart;
    }

}