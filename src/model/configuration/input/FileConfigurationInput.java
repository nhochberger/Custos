package model.configuration.input;

import hochberger.utilities.gui.ImageButton;
import hochberger.utilities.images.loader.ImageLoader;
import hochberger.utilities.text.i18n.DirectI18N;
import hochberger.utilities.text.i18n.I18N;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;

public class FileConfigurationInput extends ConfigurationInput<File> {

    public FileConfigurationInput(final File value, final I18N description) {
        super(value, description);
    }

    @Override
    public JComponent getPreferredInputComponent() {
        final JPanel panel = new JPanel(new MigLayout("", "0[grow]5[34px!]5[34px!]", ""));
        panel.setOpaque(false);
        final JTextField textField = new JTextField();
        textField.setToolTipText(description().toString());
        // HACK
        if (null == value() || "null".equals(value().getName())) {
            textField.setText(new DirectI18N("default").toString());
        } else {
            textField.setText(value().getAbsolutePath());
        }
        panel.add(textField, "growx");
        final JFileChooser fileChooser = new JFileChooser() {
            private static final long serialVersionUID = 1L;

            @Override
            public void approveSelection() {
                super.approveSelection();
                setValue(getSelectedFile());
                textField.setText(value().getAbsolutePath());
            }
        };
        if (null != value()) {
            fileChooser.setSelectedFile(value());
        }
        fileChooser.setFileFilter(new FileNameExtensionFilter("MP3 files (*.mp3*)", "mp3"));
        final ImageButton folderButton = new ImageButton(ImageLoader.loadImage("folder.png"));
        folderButton.setToolTipText(new DirectI18N("Open file").toString());
        folderButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                fileChooser.showOpenDialog(panel);
            }
        });
        panel.add(folderButton);
        final ImageButton clearButton = new ImageButton(ImageLoader.loadImage("deny.png"));
        clearButton.setToolTipText(new DirectI18N("Clear selection").toString());
        clearButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                setValue(null);
                textField.setText(new DirectI18N("default").toString());
            }
        });
        panel.add(clearButton);
        return panel;
    }
}
