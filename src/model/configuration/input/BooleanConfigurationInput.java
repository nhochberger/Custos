package model.configuration.input;

import hochberger.utilities.text.i18n.I18N;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

public class BooleanConfigurationInput extends ConfigurationInput<Boolean> {

	public BooleanConfigurationInput(final Boolean value, final I18N description) {
		super(value, description);
	}

	@Override
	public JComponent getPreferredInputComponent() {
		JCheckBox result = new JCheckBox();
		result.setSelected(value().booleanValue());
		result.setToolTipText(description().toString());
		return result;
	}
}
