/**
 * This project is a GUI-based Tag Extractor tool built with Java Swing.
 * It allows the user to select a text file and a stop word file, then analyzes
 * the text to extract meaningful words, known as tags. The program filters out
 * common noise words from the stop word list, normalizes all remaining words
 * to lowercase, removes non-letter characters, and counts how often each word
 * appears. The extracted tags and their frequencies are displayed in a scrollable
 * text area and can be saved to an output file.
 */
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class TagExtractor extends JFrame {

    private final JLabel textFileLabel;
    private final JLabel stopFileLabel;
    private File textFile;
    private File stopFile;
    private Set<String> stopWords;
    private Map<String, Integer> wordFrequency;
    private final JTextArea outputArea;

    public TagExtractor() {
        setTitle("Tag Extractor");
        setLayout(new BorderLayout(5, 5));

        // Top panel for buttons and labels
        JPanel topPanel = new JPanel(new GridLayout(4, 2, 5, 5));

        JButton chooseTextButton = new JButton("Choose Text File");
        JButton chooseStopButton = new JButton("Choose Stop Word File");
        JButton extractButton = new JButton("Extract Tags");
        JButton saveButton = new JButton("Save Tags");

        textFileLabel = new JLabel("No text file selected");
        stopFileLabel = new JLabel("No stop word file selected");

        topPanel.add(chooseTextButton);
        topPanel.add(textFileLabel);
        topPanel.add(chooseStopButton);
        topPanel.add(stopFileLabel);
        topPanel.add(extractButton);
        topPanel.add(saveButton);

        add(topPanel, BorderLayout.NORTH);

        // Text area to display extracted tags
        outputArea = new JTextArea(20, 40);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.CENTER);

        JFileChooser fileChooser = new JFileChooser();

        // Select text file
        chooseTextButton.addActionListener(e -> {
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                textFile = fileChooser.getSelectedFile();
                textFileLabel.setText(textFile.getName());
            }
        });

        // Select stop word file
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

        // Extract tags
        extractButton.addActionListener(e -> {
            if (textFile == null) {
                JOptionPane.showMessageDialog(this, "Please select a text file first!");
                return;
            }
            if (stopWords == null) {
                JOptionPane.showMessageDialog(this, "Please select a stop word file first!");
                return;
            }
            extractTags(textFile);
            displayTags();
        });

        // Save tags to file
        saveButton.addActionListener(e -> {
            if (wordFrequency == null || wordFrequency.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No tags to save. Extract tags first.");
                return;
            }
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File saveFile = fileChooser.getSelectedFile();
                try (PrintWriter writer = new PrintWriter(saveFile)) {
                    for (Map.Entry<String, Integer> entry : wordFrequency.entrySet()) {
                        writer.println(entry.getKey() + ": " + entry.getValue());
                    }
                    JOptionPane.showMessageDialog(this, "Tags saved successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage());
                }
            }
        });

        pack();
        setVisible(true);
    }

    // Load stop words into a Set
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

    // Extract words from text file and count frequencies
    private void extractTags(File file) {
        wordFrequency = new TreeMap<>();

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                String word = scanner.next().toLowerCase().replaceAll("[^a-z]", "");
                if (word.isEmpty()) continue;
                if (stopWords.contains(word)) continue;

                wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error reading text file: " + e.getMessage());
        }

        JOptionPane.showMessageDialog(this, "Extracted " + wordFrequency.size() + " unique tags.");
    }

    // Display tags in the JTextArea
    private void displayTags() {
        outputArea.setText("");
        for (Map.Entry<String, Integer> entry : wordFrequency.entrySet()) {
            outputArea.append(entry.getKey() + ": " + entry.getValue() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TagExtractor::new);
    }
}