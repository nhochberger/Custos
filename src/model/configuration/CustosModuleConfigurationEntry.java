package model.configuration;

import hochberger.utilities.text.i18n.I18N;
import model.configuration.input.ConfigurationInput;

public abstract class CustosModuleConfigurationEntry<TYPE> {

	private final I18N title;
	private final I18N description;
	private final String key;
	private TYPE value;

	public CustosModuleConfigurationEntry(final I18N title, final I18N description, final String key, final TYPE value) {
		super();
		this.title = title;
		this.description = description;
		this.key = key;
		this.value = value;
	}

	public I18N getTitle() {
		return this.title;
	}

	public I18N getDescription() {
		return this.description;
	}

	public String getKey() {
		return this.key;
	}

	public TYPE getValue() {
		return this.value;
	}

	public void setValue(final TYPE value) {
		this.value = value;
	}

	public abstract ConfigurationInput<TYPE> getInput();
}
