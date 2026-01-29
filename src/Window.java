import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

public class Window extends JFrame {
    //initializng all components
    private final JButton LoadButton = new JButton("Load WAV…");
    private final JLabel FileLabel = new JLabel("No file loaded");
    private final JComboBox<String> cmbEffect = new JComboBox<>(new String[]{
            "VolumeChanger", "PitchEffect", "ReverseEffect", "BitCrusher", "Tremolo"
    });
    private final JLabel ParamLabel = new JLabel("Param:");
    private final JSpinner ParamSpinner = new JSpinner();
    private final JCheckBox ChainButton = new JCheckBox("Chain effects (accumulate)", true);
    private final JButton SaveButton = new JButton("Apply & Save…");
    private final JLabel StatusLabel = new JLabel(" ");

    //settings unselected things to null;
    private File loadedFile = null;
    //copy kept in memory with effects
    private AudioClip loadedClip = null;
    //original copy of loaded clip for non chained effects
    private AudioClip originalClip = null;

    public Window() {
        //setting the title, size, layout, and adding all components using GridBagLayout for organization
        super("WAV Effects (Simple)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(520, 200);
        setLocationByPlatform(true);
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 8, 6, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        //row 1: load button and file label
        c.gridx = 0; c.gridy = 0; c.weightx = 0;
        add(LoadButton, c);
        c.gridx = 1; c.gridy = 0; c.weightx = 1;
        add(FileLabel, c);

        //row 2: effect selection, param, and chain checkbox
        c.gridx = 0; c.gridy = 1; c.weightx = 0;
        add(cmbEffect, c);
        c.gridx = 1; c.gridy = 1; c.weightx = 1;
        JPanel paramPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        paramPanel.add(ParamLabel);
        ParamSpinner.setPreferredSize(new Dimension(100, ParamSpinner.getPreferredSize().height));
        paramPanel.add(ParamSpinner);
        paramPanel.add(ChainButton);
        add(paramPanel, c);

        //row 3: apply/save button and status label
        c.gridx = 0; c.gridy = 2; c.weightx = 0;
        add(SaveButton, c);
        c.gridx = 1; c.gridy = 2; c.weightx = 1;
        StatusLabel.setForeground(new Color(80, 80, 80));
        add(StatusLabel, c);

        //default param UI
        UpdateParams();

        //event handlers for buttons and combo box
        //using :: instead of lambda for simplicity
        //:: passes the variable into the method without having to use e ->
        LoadButton.addActionListener(this::LoadFile);
        cmbEffect.addActionListener(e -> UpdateParams());
        SaveButton.addActionListener(this::Save);
    }

    //handles the loading of a file
    private void LoadFile(ActionEvent e) {
        //create a new file chooser and force it to only accept .wav files
        JFileChooser FileChooser = new JFileChooser();
        FileChooser.setFileFilter(new FileNameExtensionFilter("WAV audio (*.wav)", "wav", "WAV"));
        //set initial directory if we have one
        if (loadedFile != null) FileChooser.setCurrentDirectory(loadedFile.getParentFile());
        //show open dialog and if approved, try to load the file
        if (FileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                //try to read the audio file using the FileManager
                loadedFile = FileChooser.getSelectedFile();
                loadedClip = FileManager.readAudio(loadedFile.getAbsolutePath());
                if (loadedClip == null) throw new IllegalStateException("Failed to read WAV.");
                //throw an error if the header is missing or too short (wav files can be complex so it has to be a simple one)
                if (loadedClip.getHeader() == null || loadedClip.getHeader().length < 44) {
                    throw new IllegalArgumentException("Header is missing or too short (≥44 bytes required).");
                }
                //keep a copy of the original clip in case the user does not want to chain effects
                originalClip = Copy(loadedClip);
                //update UI labels
                FileLabel.setText("Loaded: " + loadedFile.getName());
                StatusLabel.setText("Ready");
            } catch (Exception er) {
                //if any error occurs, reset everything and show error message
                loadedFile = null;
                loadedClip = null;
                originalClip = null;
                FileLabel.setText("Load failed");
                StatusLabel.setText(er.getMessage());
                JOptionPane.showMessageDialog(this, "Error loading WAV:\n" + er.getMessage(),
                        "Load Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //handles saving the processed file
    private void Save(ActionEvent e) {
        if (loadedClip == null) {
            JOptionPane.showMessageDialog(this, "Load a WAV first.", "No File", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        //choose destination folder
        JFileChooser FileChooser = new JFileChooser();
        FileChooser.setDialogTitle("Choose destination folder");
        FileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        FileChooser.setAcceptAllFileFilterUsed(false);
        if (loadedFile != null) FileChooser.setCurrentDirectory(loadedFile.getParentFile());
        if (FileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File folder = FileChooser.getSelectedFile();

        try {
            AudioEffect effect = CreateEffect();

            if (ChainButton.isSelected()) {
                //if the chain option is selected, apply effect to current loadedClip and update it
                loadedClip.applyEffect(effect);
            } else {
                //if the chain option is NOT selected, always start from originalClip
                if (originalClip == null) {
                    throw new IllegalStateException("No original clip cached. Load a WAV first.");
                }
                loadedClip = effect.applyEffect(Copy(originalClip)); // effect returns a new clip with your suffix in fileName
            }

            //this is the destination of the file to save
            File dest = new File(folder, loadedClip.getFileName());

            //create the file using the path, and the clip the user has loaded and applied effects to
            FileManager.CreateFile(loadedClip, dest.getAbsolutePath());

            //create a window popup to show success, and update the status as well
            StatusLabel.setText("Saved: " + dest.getAbsolutePath().toLowerCase() + ".wav");
            JOptionPane.showMessageDialog(this, "Saved:\n" + dest.getAbsolutePath() + ".wav",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

        } //in the case of any error, show a popup and update status
        catch (Exception ex) {
            StatusLabel.setText("Failed");
            JOptionPane.showMessageDialog(this, "Error:\n" + ex.getMessage(),
                    "Processing Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //updates the parameter label and spinner based on the selected effect
    private void UpdateParams() {
        String sel = String.valueOf(cmbEffect.getSelectedItem());
        switch (sel) {
            case "VolumeChanger" -> {
                ParamLabel.setText("Multiplier:");
                ParamSpinner.setModel(new SpinnerNumberModel(1.0, 0.0, 16.0, 0.1));
                ParamSpinner.setEnabled(true);
            }
            case "PitchEffect" -> {
                ParamLabel.setText("Factor:");
                ParamSpinner.setModel(new SpinnerNumberModel(1.0, 0.1, 4.0, 0.1));
                ParamSpinner.setEnabled(true);
            }
            case "BitCrusher" -> {
                ParamLabel.setText("Keep bits (1-15):");
                ParamSpinner.setModel(new SpinnerNumberModel(8, 1, 15, 1));
                ParamSpinner.setEnabled(true);
            }
            case "Tremolo" -> {
                ParamLabel.setText("Rate (Hz):");
                ParamSpinner.setModel(new SpinnerNumberModel(5.0, 0.1, 20.0, 0.1));
                ParamSpinner.setEnabled(true);
            }
            default -> {
                ParamLabel.setText("(no parameters)");
                ParamSpinner.setModel(new SpinnerNumberModel(0, 0, 0, 1));
                ParamSpinner.setEnabled(false);
            }
        }
    }
    //creates an AudioEffect based on the selected effect and parameter
    private AudioEffect CreateEffect() {
        String sel = String.valueOf(cmbEffect.getSelectedItem());
        return switch (sel) {
            case "VolumeChanger" -> new VolumeChanger(((double) ParamSpinner.getValue()));
            case "PitchEffect" -> new PitchEffect(((double) ParamSpinner.getValue()));
            case "BitCrusher" -> new BitCrusher(((int) ParamSpinner.getValue()));
            case "Tremolo" -> new Tremolo(((double) ParamSpinner.getValue()));
            default -> new ReverseEffect();
        };
    }

    //creates a copy of an AudioClip (to avoid modifying the original)
    private static AudioClip Copy(AudioClip src) {
        return new AudioClip(src.getFileName(), new ArrayList<>(src.getSamples()), src.getHeader().clone());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
            } catch (Exception ignored) {}
            new Window().setVisible(true);
        });
    }
}
