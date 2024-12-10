import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public class RecursiveLister {
    private JFrame frame;
    private JTextArea textArea;
    private JScrollPane scrollPane;

    public RecursiveLister() {
        // Create the frame
        frame = new JFrame("Recursive File Lister");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Create a JLabel for the title
        JLabel titleLabel = new JLabel("Recursive File Lister", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        frame.add(titleLabel, BorderLayout.NORTH);

        // Create a JTextArea within a JScrollPane
        textArea = new JTextArea();
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Create buttons
        JPanel buttonPanel = new JPanel();
        JButton startButton = new JButton("Start");
        JButton quitButton = new JButton("Quit");

        buttonPanel.add(startButton);
        buttonPanel.add(quitButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners for buttons
        startButton.addActionListener(e -> startListing());
        quitButton.addActionListener(e -> System.exit(0));

        // Show the frame
        frame.setVisible(true);
    }

    private void startListing() {
        // Create a JFileChooser restricted to directories
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(frame);

        if (result == JFileChooser.APPROVE_OPTION) {
            Path selectedDirectory = fileChooser.getSelectedFile().toPath();
            textArea.setText("Listing files for directory: " + selectedDirectory + "\n\n");

            try {
                listFilesRecursive(selectedDirectory);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Error reading directory: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void listFilesRecursive(Path directory) throws IOException {
        try (Stream<Path> paths = Files.list(directory)) {
            for (Path path : (Iterable<Path>) paths::iterator) {
                textArea.append(path.toString() + "\n");
                if (Files.isDirectory(path)) {
                    listFilesRecursive(path); // Recurse into subdirectories
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RecursiveLister::new);
    }
}