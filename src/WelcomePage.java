import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List; // Add this line
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;


import javax.swing.*;

public class WelcomePage implements ActionListener {

    JFrame frame = new JFrame();
    JLabel welcomeLabel = new JLabel("Hello!");
    JButton nextButton = new JButton("Next");
    JButton backButton = new JButton("Back"); // Add a back button

    WelcomePage(String userID) {
        welcomeLabel.setBounds(0, 0, 200, 35);
        welcomeLabel.setFont(new Font(null, Font.PLAIN, 25));
        welcomeLabel.setText("Hello " + userID);

        nextButton.setBounds(50, 100, 80, 25);
        backButton.setBounds(150, 100, 80, 25); // Set the position of the back button

        nextButton.addActionListener(this);
        backButton.addActionListener(this); // Add ActionListener to the back button

        frame.add(nextButton);
        frame.add(backButton); // Add the back button to the frame
        frame.add(welcomeLabel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 420);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nextButton) {
            frame.dispose();
            showMenuOptions();
        } else if (e.getSource() == backButton) {
            frame.dispose();
            // Implement logic to go back to the previous page (if needed)
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
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new ImportCSVGUI();
                    }
                });
                break;
            case 1:
                Map<String, List<String>> categories = new HashMap<>();
                ItemGroupGUI.ImportData("resources/lookup_item_clean.csv", categories);
                //GUI display lorh
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new ItemGroupGUI(categories);
                    }
                });
                break;
            case 2:
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new ItemSearchGUI().setVisible(true);
                    }
                });
                break;
            case 3:
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        ShoppingCart shoppingCart = new ShoppingCart();
                        new ShoppingCartGUI(shoppingCart).setVisible(true);
                    }
                });
                break;
            case 4:
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new AccountSettingsGUI().setVisible(true);
                    }
                });
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
}
