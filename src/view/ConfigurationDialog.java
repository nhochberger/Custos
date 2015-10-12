package view;

import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.gui.WrappedComponent;
import hochberger.utilities.text.i18n.DirectI18N;

import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import modules.CustosModuleConfigurationEntry;

public class ConfigurationDialog extends WrappedComponent<JDialog> {

    private final List<CustosModuleConfigurationEntry> configurationEntries;
    private final BasicSession session;

    public ConfigurationDialog(final List<CustosModuleConfigurationEntry> configurationEntries, final BasicSession session) {
        super();
        this.configurationEntries = configurationEntries;
        this.session = session;
    }

    @Override
    protected void buildComponent() {
        final JDialog dialog = new JDialog();
        dialog.setTitle(new DirectI18N("Custos Configuration").toString());

    }

    private JPanel settingPanel(final CustosModuleConfigurationEntry entry) {
        final JPanel result = new JPanel();

        return result;
    }
}
