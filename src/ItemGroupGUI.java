import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItemGroupGUI extends JFrame {
    private JComboBox<String> itemGroupComboBox;
    private JTextArea itemCategoryTextArea;

    private Map<String, List<String>> categories;

    public ItemGroupGUI(Map<String, List<String>> categories) {
        this.categories = categories;

        setTitle("Item Group and Category Viewer");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 1));

        itemGroupComboBox = new JComboBox<>(categories.keySet().toArray(new String[0]));
        itemGroupComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayItemCategories();
            }
        });

        itemCategoryTextArea = new JTextArea();
        itemCategoryTextArea.setEditable(false);

        mainPanel.add(new JLabel("Select Item Group:"));
        mainPanel.add(itemGroupComboBox);
        mainPanel.add(new JScrollPane(itemCategoryTextArea));

        add(mainPanel);
        setVisible(true);
    }

    private void displayItemCategories() {
    String selectedGroup = (String) itemGroupComboBox.getSelectedItem();
    if (selectedGroup != null) {
        List<String> itemCategories = categories.get(selectedGroup);
        if (itemCategories != null) {
            Set<String> displayedItems = new HashSet<>();
            StringBuilder categoriesText = new StringBuilder();
            
            for (String category : itemCategories) {
                // Check if the item is not already displayed
                if (!displayedItems.contains(category)) {
                    categoriesText.append(category).append("\n");
                    displayedItems.add(category);
                }
            }
            
            itemCategoryTextArea.setText(categoriesText.toString());
        }
    }
}

    public static void ImportData(String fileName, Map<String, List<String>> categories) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {

                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] parts = customSplit(line);
                String itemGroup = parts[3].trim();
                String itemCategory = parts[4].trim();
                categories.computeIfAbsent(itemGroup, k -> new ArrayList<>()).add(itemCategory);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    

