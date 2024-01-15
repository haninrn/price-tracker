import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemSearchGUI extends JFrame {
    private static Map<String, List<String>> categories = new HashMap<>();
    private JTextField searchField;
    private JTextArea resultTextArea;
    private JTextField choiceTextField;
    private JTextArea actionsTextArea;

    public ItemSearchGUI() {
        setTitle("Item Search");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JLabel searchLabel = new JLabel("Search Item:");
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");

        resultTextArea = new JTextArea(10, 30);
        resultTextArea.setEditable(false);

        choiceTextField = new JTextField(5);
        JButton chooseButton = new JButton("Choose");

        actionsTextArea = new JTextArea(5, 30);
        actionsTextArea.setEditable(false);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText();
                performSearch(searchText);
            }
        });

        chooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleUserChoice();
            }
        });

        panel.add(searchLabel);
        panel.add(searchField);
        panel.add(searchButton);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(resultTextArea), BorderLayout.CENTER);

        JPanel choicePanel = new JPanel();
        choicePanel.add(new JLabel("Enter choice:"));
        choicePanel.add(choiceTextField);
        choicePanel.add(chooseButton);

        JPanel actionsPanel = new JPanel();
        actionsPanel.add(new JLabel("Select actions:"));
        actionsPanel.add(new JScrollPane(actionsTextArea));

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));
        bottomPanel.add(choicePanel);
        bottomPanel.add(actionsPanel);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void performSearch(String searchText) {
        // Convert the search text to uppercase
        String searchUpper = searchText.toUpperCase();

        ImportData("resources/lookup_item_clean.csv");
        SearchForProduct.setSearchData(categories);

        List<String> filteredItems = searchItemsWithKeywords(searchUpper, categories);
        updateResultTextArea(filteredItems);

        handleUserChoice();
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
        } else {
            actionsTextArea.setText("Invalid choice. Please try again.");
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
}
