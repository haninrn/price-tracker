import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class WelcomePage implements ActionListener {

    ItemSearchGUI itemSearchGUI;

    JFrame frame = new JFrame();
    JLabel welcomeLabel = new JLabel("Hello!");
    JButton nextButton = new JButton("Next");

    WelcomePage(String userID) {

        welcomeLabel.setBounds(0, 0, 200, 35);
        welcomeLabel.setFont(new Font(null, Font.PLAIN, 25));
        welcomeLabel.setText("Hello " + userID);

        nextButton.setBounds(50, 100, 80, 25);

        nextButton.addActionListener(this);

        frame.add(nextButton);
        frame.add(welcomeLabel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 420);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nextButton) {
            showMenuOptions();
        }
    }

    private void showMenuOptions() {
        String[] options = { "Import Data", "Browse by Categories", "Search for a Product", "View Shopping Cart",
                "Account Settings", "Exit" };

        int choice = JOptionPane.showOptionDialog(frame, "PriceTracker - Track Prices with Ease\n\n"
                + "Welcome to Product Search and Selection\n\n" + getMenuOptions(), "Menu", JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        switch (choice) {
            case 0:
            //import data funct
                // ProductSearchApp.ImportData("../resources/lookup_item_clean.csv");
                break;
            case 1:
                // Browse by Categories
                // Add logic to handle Browse by Categories
                break;
            case 2:
                // Search for a Product
                // Add logic to handle Search for a Product
                if (itemSearchGUI == null) {
                    itemSearchGUI = new ItemSearchGUI();
                    itemSearchGUI.setVisible(true);
                }
                break;
            case 3:
                // View Shopping Cart
                // Add logic to handle View Shopping Cart
                break;
            case 4:
                // Account Settings
                // AccountSettings.displayAccountSettings(null);
                break;
            case 5:
                // Exit
                System.exit(0);
                break;
            default:
                // Do nothing
                break;
        }
    }

    private String getMenuOptions() {
        return "1. Import Data\n" + "2. Browse by Categories\n" + "3. Search for a Product\n" + "4. View Shopping Cart\n"
                + "5. Account Settings\n" + "6. Exit\n\n" + "Enter your choice (1/2/3/4/5/6) :";
    }

    // public static void main(String[] args) {
    //     SwingUtilities.invokeLater(() -> new WelcomePage("User123"));
    // }
}
