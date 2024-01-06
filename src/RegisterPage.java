//Register Page is an extension of Login

import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class RegisterPage extends JFrame implements ActionListener {
    // Components
    private JTextField userIDField, emailField;
    private JPasswordField passwordField;
    private JButton registerButton;

    public RegisterPage() {
        // Frame setup
        setTitle("Registration Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Components initialization
        JLabel userIDLabel = new JLabel("UserID:");
        userIDField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        registerButton = new JButton("Register");

        // Panel and layout
        JFrame frame=new JFrame();

        emailLabel.setBounds(50, 100, 75, 25);
        userIDLabel.setBounds(50, 150, 75, 25);
        passwordLabel.setBounds(50, 200, 75, 25);

        emailField.setBounds(125, 100, 200, 25);
        userIDField.setBounds(125, 150, 200, 25);
        passwordField.setBounds(125, 200, 200, 25);

        registerButton.setBounds(175, 250, 100, 25);
        registerButton.setFocusable(false);

        // Adding components to the panel
        frame.add(userIDLabel);
        frame.add(emailLabel);
        frame.add(passwordLabel);
        frame.add(userIDField);
        frame.add(emailField);
        frame.add(passwordField);
        frame.add(registerButton);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 420);
        frame.setLayout(null);
        frame.setVisible(true);

        // Action Listener for the register button           
        registerButton.addActionListener(this);
    }

    // Action performed when register button is clicked
    @Override
     public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {

            // Retrieve values from fields
            String userID = userIDField.getText();
            String email = emailField.getText();
            char[] passwordChars = passwordField.getPassword();
            String password = new String(passwordChars); // Convert char[] to String

            //Updates user_data
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("../user_data.txt", true))) {
            writer.write(userID + "," + password);
            writer.newLine();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }
}

