import java.util.Scanner;
import java.util.*;
import java.io.*;

public class ItemDetails{

    /**
     * @param args
     */

    public static void main(String[] args) {
        String lookupItemFilePath = "resources/lookup_item_clean.csv";
        String lookupPremiseFilePath = "resources/lookup_premise_clean.csv";
        String priceCatcherFilePath = "resources/pricecatcher_2023-08.csv";

        CSVReader csvReader = new CSVReader();
        List<LookupItem> lookupItems = csvReader.readLookupItemCSV(lookupItemFilePath);
        List<LookupPremise> lookupPremises = csvReader.readLookupPremiseCSV(lookupPremiseFilePath);
        List<PriceCatcher> priceCatchers = csvReader.readPriceCatcherCSV(priceCatcherFilePath);

        //assume selected item
        LookupItem selecteditem = findItemByName(lookupItems, "CILI AKAR HIJAU");

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
                    ShoppingCart.addToCart(selecteditem.getItem());
                    break;
                case 6:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
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

    private static LookupItem findItemByName(List<LookupItem> lookupItems, String itemName) {
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

    public static void findCheapestPrices(String itemName, String itemsFilePath, String pricesFilePath, String premiseFilePath) {
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

            cheapestPrices.sort(Comparator.comparingDouble(a -> Double.parseDouble(a[3])));

            if (cheapestPrices.isEmpty()) {
                System.out.println("No prices found for the item.");
            } else {
                // Retrieve itemunit based on the first item found in cheapestPrices
                String firstItemCode = cheapestPrices.get(0)[2];
                String itemUnit = getItemUnit(firstItemCode, itemCodeMap);

                System.out.println("Top 5 Cheapest Sellers for " + itemName + " (" + itemUnit + ")\n");
                int count = 0;
                char k = 'A';
                for (String[] price : cheapestPrices) {
                    if (count >= 5) {
                        break;
                    }
                    String premiseCode = price[1];
                    String premiseAddress = findPremiseAddress(premiseCode, premiseDetailsList);
                    System.out.println(count + 1 + ". Retailer " + k + "\n" +                          
                            "   Premise Code: " + premiseCode + "\n" +
                            "   Price: RM" + price[3] + "\n" +
                            "   Address: " + premiseAddress
                    );
                    System.out.println();
                    k++;
                    count++;
                }
            }

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
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
                return details[2] + ", " + details[3] + ", " + details[5];
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
}

