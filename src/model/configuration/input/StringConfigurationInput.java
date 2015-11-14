package model.configuration.input;

import hochberger.utilities.text.i18n.I18N;

import javax.swing.JComponent;
import javax.swing.JTextField;

public class StringConfigurationInput extends ConfigurationInput<String> {

	public StringConfigurationInput(final String value, final I18N description) {
		super(value, description);
	}

	@Override
	public JComponent getPreferredInputComponent() {
		JTextField result = new JTextField(value());
		result.setToolTipText(description().toString());
		return result;
	}
}
