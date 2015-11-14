package model.configuration.input;

import hochberger.utilities.text.i18n.I18N;

import javax.swing.JComponent;

public abstract class ConfigurationInput<TYPE> {

	private final TYPE value;
	private final I18N description;

	public ConfigurationInput(final TYPE value, final I18N description) {
		super();
		this.value = value;
		this.description = description;
	}

	public TYPE value() {
		return this.value;
	}

	public I18N description() {
		return this.description;
	}

	public abstract JComponent getPreferredInputComponent();

}
