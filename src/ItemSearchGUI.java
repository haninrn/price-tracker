import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class ItemSearchGUI extends JFrame {
    private static Map<String, List<String>> categories = new HashMap<>();
    private JList<String> resultList;
    private DefaultListModel<String> resultListModel;
    private JTextArea actionsTextArea;
    private JTextField choiceTextField;
    private JTextArea resultTextArea;

    private JButton viewDetailsButton;
    private JButton modifyDetailsButton;
    private JButton viewCheapestSellerButton;
    private JButton viewPriceTrendButton;
    private JButton addToCartButton;



    public ItemSearchGUI() {
        setTitle("Item Search");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JLabel searchLabel = new JLabel("Search Item:");
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");

        resultListModel = new DefaultListModel<>();
        resultList = new JList<>(resultListModel);
        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane resultScrollPane = new JScrollPane(resultList);
        resultScrollPane.setPreferredSize(new Dimension(350, 200));

        actionsTextArea = new JTextArea(5, 30);
        actionsTextArea.setEditable(false);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText();
                performSearch(searchText);
            }
        });

        resultList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int index = resultList.locationToIndex(e.getPoint());
                    processUserChoice(index + 1);

                }
            }
        });

        viewDetailsButton = createHoverButton("View Details", "view_details.png", "View item details");
        modifyDetailsButton = createHoverButton("Modify Details", "modify_details.png", "Modify item details");
        viewCheapestSellerButton = createHoverButton("View Cheapest Seller", "view_cheapest_seller.png", "View top 5 cheapest seller");
        viewPriceTrendButton = createHoverButton("View Price Trend", "view_price_trend.png", "View price trend");
        addToCartButton = createHoverButton("Add to Cart", "add_to_cart.png", "Add to shopping cart");

        viewDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle View Details button click action
                handleButtonAction(1);
            }
        });

        modifyDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle Modify Details button click action
                handleButtonAction(2);
            }
        });

        viewCheapestSellerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle View Cheapest Seller button click action
                handleButtonAction(3);
            }
        });

        viewPriceTrendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle View Price Trend button click action
                handleButtonAction(4);
            }
        });

        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle Add to Cart button click action
                handleButtonAction(5);
            }
        });

        panel.add(searchLabel);
        panel.add(searchField);
        panel.add(searchButton);

        add(panel, BorderLayout.NORTH);
        add(resultScrollPane, BorderLayout.CENTER);

        JPanel actionsPanel = new JPanel();
        actionsPanel.add(new JLabel("Select actions:"));
        actionsPanel.add(new JScrollPane(actionsTextArea));

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));
        bottomPanel.add(actionsPanel);

        // Adding image buttons with hover text
        bottomPanel.add(createImageButtonPanel());

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void handleButtonAction(int choice) {
        CSVReader csvReader = new CSVReader();
        List<PriceCatcher> priceCatchers = csvReader.readPriceCatcherCSV("resources/pricecatcher_2023-08.csv");
        List<LookupItem> lookupItems = csvReader.readLookupItemCSV("resources/lookup_item_clean.csv");
        LookupItem selecteditem = new LookupItem(getName(), getName(), getTitle(), getWarningString(), getName());

        findItemByName(lookupItems, getItemSearched(selecteditem));

        if (selecteditem != null) {
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

                    int choiceSCMenu = getUserChoice(); // Replace with your method to get user input

                    while (choiceSCMenu != 1 && choiceSCMenu != 2) {
                        if (choiceSCMenu == 1) {
                            // Handle choice 1
                        } else if (choiceSCMenu == 2) {
                            // Handle choice 2
                        } else {
                            System.out.println("Invalid choice");
                            shoppingCart.displayInnerSCMenu();
                        }
                        choiceSCMenu = getUserChoice(); // Replace with your method to get user input
                    }
                    break;
                default:
                    break;
            }
        } else {
            System.out.println("Selected item not found.");
        }
    }

    // Replace this with your logic to get user input
    private int getUserChoice() {
        // Example: You can use your existing code for text input or GUI interaction
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your choice: ");
        return scanner.nextInt();
    }

    private JPanel createImageButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(createHoverButton("View Details", "view_details.png", "View item details"));
        buttonPanel.add(createHoverButton("Modify Details", "modify_details.png", "Modify item details"));
        buttonPanel.add(createHoverButton("View Cheapest Seller", "view_cheapest_seller.png", "View top 5 cheapest seller"));
        buttonPanel.add(createHoverButton("View Price Trend", "view_price_trend.png", "View price trend"));
        buttonPanel.add(createHoverButton("Add to Cart", "add_to_cart.png", "Add to shopping cart"));

        return buttonPanel;
    }

    private JButton createHoverButton(String buttonText, String iconFilename, String hoverText) {
        JButton button = new JButton(buttonText, new ImageIcon(iconFilename));
        button.setToolTipText(hoverText);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                actionsTextArea.setText(hoverText);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                actionsTextArea.setText("");
            }
        });

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle button click action
                actionsTextArea.setText("Button clicked: " + buttonText);
            }
        });

        return button;
    }

    public void performSearch(String searchText) {
        // Convert the search text to uppercase
        String searchUpper = searchText.toUpperCase();

        ImportData("resources/lookup_item_clean.csv");
        SearchForProduct.setSearchData(categories);

        // Clear previous results
        resultListModel.clear();

        List<String> filteredItems = searchItemsWithKeywords(searchUpper, categories);
        updateResultList(filteredItems);

        handleUserChoice();
    }

    private void updateResultList(List<String> filteredItems) {
        for (String item : filteredItems) {
            resultListModel.addElement(item);
        }
    }

    private List<String> searchItemsWithKeywords(String keywords, Map<String, List<String>> items) {
        List<String> filteredItems = new ArrayList<>();

        items.forEach((category, itemList) -> {
            itemList.forEach(item -> {
                // Case-insensitive check for each keyword in the item
                if (containsAllKeywords(item, keywords)) {
                    filteredItems.add(category + " - " + item);
                }
            });
        });

        return filteredItems;
    }

    private boolean containsAllKeywords(String item, String keywords) {
        String[] keywordArray = keywords.split("\\s+");
        for (String keyword : keywordArray) {
            if (!item.toUpperCase().contains(keyword.toUpperCase())) {
                return false;
            }
        }
        return true;
    }

    private void updateResultTextArea(List<String> filteredItems) {
        StringBuilder resultText = new StringBuilder();
        for (String item : filteredItems) {
            resultText.append(item).append("\n");
        }
        resultTextArea.setText(resultText.toString());
    }

    private void handleUserChoice() {
        String userChoiceText = choiceTextField.getText();
        try {
            int userChoice = Integer.parseInt(userChoiceText);
            processUserChoice(userChoice);
        } catch (NumberFormatException e) {
            actionsTextArea.setText("Invalid choice. Please enter a number.");
        }
    }

    protected void processUserChoice(int userChoice) {
        List<String> foundProducts = new ArrayList<>(resultTextArea.getLineCount());
        Scanner scanner = new Scanner(resultTextArea.getText());

        while (scanner.hasNextLine()) {
            foundProducts.add(scanner.nextLine());
        }

        if (userChoice >= 1 && userChoice <= foundProducts.size()) {
            String chosenProduct = foundProducts.get(userChoice - 1);

            actionsTextArea.setText("Selected " + chosenProduct + ".\n\nSelect action:\n" +
                    "1. View item details\n" +
                    "2. Modify item details\n" +
                    "3. View top 5 cheapest seller\n" +
                    "4. View price trend\n" +
                    "5. Add to shopping cart");

            // Set visibility of buttons based on user's choice
            setButtonsVisibility(true);
        } else {
            actionsTextArea.setText("Invalid choice. Please try again.");

            // Hide buttons if the choice is invalid
            setButtonsVisibility(false);
        }
    }

    public static void ImportData(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String itemGroup = parts[0].trim();
                String itemCategory = parts[1].trim();
                String subCategory = parts[2].trim();
                categories.computeIfAbsent(itemGroup, k -> new ArrayList<>()).add(itemCategory + " - " + subCategory);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setButtonsVisibility(boolean visible) {
        viewDetailsButton.setVisible(visible);
        modifyDetailsButton.setVisible(visible);
        viewCheapestSellerButton.setVisible(visible);
        viewPriceTrendButton.setVisible(visible);
        addToCartButton.setVisible(visible);
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
                return details[1]+ ", " + details[2] + ", " + details[3] + ", " + details[4] + ", " + details[5];
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

        List<String[]> premiseDetailsList = new ArrayList<>();
    
        List<String[]> cheapestPrices = findCheapestPrices(itemName, itemsFilePath, pricesFilePath, premiseFilePath);
    
        List<String[]> itemCodeMap = new ArrayList<>();
    
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

        for (Map.Entry<String, Integer> entry : getCartEntries()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();

            System.out.println("Cheapest Seller for " + itemName + " (Quantity: " + quantity + "):");
            viewCheapestSellerForItem(itemName, quantity);
            System.out.println();  // Add a separator between items for better readability
        }

        System.out.println("Cheapest Seller for the entire cart:");
        cart.viewCheapestSellerForCart();
    }

    public String getItemSearched(LookupItem lookupItem){
        return lookupItem.getItem();
    }

    public Set<Map.Entry<String, Integer>> getCartEntries() {
        return itemCodeMap.entrySet();
    }
    
}
