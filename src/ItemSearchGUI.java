import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ItemSearchGUI extends JFrame {

    private JTextField searchField;

    public ItemSearchGUI() {
        setTitle("Item Search");
        setSize(400, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JLabel searchLabel = new JLabel("Search Item:");
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText();
                performSearch(searchText); // Implement this method for fuzzy search
            }
        });

        panel.add(searchLabel);
        panel.add(searchField);
        panel.add(searchButton);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    // Implement fuzzy search logic here
    public void performSearch(String searchText) {

        if(searchText == "test"){
            System.out.println("FUNCTION WORK.");
            JFrame frame = new JFrame();
            JLabel text = new JLabel("Found!");
            text.setText("found ");
            frame.add(text);
        }
    }
}