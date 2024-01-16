import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class ImportCSVGUI extends JFrame {
    private JTextArea importedDataTextArea;

    public ImportCSVGUI() {
        setTitle("Import CSV Data");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        importedDataTextArea = new JTextArea(10, 30);
        importedDataTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(importedDataTextArea);
        mainPanel.add(scrollPane);

        JButton importButton = new JButton("Import CSV");
        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                importCSV();
            }
        });
        mainPanel.add(importButton);

        add(mainPanel);
        setVisible(true);
    }

    private void importCSV() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (FileReader fileReader = new FileReader(selectedFile);
                CSVReader csvReader = new CSVReader(fileReader)) {
                StringBuilder content = new StringBuilder();
                List<String[]> records = csvReader.readAll();
                
                for (String[] record : records) {
                    for (String cell : record) {
                        content.append(cell).append(", ");
                    }
                    content.append("\n");
                }
                
                importedDataTextArea.setText(content.toString());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error reading the CSV file.", "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}
