import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;



public class ShoppingCartGUI extends JFrame {

    private JTextArea cartTextArea;
    private ShoppingCart shoppingCart;

    public ShoppingCartGUI(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Shopping Cart Viewer");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        cartTextArea = new JTextArea();
        cartTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(cartTextArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JButton viewCartButton = new JButton("View Shopping Cart");
        viewCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewShoppingCart();
            }
        });

        JButton viewCheapestStoreButton = new JButton("View Cheapest Store");
        viewCheapestStoreButton.addActionListener(e -> viewCheapestStore());
        mainPanel.add(viewCheapestStoreButton, BorderLayout.SOUTH);

        mainPanel.add(viewCartButton, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void viewShoppingCart() {
        // Assuming shoppingCart is a class field in ShoppingCartGUI
        Map<String, Integer> cartItems = shoppingCart.getShoppingCart();

        if (cartItems.isEmpty()) {
            cartTextArea.setText("Shopping cart is empty.");
        } else {
            StringBuilder cartContent = new StringBuilder("Shopping Cart:\n");
            for (Map.Entry<String, Integer> entry : cartItems.entrySet()) {
                cartContent.append(entry.getKey()).append(" - Quantity: ").append(entry.getValue()).append("\n");
            }
            cartTextArea.setText(cartContent.toString());
        }
    }

    private void viewCheapestStore() {
        Map<String, Integer> cartItems = shoppingCart.getShoppingCart();

        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Shopping cart is empty.", "Empty Cart", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // You need to implement the logic for finding the cheapest store
            // using the specified CSV files (lookup_item_clean.csv, lookup_premise_clean.csv, pricecatcher_2023-08.csv)

            // Example: Call a method to find the cheapest store
            String cheapestStoreInfo = findCheapestStore(cartItems);
            JOptionPane.showMessageDialog(this, cheapestStoreInfo, "Cheapest Store", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private String findCheapestStore(Map<String, Integer> cartItems) {
    CSVReader csvReader = new CSVReader();
    List<LookupItem> lookupItems = csvReader.readLookupItemCSV("lookup_item_clean.csv");
    List<LookupPremise> lookupPremises = csvReader.readLookupPremiseCSV("lookup_premise_clean.csv");
    List<PriceCatcher> priceCatchers = csvReader.readPriceCatcherCSV("pricecatcher_2023-08.csv");

    // Calculate the total cost for each premise based on the items in the cart
    Map<String, Double> premiseTotalCosts = new HashMap<>();

    for (Map.Entry<String, Integer> entry : cartItems.entrySet()) {
        String itemName = entry.getKey();
        int quantity = entry.getValue();

        // Find the corresponding item details
        LookupItem item = findItemByName(lookupItems, itemName);

        if (item != null) {
            // Find the cheapest price for the item
            List<PriceCatcher> cheapestPrices = findCheapestPrices(priceCatchers, item.getItemCode());

            if (!cheapestPrices.isEmpty()) {
                // Assuming the cheapest seller is the first one in the list
                String cheapestPremiseId = cheapestPrices.get(0).getPremiseCode();
                double itemCost = cheapestPrices.get(0).getPrice() * quantity;

                // Update the total cost for the premise
                premiseTotalCosts.merge(cheapestPremiseId, itemCost, Double::sum);
            }
        }
    }

    // Find the premise with the minimum total cost
    String cheapestPremiseId = premiseTotalCosts.entrySet()
            .stream()
            .min(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);

    if (cheapestPremiseId != null) {
        // Find premise details
        LookupPremise cheapestPremise = findPremiseById(lookupPremises, cheapestPremiseId);

        // Format and return the cheapest store information
        return "Cheapest store: " + cheapestPremise.getPremise() +
                "\nAddress: " + cheapestPremise.getAddress() +
                "\nTotal Cost: RM" + premiseTotalCosts.get(cheapestPremiseId);
    } else {
        return "No prices found for the items in the cart.";
    }
}

private LookupItem findItemByName(List<LookupItem> items, String itemName) {
    return items.stream()
            .filter(item -> item.getItem().equals(itemName))
            .findFirst()
            .orElse(null);
}

private List<PriceCatcher> findCheapestPrices(List<PriceCatcher> prices, String itemCode) {
    return prices.stream()
            .filter(price -> price.getItemCode().equals(itemCode))
            .sorted(Comparator.comparingDouble(price -> Double.valueOf(price.getPrice())))
            .collect(Collectors.toList());
}


private LookupPremise findPremiseById(List<LookupPremise> premises, String premiseId) {
    return premises.stream()
            .filter(premise -> premise.getPremiseCode().equals(premiseId))
            .findFirst()
            .orElse(null);
}

}
