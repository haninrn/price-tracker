import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ItemManager {
    private List<String[]> itemList;
    private List<String> itemGroups;
    private Map<String, List<String>> itemCategories;

    public ItemManager(String csvFile) {
        this.itemList = readCSV(csvFile);
        this.itemGroups = getUniqueItems(3);
        this.itemCategories = new HashMap<>();
        initializeItemCategories();
    }

    private List<String[]> readCSV(String csvFile) {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                data.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private List<String> getUniqueItems(int columnIndex) {
        Set<String> items = new HashSet<>();
        for (String[] row : itemList) {
            items.add(row[columnIndex]);
        }
        return new ArrayList<>(items);
    }

    private void initializeItemCategories() {
        for (String group : itemGroups) {
            List<String> categories = new ArrayList<>();
            for (String[] row : itemList) {
                if (row[3].equals(group)) {
                    categories.add(row[4]);
                }
            }
            Set<String> uniqueCategories = new HashSet<>(categories);
            itemCategories.put(group, new ArrayList<>(uniqueCategories));
        }
    }

    public List<String> getItemGroups() {
        return itemGroups;
    }

    public Map<String, List<String>> getItemCategories() {
        return itemCategories;
    }

    public void displayItemGroups() {
        for (int i = 0; i < itemGroups.size(); i++) {
            System.out.println((i + 1) + ". " + itemGroups.get(i));
        }
    }

    public void displayItemCategories(String userInput) {
        if ("0".equals(userInput)) {
            return; // Return to main menu
        }

        if (userInput.matches("\\d+")) { // Check if input is a number
            int groupNumber = Integer.parseInt(userInput);
            if (groupNumber > 0 && groupNumber <= itemGroups.size()) {
                String selectedGroup = itemGroups.get(groupNumber - 1);
                printCategories(selectedGroup);
            } else {
                System.out.println("Invalid item group number.");
            }
        } else if (itemCategories.containsKey(userInput)) {
            printCategories(userInput);
        } else {
            System.out.println("Invalid item group.");
        }
    }

    private void printCategories(String group) {
        List<String> categories = itemCategories.get(group);
        System.out.println("Item subcategories for " + group + ":");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i));
        }
        System.out.println("0. GO BACK TO CATEGORY ");
    }
}
