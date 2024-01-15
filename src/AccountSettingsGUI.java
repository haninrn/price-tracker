import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountSettingsGUI extends JFrame {
    private AccountSettings accountSettings = new AccountSettings();

    private JTextField usernameField;
    private JPasswordField passwordField;

    public AccountSettingsGUI() {
        super("Account Settings");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel usernameLabel = new JLabel("Current Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Current Password:");
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentUsername = usernameField.getText();
                String currentPassword = new String(passwordField.getPassword());

                accountSettings.loadUserAccounts();

                if (accountSettings.authenticateUser(currentUsername, currentPassword)) {
                    displayModifyDetails(currentUsername);
                } else {
                    JOptionPane.showMessageDialog(AccountSettingsGUI.this,
                            "Invalid username or password. Please try again.",
                            "Invalid Credentials",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(new JLabel()); // Empty label for spacing
        loginPanel.add(loginButton);

        setLayout(new BorderLayout());
        add(loginPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null); // Center the frame on the screen
    }

    private void displayModifyDetails(String currentUsername) {
        ModifyDetailsPanel modifyDetailsPanel = new ModifyDetailsPanel(currentUsername);
        modifyDetailsPanel.setVisible(true);
    }
}