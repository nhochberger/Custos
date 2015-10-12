package modules;

import hochberger.utilities.text.i18n.I18N;

public class CustosModuleConfigurationEntry {

    private final I18N title;
    private final I18N description;
    private final String key;
    private final String value;

    public CustosModuleConfigurationEntry(final I18N title, final I18N description, final String key, final String value) {
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

    public String getValue() {
        return this.value;
    }
}
