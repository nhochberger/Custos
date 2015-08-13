package modules.alarmclock;

import hochberger.utilities.gui.ImageButton;
import hochberger.utilities.gui.font.FontLoader;
import hochberger.utilities.gui.input.SelfHighlightningValidatingTextField;
import hochberger.utilities.gui.input.validator.IntegerStringInputValidator;
import hochberger.utilities.images.loader.ImageLoader;
import hochberger.utilities.text.i18n.DirectI18N;

import java.awt.Component;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import view.ColorProvider;

public class NewAlarmDialog {

	private JDialog dialog;
	private boolean isBuilt;
	private final Component parent;
	private final ColorProvider colorProvider;
	private boolean closedByCommit;
	private Alarm result;

	public NewAlarmDialog(final Component parent, final ColorProvider colorProvider) {
		super();
		this.parent = parent;
		this.colorProvider = colorProvider;
		this.isBuilt = false;
		this.closedByCommit = false;
	}

	public void build() {
		if (this.isBuilt) {
			return;
		}
		this.isBuilt = true;
		this.dialog = new JDialog();
		this.dialog.setModal(true);
		this.dialog.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		this.dialog.setUndecorated(true);
		this.dialog.setAlwaysOnTop(true);
		this.dialog.setSize(this.parent.getWidth(), this.parent.getHeight());
		this.dialog.setTitle(new DirectI18N("Create Alarm").toString());
		this.dialog.setLocationRelativeTo(this.parent);
		JPanel panel = new JPanel(new MigLayout("debug", ":push[]:push", "[]"));
		panel.setBackground(this.colorProvider.backgroundColor());
		this.dialog.setContentPane(panel);

		final Font baseFont = FontLoader.loadFromWithFallback("monofonto.ttf", new JPanel().getFont());
		JPanel settingsPanel = new JPanel(new MigLayout());
		SelfHighlightningValidatingTextField hourTextField = new SelfHighlightningValidatingTextField(2);
		hourTextField.addValidator(new IntegerStringInputValidator());
		settingsPanel.add(hourTextField);
		panel.add(settingsPanel);

		JPanel buttonsPanel = new JPanel(new MigLayout("", ":push[]50[]:push", "0[]0"));
		ImageButton acceptButton = new ImageButton(ImageLoader.loadImage("modules/alarmclock/check.png"));
		acceptButton.addActionListener(new AcceptButtonActionListener());
		ImageButton denyButton = new ImageButton(ImageLoader.loadImage("modules/alarmclock/deny.png"));
		denyButton.addActionListener(new DenyButtonActionListener());
		buttonsPanel.setOpaque(false);
		buttonsPanel.add(acceptButton);
		buttonsPanel.add(denyButton);
		panel.add(buttonsPanel, "south");
	}

	public void show() {
		this.dialog.setVisible(true);
	}

	public boolean wasClosedByCommit() {
		return this.closedByCommit;
	}

	public Alarm getResult() {
		return this.result;
	}

	private class DenyButtonActionListener implements ActionListener {

		public DenyButtonActionListener() {
			super();
		}

		@Override
		public void actionPerformed(final ActionEvent arg0) {
			NewAlarmDialog.this.dialog.setVisible(false);
		}
	}

	private class AcceptButtonActionListener implements ActionListener {

		public AcceptButtonActionListener() {
			super();
		}

		@Override
		public void actionPerformed(final ActionEvent arg0) {
			NewAlarmDialog.this.dialog.setVisible(false);
			NewAlarmDialog.this.closedByCommit = true;
		}
	}
}
