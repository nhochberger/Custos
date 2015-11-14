package model.configuration.input;

import hochberger.utilities.text.i18n.I18N;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JTextField;

public class StringConfigurationInput extends ConfigurationInput<String> {

	public StringConfigurationInput(final String value, final I18N description) {
		super(value, description);
	}

	@Override
	public JComponent getPreferredInputComponent() {
		final JTextField result = new JTextField(value());
		result.setToolTipText(description().toString());
		result.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final KeyEvent e) {
				setValue(result.getText());
				super.keyReleased(e);
			}
		});
		return result;
	}
}
