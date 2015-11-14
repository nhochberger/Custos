package model.configuration;

import hochberger.utilities.text.i18n.I18N;
import model.configuration.input.ConfigurationInput;
import model.configuration.input.StringConfigurationInput;

public class CustosModuleStringConfigurationEntry extends CustosModuleConfigurationEntry<String> {

	public CustosModuleStringConfigurationEntry(final I18N title, final I18N description, final String key, final String value) {
		super(title, description, key, value);
	}

	@Override
	public ConfigurationInput<String> getInput() {
		return new StringConfigurationInput(getValue(), getDescription());
	}
}
