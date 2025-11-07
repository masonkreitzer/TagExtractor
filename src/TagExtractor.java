import javax.swing.*;
import java.awt.*;
import java.io.File;

public class TagExtractor extends JFrame {

    private final JLabel textFileLabel;
    private final JLabel stopFileLabel;
    private File textFile;
    private File stopFile;

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
            }
        });

        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TagExtractor::new);
    }
}