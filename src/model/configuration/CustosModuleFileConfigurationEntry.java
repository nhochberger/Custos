package model.configuration;

import hochberger.utilities.text.Text;
import hochberger.utilities.text.i18n.I18N;

import java.io.File;

import model.configuration.input.ConfigurationInput;
import model.configuration.input.FileConfigurationInput;

public class CustosModuleFileConfigurationEntry extends CustosModuleConfigurationEntry<File> {

    private static final String NULL_STRING = "null";

    public CustosModuleFileConfigurationEntry(final I18N title, final I18N description, final String key, final File value) {
        super(title, description, key, value);
    }

    @Override
    public void setValue(final String value) {
        // necessary because of issues with serialization
        if (null == value || Text.empty().equals(value) || NULL_STRING.equals(value)) {
            this.value = null;
        }
        this.value = new File(value);
    }

    @Override
    public ConfigurationInput<File> getInput() {
        return new FileConfigurationInput(this.value, getDescription());
    }

}
