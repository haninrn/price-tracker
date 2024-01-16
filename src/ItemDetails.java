import java.util.*;
import java.io.*;

public class ItemDetails {
    private Map<String, Integer> shoppingCart = new HashMap<>();

    public static void itemDetailsMainMenu() {
        String lookupItemFilePath = "resources/lookup_item_clean.csv";
        String lookupPremiseFilePath = "resources/lookup_premise_clean.csv";
        String priceCatcherFilePath = "resources/pricecatcher_2023-08.csv";

        CSVReader csvReader = new CSVReader();
        List<LookupItem> lookupItems = csvReader.readLookupItemCSV(lookupItemFilePath);
        List<LookupPremise> lookupPremises = csvReader.readLookupPremiseCSV(lookupPremiseFilePath);
        List<PriceCatcher> priceCatchers = csvReader.readPriceCatcherCSV(priceCatcherFilePath);

        //assume selected item
        LookupItem selecteditem = findItemByName(lookupItems, "CILI AKAR HIJAU");

        //Objects for each app

        if (selecteditem != null) {
            Scanner scanner = new Scanner(System.in);
            boolean exit = false;

        while (!exit) {
            System.out.println("Selected " + selecteditem.getItem());
            System.out.println("Select actions:");
            System.out.println("1. View item details");
            System.out.println("2. Modify item details");
            System.out.println("3. View top 5 cheapest seller");
            System.out.println("4. View price trend");
            System.out.println("5. Add to shopping cart");
            System.out.println("6. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    viewItemDetails(selecteditem);
                    break;
                case 2:
                    modifyItemDetails(selecteditem);
                    break;
                case 3:
                    Cheapest(selecteditem);
                    break;
                case 4:
                    AveragePrices averagePrices = new AveragePrices();
                    averagePrices.calculateAveragePrices(lookupItems, priceCatchers);
                    break;
                case 5:
                    ShoppingCart shoppingCart = new ShoppingCart();
                    shoppingCart.addToCart(selecteditem.getItem());
                    shoppingCart.displayInnerSCMenu();
                    
                    int choiceSCMenu = scanner.nextInt();

                    while(choiceSCMenu!=1 && choiceSCMenu!=2){
                        if (choiceSCMenu==1){

                        } else if (choiceSCMenu==2) {

                        } else {
                            System.out.println("Invalid choice");
                            shoppingCart.displayInnerSCMenu();
                        }
                    }     
                    break;
                case 6:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
        scanner.close();
        } else {
            System.out.println("Selected item not found.");
        }

        
    }

    private static void viewItemDetails(LookupItem item) {
        System.out.println("Item Details:");
        System.out.println("Item Code: " + item.getItemCode());
        System.out.println("Item: " + item.getItem());
        System.out.println("Unit: " + item.getUnit());
        System.out.println("Item Group: " + item.getItemGroup());
        System.out.println("Item Category: " + item.getItemCategory());
    }

    private static void modifyItemDetails(LookupItem item) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the new details:");

        // Get the new details from the user
        System.out.print("New Item: ");
        String newItem = scanner.nextLine();
        System.out.print("New Unit: ");
        String newUnit = scanner.nextLine();
        System.out.print("New Item Group: ");
        String newItemGroup = scanner.nextLine();
        System.out.print("New Item Category: ");
        String newItemCategory = scanner.nextLine();

        // Update the item details
        item.setItem(newItem);
        item.setUnit(newUnit);
        item.setItemGroup(newItemGroup);
        item.setItemCategory(newItemCategory);

        System.out.println("Item details modified successfully.");
    }

    public static LookupItem findItemByName(List<LookupItem> lookupItems, String itemName) {
        for (LookupItem item : lookupItems) {
            if (item.getItem().equals(itemName)) {
                return item;
            }
        }
        return null;
    }

    //The part used to find the chepaest sellerwf
    public static void Cheapest(LookupItem lookupItem) {
        String itemsFilePath = "resources/lookup_item_clean.csv";
        String premiseFilePath="resources/lookup_premise_clean.csv";
        String pricesFilePath="resources/pricecatcher_2023-08.csv";
     
        String itemName = lookupItem.getItem();
       
        findCheapestPrices(itemName,itemsFilePath,pricesFilePath,premiseFilePath);
    }

    public static List<String[]> findCheapestPrices(String itemName, String itemsFilePath, String pricesFilePath, String premiseFilePath) {
        List<String[]> cheapestPrices = new ArrayList<>();
    
        try (BufferedReader brItems = new BufferedReader(new FileReader(itemsFilePath));
             BufferedReader brPremise = new BufferedReader(new FileReader(premiseFilePath));
             BufferedReader brPrices = new BufferedReader(new FileReader(pricesFilePath))) {
    
                    String line;
                    List<String[]> itemCodeMap = new ArrayList<>();
                    while ((line = brItems.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length >= 4) {
                            itemCodeMap.add(parts);
        
                            // double premiseCode = Double.parseDouble(parts[1].trim());
                            // parts[1] = String.valueOf(premiseCode); // Convert back to string if needed
                            // itemCodeMap.add(parts);
                        }
                    }
        
                    List<String[]> premiseDetailsList = new ArrayList<>();
                    while ((line = brPremise.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length >= 6) {
                            premiseDetailsList.add(parts);
                        }
                    }
                    List<String[]> priceList = new ArrayList<>();
                    while ((line = brPrices.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length >= 4) {
                            priceList.add(parts);
                        }
                    }
                    for (String[] price : priceList) {
                        String itemCode = price[2].trim();
                        for (String[] item : itemCodeMap) {
                            if (itemName.equals(item[1].trim()) && itemCode.equals(item[0].trim())) {
                                if (!containsPrice(cheapestPrices, price[3])) {
                                    cheapestPrices.add(price);
                                }
                            }
                        }
                    }
                    if (cheapestPrices.isEmpty()) {
                        System.out.println("No prices found for the item.");
                    } else {
                        // Retrieve itemunit based on the first item found in cheapestPrices
                        String firstItemCode = cheapestPrices.get(0)[2];
                        String itemUnit = getItemUnit(firstItemCode, itemCodeMap);
                    }
                return cheapestPrices;
        
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }
        
            // In case of an exception, return an empty list
            return Collections.emptyList();
        }
    
    


    private static boolean containsPrice(List<String[]> cheapestPrices, String priceToCheck) {
        for (String[] price : cheapestPrices) {
            if (price[3].equals(priceToCheck)) {
                return true;
            }
        }
        return false;
    }

    private static String findPremiseAddress(String premiseCode, List<String[]> premiseDetailsList) {
        for (String[] details : premiseDetailsList) {
            if (premiseCode.equals(details[0])) {
                String[] parsedDetails = customSplit(details[1] + ", " + details[2]);
                return parsedDetails[0] + ", " + parsedDetails[1];
            }
        }
        return "Address not found";
    }

    private static String getItemUnit(String itemCode, List<String[]> itemCodeMap) {
        for (String[] item : itemCodeMap) {
            if (item[0].trim().equals(itemCode.trim())) {
                return item[2];
            }
        }
        return "Unit not found";
    }

    void viewCheapestSellerForItem(String itemName, int quantity) {
        String itemsFilePath = "resources/lookup_item_clean.csv";
        String premiseFilePath = "resources/lookup_premise_clean.csv";
        String pricesFilePath = "resources/pricecatcher_2023-08.csv";
    
        List<String[]> cheapestPrices = findCheapestPrices(itemName, itemsFilePath, pricesFilePath, premiseFilePath);
    
        List<String[]> itemCodeMap;
    
        if (cheapestPrices.isEmpty()) {
            System.out.println("No prices found for the item.");
        } else {
            String firstItemCode = cheapestPrices.get(0)[2];
            String itemUnit = getItemUnit(firstItemCode, itemCodeMap);
    
            int count = 0;
            char k = 'A';
    
            for (String[] price : cheapestPrices) {
                if (count >= 1) {
                    break;
                }
    
                String premiseCode = price[1].endsWith(".0") ? price[1] : price[1] + ".0";

                String premiseAddress = findPremiseAddress(premiseCode, premiseDetailsList);
    
                System.out.println(
                        "Retailer " + k + "\n" +
                                "   Premise Code: " + premiseCode + "\n" +
                                "   Price: RM" + price[3] + "\n" +
                                "   Address: " + premiseAddress
                );
    
                k++;
                count++;
            }
        }
    }
    
    public void viewCheapestSellers() {
        ShoppingCart cart = new ShoppingCart();

        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();

            System.out.println("Cheapest Seller for " + itemName + " (Quantity: " + quantity + "):");
            viewCheapestSellerForItem(itemName, quantity);
            System.out.println();  // Add a separator between items for better readability
        }

        System.out.println("Cheapest Seller for the entire cart:");
        cart.viewCheapestSellerForCart();
    }

    private static String[] customSplit(String line) {
        List<String> result = new ArrayList<>();
        boolean insideQuotes = false;
        StringBuilder currentPart = new StringBuilder();

        for (char c : line.toCharArray()) {
            if (c == ',' && !insideQuotes) {
                result.add(currentPart.toString().trim());
                currentPart.setLength(0); // clear the current part
            } else if (c == '"') {
                insideQuotes = !insideQuotes;
            } else {
                currentPart.append(c);
            }
        }

        // add the last part
        result.add(currentPart.toString().trim());

        return result.toArray(new String[0]);
    }
    
}

