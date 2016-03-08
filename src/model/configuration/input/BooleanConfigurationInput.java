package model.configuration.input;

import hochberger.utilities.text.i18n.I18N;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

public class BooleanConfigurationInput extends ConfigurationInput<Boolean> {

    public BooleanConfigurationInput(final Boolean value, final I18N description) {
        super(value, description);
    }

    @Override
    public JComponent getPreferredInputComponent() {
        final JCheckBox result = new JCheckBox();
        result.setSelected(value().booleanValue());
        result.setToolTipText(description().toString());
        result.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                setValue(result.isSelected());
            }
        });
        result.setOpaque(false);
        return result;
    }
}
