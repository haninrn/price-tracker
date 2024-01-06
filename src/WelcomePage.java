import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class WelcomePage implements ActionListener{

    ItemSearchGUI itemSearchGUI;

    JFrame frame = new JFrame();
    JLabel welcomeLabel = new JLabel("Hello!");
    // JTextField searchField = new JTextField(20);
    // JButton searchButton = new JButton("Search");
    JButton nextButton = new JButton("Next");

    WelcomePage(String userID) {

        welcomeLabel.setBounds(0, 0, 200, 35);
        welcomeLabel.setFont(new Font(null, Font.PLAIN, 25));
        welcomeLabel.setText("Hello " + userID);

        // searchField.setBounds(50, 50, 200, 25);
        // searchButton.setBounds(260, 50, 80, 25);
        nextButton.setBounds(50, 100, 80, 25);


        // searchButton.addActionListener(e -> {
        //     String searchText = searchField.getText();
        //     if (itemSearchGUI == null) {
        //         itemSearchGUI = new ItemSearchGUI();
        //     }
        //     itemSearchGUI.performSearch(searchText);
        //     // You might want to set the visibility of itemSearchGUI based on your requirements
        //     // itemSearchGUI.setVisible(true);
        // });

        nextButton.addActionListener(e -> {
            if (itemSearchGUI == null) {
                SwingUtilities.invokeLater(() -> new ItemSearchGUI());
            }
            itemSearchGUI.setVisible(true);
        });

        // frame.add(searchField);
        // frame.add(searchButton);
        frame.add(nextButton);

        frame.add(welcomeLabel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 420);
        frame.setLayout(null);
        frame.setVisible(true);
    }

	@Override
    public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==nextButton) {
            // frame.getContentPane().removeAll();
			SwingUtilities.invokeLater(() -> new RegisterPage());
		}
	}


}
