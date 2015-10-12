package modules;

import hochberger.utilities.text.i18n.I18N;

import java.util.LinkedList;
import java.util.List;

public class CustosModuleConfiguration {

    private final I18N title;
    private final List<CustosModuleConfigurationEntry> entries;

    public CustosModuleConfiguration(final I18N title) {
        super();
        this.title = title;
        this.entries = new LinkedList<>();
    }

    public void addConfigurationEntry(final CustosModuleConfigurationEntry entry) {
        this.entries.add(entry);
    }

    public List<CustosModuleConfigurationEntry> getConfigurationEntries() {
        return this.entries;
    }

    public I18N getTitle() {
        return this.title;
    }
}
