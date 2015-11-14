package view;

import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.gui.ImageButton;
import hochberger.utilities.gui.WrappedComponent;
import hochberger.utilities.gui.font.FontLoader;
import hochberger.utilities.images.loader.ImageLoader;
import hochberger.utilities.text.i18n.DirectI18N;

import java.awt.Dialog.ModalExclusionType;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.configuration.CustosConfiguration;
import model.configuration.CustosModuleConfiguration;
import model.configuration.CustosModuleConfigurationEntry;
import model.configuration.CustosModuleStringConfigurationEntry;
import model.configuration.input.ConfigurationInput;
import net.miginfocom.swing.MigLayout;
import controller.UpdateConfigurationEvent;

public class ConfigurationDialog extends WrappedComponent<JDialog> {

	private final BasicSession session;
	private final CustosConfiguration configuration;
	private final ColorProvider colorProvider;

	public ConfigurationDialog(final CustosConfiguration configuration, final ColorProvider colorProvider, final BasicSession session) {
		super();
		this.configuration = configuration;
		this.colorProvider = colorProvider;
		this.session = session;
	}

	@Override
	protected void buildComponent() {
		final DirectI18N title = new DirectI18N("Custos Settings");
		final JDialog dialog = new JDialog();
		final Font baseFont = FontLoader.loadFromWithFallback("monofonto.ttf", new JPanel().getFont());
		final JPanel panel = new JPanel(new MigLayout("", "10[]10[350px!]10", "10[]5[]10"));
		dialog.setContentPane(panel);
		setComponent(dialog);
		dialog.setUndecorated(true);
		panel.setBorder(BorderFactory.createLineBorder(this.colorProvider.shadowColor()));
		dialog.setTitle(title.toString());
		dialog.setModal(true);
		dialog.setAlwaysOnTop(true);
		dialog.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		panel.setBackground(this.colorProvider.backgroundColor());
		final JLabel headlineLabel = new JLabel(title.toString());
		headlineLabel.setFont(baseFont.deriveFont(24f));
		panel.add(headlineLabel, "span 2, wrap");
		final Map<String, ConfigurationInput> configurationTextFields = new HashMap<>();
		for (final CustosModuleConfiguration moduleConfiguration : this.configuration.getModuleConfigurations()) {
			final JLabel sectionLabel = new JLabel(moduleConfiguration.getTitle().toString());
			sectionLabel.setFont(baseFont.deriveFont(16f));
			panel.add(sectionLabel, "span 2, wrap");
			for (final CustosModuleStringConfigurationEntry entry : moduleConfiguration.getConfigurationEntries().values()) {
				panel.add(new JLabel(entry.getTitle().toString()));
				// final JTextField textField = new
				// JTextField(entry.getValue());
				// panel.add(textField, "grow, wrap");
				ConfigurationInput configurationInput = entry.getInput();
				JComponent inputComponent = configurationInput.getPreferredInputComponent();
				configurationTextFields.put(entry.getKey(), configurationInput);
				panel.add(inputComponent, "grow, wrap");
			}
		}
		final ImageButton acceptButton = new ImageButton(ImageLoader.loadImage("check.png"));
		acceptButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				for (final CustosModuleConfiguration moduleConfiguration : ConfigurationDialog.this.configuration.getModuleConfigurations()) {
					for (final CustosModuleConfigurationEntry entry : moduleConfiguration.getConfigurationEntries().values()) {
						entry.setValue(configurationTextFields.get(entry.getKey()).value());
						ConfigurationDialog.this.configuration.setValueFor(entry.getKey(), String.valueOf(configurationTextFields.get(entry.getKey()).value()));
					}
				}
				ConfigurationDialog.this.session.getEventBus().publishFromEDT(new UpdateConfigurationEvent());
				dialog.dispose();
			}
		});
		final ImageButton denyButton = new ImageButton(ImageLoader.loadImage("deny.png"));
		denyButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent arg0) {
				dialog.dispose();
			}
		});
		final JPanel buttonsPanel = new JPanel(new MigLayout("center", "[]50[]", ""));
		buttonsPanel.setOpaque(false);
		buttonsPanel.add(acceptButton);
		buttonsPanel.add(denyButton);
		panel.add(buttonsPanel, "growx, span 2");
		dialog.pack();
		dialog.setLocationRelativeTo(null);
	}

	public void show() {
		component().setVisible(true);
	}
}
