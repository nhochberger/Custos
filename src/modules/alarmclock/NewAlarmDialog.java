package modules.alarmclock;

import hochberger.utilities.gui.ImageButton;
import hochberger.utilities.gui.font.FontLoader;
import hochberger.utilities.gui.input.SelfHighlightningValidatingTextField;
import hochberger.utilities.gui.input.validator.MinMaxIntegerStringInputValidator;
import hochberger.utilities.images.loader.ImageLoader;
import hochberger.utilities.text.i18n.DirectI18N;

import java.awt.Component;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import view.ColorProvider;

public class NewAlarmDialog {

	private JDialog dialog;
	private boolean isBuilt;
	private final Component parent;
	private final ColorProvider colorProvider;
	private boolean closedByCommit;
	private SelfHighlightningValidatingTextField hourTextField;
	private SelfHighlightningValidatingTextField minuteTextField;

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
		this.dialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.dialog.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		this.dialog.setUndecorated(true);
		this.dialog.setAlwaysOnTop(true);
		this.dialog.setSize(this.parent.getWidth(), this.parent.getHeight());
		this.dialog.setTitle(new DirectI18N("Create Alarm").toString());
		this.dialog.setLocationRelativeTo(this.parent);
		JPanel panel = new JPanel(new MigLayout("", ":push[]:push", "[]"));
		panel.setBackground(this.colorProvider.backgroundColor());
		this.dialog.setContentPane(panel);

		final Font baseFont = FontLoader.loadFromWithFallback("monofonto.ttf", new JPanel().getFont());
		Font font = baseFont.deriveFont(25f);
		JPanel settingsPanel = new JPanel(new MigLayout());
		settingsPanel.setOpaque(false);
		this.hourTextField = new SelfHighlightningValidatingTextField(3);
		this.hourTextField.addValidator(new MinMaxIntegerStringInputValidator(0, 23));
		this.hourTextField.setFont(font);
		this.hourTextField.setHorizontalAlignment(JTextField.CENTER);
		this.hourTextField.setText("00");
		settingsPanel.add(this.hourTextField);

		JLabel colonLabel = new JLabel(":");
		colonLabel.setFont(font);
		settingsPanel.add(colonLabel);

		this.minuteTextField = new SelfHighlightningValidatingTextField(3);
		this.minuteTextField.addValidator(new MinMaxIntegerStringInputValidator(0, 59));
		this.minuteTextField.setFont(font);
		this.minuteTextField.setHorizontalAlignment(JTextField.CENTER);
		this.minuteTextField.setText("00");
		settingsPanel.add(this.minuteTextField);

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

	public void hide() {
		this.dialog.setVisible(false);
	}

	public boolean wasClosedByCommit() {
		return this.closedByCommit;
	}

	public Alarm getResult() {
		if (!this.isBuilt) {
			return null;
		}
		AlarmTime time = new AlarmTime(Integer.valueOf(this.hourTextField.getText()), Integer.valueOf(this.minuteTextField.getText()));
		Alarm result = new Alarm();
		result.setAlarmTime(time);
		return result;
	}

	private class DenyButtonActionListener implements ActionListener {

		public DenyButtonActionListener() {
			super();
		}

		@Override
		public void actionPerformed(final ActionEvent arg0) {
			hide();
		}
	}

	private class AcceptButtonActionListener implements ActionListener {

		public AcceptButtonActionListener() {
			super();
		}

		@Override
		public void actionPerformed(final ActionEvent arg0) {
			if (!(NewAlarmDialog.this.hourTextField.validateInput() && NewAlarmDialog.this.minuteTextField.validateInput())) {
				return;
			}
			hide();
			NewAlarmDialog.this.closedByCommit = true;
		}
	}
}
