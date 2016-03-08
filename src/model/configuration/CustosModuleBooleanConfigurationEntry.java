package model.configuration;

import hochberger.utilities.text.i18n.I18N;
import model.configuration.input.BooleanConfigurationInput;
import model.configuration.input.ConfigurationInput;

public class CustosModuleBooleanConfigurationEntry extends CustosModuleConfigurationEntry<Boolean> {

    public CustosModuleBooleanConfigurationEntry(final I18N title, final I18N description, final String key, final Boolean value) {
        super(title, description, key, value);
    }

    @Override
    public ConfigurationInput<Boolean> getInput() {
        return new BooleanConfigurationInput(getValue(), getDescription());
    }

    // HACK
    @Override
    public Boolean getValue() {
        if (Boolean.TRUE.equals(this.value)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public void setValue(final String value) {
        this.value = Boolean.TRUE.equals(Boolean.valueOf(value));
    }
}
