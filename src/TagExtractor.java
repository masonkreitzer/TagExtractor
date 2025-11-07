import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class TagExtractor extends JFrame {

    private final JLabel textFileLabel;
    private final JLabel stopFileLabel;
    private File textFile;
    private File stopFile;
    private Set<String> stopWords;

    public TagExtractor() {
        setTitle("Tag Extractor");

        setLayout(new GridLayout(2, 2));

        JButton chooseTextButton = new JButton("Choose Text File");
        JButton chooseStopButton = new JButton("Choose Stop Word File");

        textFileLabel = new JLabel("No text file selected");
        stopFileLabel = new JLabel("No stop word file selected");

        add(chooseTextButton);
        add(textFileLabel);
        add(chooseStopButton);
        add(stopFileLabel);

        JFileChooser fileChooser = new JFileChooser();

        chooseTextButton.addActionListener(e -> {
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                textFile = fileChooser.getSelectedFile();
                textFileLabel.setText(textFile.getName());
            }
        });

        chooseStopButton.addActionListener(e -> {
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                stopFile = fileChooser.getSelectedFile();
                stopFileLabel.setText(stopFile.getName());
                try {
                    stopWords = loadStopWords(stopFile);
                    JOptionPane.showMessageDialog(this,
                            "Loaded " + stopWords.size() + " stop words.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error reading stop word file: " + ex.getMessage());
                }
            }
        });

        pack();
        setVisible(true);
    }

    private Set<String> loadStopWords(File file) throws IOException {
        Set<String> words = new HashSet<>();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim().toLowerCase();
                if (!line.isEmpty()) {
                    words.add(line);
                }
            }
        }
        return words;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TagExtractor::new);
    }
}