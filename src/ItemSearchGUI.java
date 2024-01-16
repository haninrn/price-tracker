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
    
}
