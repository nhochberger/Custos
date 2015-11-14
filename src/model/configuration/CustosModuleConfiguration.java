package model.configuration;

import hochberger.utilities.text.Text;
import hochberger.utilities.text.i18n.DirectI18N;
import hochberger.utilities.text.i18n.I18N;

import java.util.HashMap;
import java.util.Map;

public class CustosModuleConfiguration {

    private final I18N title;
    private final Map<String, CustosModuleStringConfigurationEntry> entries;

    public CustosModuleConfiguration(final I18N title) {
        super();
        this.title = title;
        this.entries = new HashMap<>();
    }

    public void addConfigurationEntry(final CustosModuleStringConfigurationEntry entry) {
        this.entries.put(entry.getKey(), entry);
    }

    public Map<String, CustosModuleStringConfigurationEntry> getConfigurationEntries() {
        return this.entries;
    }

    public CustosModuleStringConfigurationEntry getEntryFor(final String key) {
        return getConfigurationEntries().get(key);
    }

    public I18N getTitle() {
        return this.title;
    }

    public static class NoCustosModuleConfiguration extends CustosModuleConfiguration {

        public NoCustosModuleConfiguration() {
            super(new DirectI18N(Text.empty()));
        }
    }
}
