import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ModifyDetailsPanel extends JFrame {
    private JTextField newUsernameField;
    private JPasswordField newPasswordField;
    private JButton modifyButton;

    public ModifyDetailsPanel(String currentUsername) {
        super("Modify Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel modifyPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        modifyPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel currentUsernameLabel = new JLabel("Current Username:");
        JTextField currentUsernameField = new JTextField(currentUsername);
        JLabel newUsernameLabel = new JLabel("New Username:");
        newUsernameField = new JTextField();
        JLabel newPasswordLabel = new JLabel("New Password:");
        newPasswordField = new JPasswordField();
        modifyButton = new JButton("Modify");

        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newUsername = newUsernameField.getText();
                String newPassword = new String(newPasswordField.getPassword());

                AccountSettings accountSettings = new AccountSettings();
                accountSettings.modifyUserDetails(currentUsername, "", newUsername, newPassword);

                JOptionPane.showMessageDialog(ModifyDetailsPanel.this,
                        "Details modified successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                dispose(); // Close the modification window
            }
        });

        modifyPanel.add(currentUsernameLabel);
        modifyPanel.add(currentUsernameField);
        modifyPanel.add(newUsernameLabel);
        modifyPanel.add(newUsernameField);
        modifyPanel.add(newPasswordLabel);
        modifyPanel.add(newPasswordField);
        modifyPanel.add(new JLabel()); // Empty label for spacing
        modifyPanel.add(modifyButton);

        setLayout(new BorderLayout());
        add(modifyPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null); // Center the frame on the screen
    }
}
