import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                StringBuilder categoriesText = new StringBuilder();
                for (String category : itemCategories) {
                    categoriesText.append(category).append("\n");
                }
                itemCategoryTextArea.setText(categoriesText.toString());
            }
        }
    }

    public static void ImportData(String fileName, Map<String, List<String>> categories) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String itemGroup = parts[3].trim();
                String itemCategory = parts[4].trim();
                categories.computeIfAbsent(itemGroup, k -> new ArrayList<>()).add(itemCategory);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
