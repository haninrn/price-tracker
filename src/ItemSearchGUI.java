import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ItemSearchGUI extends JFrame {
    private static Map<String, List<String>> categories = new HashMap<>();
    private JTextField searchField;
    private JTextArea resultTextArea;
    private JTextField choiceTextField;  // New text field for user choice
    private JTextArea actionsTextArea;    // Text area to display actions

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

        choiceTextField = new JTextField(5);  // Initialize the choice text field
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

        // Action listener for the choose button
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

        // Add the choice text field and choose button to the GUI
        JPanel choicePanel = new JPanel();
        choicePanel.add(new JLabel("Enter choice:"));
        choicePanel.add(choiceTextField);
        choicePanel.add(chooseButton);

        // Add the actions text area to the GUI
        JPanel actionsPanel = new JPanel();
        actionsPanel.add(new JLabel("Select actions:"));
        actionsPanel.add(new JScrollPane(actionsTextArea));

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));
        bottomPanel.add(choicePanel);
        bottomPanel.add(actionsPanel);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Implement fuzzy search logic here
    public void performSearch(String searchText) {
        Scanner scanner = new Scanner(searchText);
        ImportData("../resources/lookup_item_clean.csv");
        SearchForProduct.setSearchData(categories);

        // Redirect System.out to a ByteArrayOutputStream to capture the console output
        // This is to capture the list of actions
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        System.setOut(ps);

        // Perform the search
        SearchForProduct.searchForProduct(scanner);

        // Display the result in the JTextArea
        resultTextArea.setText(baos.toString());

        // Reset System.out to the console
        System.out.flush();
        System.setOut(old);

        // Set the list of actions in the actionsTextArea
        handleUserChoice();
    }

    // Handle the user choice from the text field
    private void handleUserChoice() {
        String userChoiceText = choiceTextField.getText();
        try {
            int userChoice = Integer.parseInt(userChoiceText);
            System.out.println("test");
            // Process the user choice as needed
            // Call the logic from SearchForProduct to handle the chosen product
            processUserChoice(userChoice);
        } catch (NumberFormatException e) {
            // Handle the case where the input is not a valid integer
            // JOptionPane.showMessageDialog(this, "Invalid choice. Please enter a number.");
        }
    }

    // Method to process the user choice
    // private void processUserChoice(int userChoice) {
    //     // Get the selected product based on userChoice
    //     List<String> foundProducts = new ArrayList<>();
    //     for (List<String> subCategories : categories.values()) {
    //         for (String subCategory : subCategories) {
    //             foundProducts.add(subCategory);
    //         }
    //     }

    //     if (userChoice >= 1 && userChoice <= foundProducts.size()) {
    //         String chosenProduct = foundProducts.get(userChoice - 1);
    //         // Display the chosen product in the actionsTextArea
    //         actionsTextArea.setText("Selected " + chosenProduct + ".\n\nSelect actions:\n" +
    //                 "1. View item details\n" +
    //                 "2. Modify item details\n" +
    //                 "3. View top 5 cheapest seller\n" +
    //                 "4. View price trend\n" +
    //                 "5. Add to shopping cart");
    //     } else {
    //         // Display an error message if the user choice is invalid
    //         actionsTextArea.setText("Invalid choice. Please try again.");
    //     }
    // }
    // Method to process the user choice
private void processUserChoice(int userChoice) {
    // Get the selected product based on userChoice from the search results
    List<String> foundProducts = new ArrayList<>(resultTextArea.getLineCount());
    Scanner scanner = new Scanner(resultTextArea.getText());

    while (scanner.hasNextLine()) {
        foundProducts.add(scanner.nextLine());
    }

    if (userChoice >= 1 && userChoice <= foundProducts.size()) {
        String chosenProduct = foundProducts.get(userChoice + 1);
        // Display the chosen product in the actionsTextArea
        actionsTextArea.setText("Selected " + chosenProduct + ".\n\nSelect actions:\n" +
                "1. View item details\n" +
                "2. Modify item details\n" +
                "3. View top 5 cheapest seller\n" +
                "4. View price trend\n" +
                "5. Add to shopping cart");
    } else {
        // Display an error message if the user choice is invalid
        actionsTextArea.setText("Invalid choice. Please try again.");
    }
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
}