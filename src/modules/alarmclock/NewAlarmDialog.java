package modules.alarmclock;

import hochberger.utilities.gui.ImageButton;
import hochberger.utilities.gui.WrappedComponent;
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

import modules.alarmclock.Alarm.EmptyAlarm;
import net.miginfocom.swing.MigLayout;
import view.ColorProvider;

public class NewAlarmDialog extends WrappedComponent<JDialog> {

	private final Component parent;
	private final ColorProvider colorProvider;
	private boolean closedByCommit;
	private SelfHighlightningValidatingTextField hourTextField;
	private SelfHighlightningValidatingTextField minuteTextField;
	private final Alarm alarm;
	private WeekdayRepetitionSettingsPanel weekdayRepetitionSettingsPanel;

	public NewAlarmDialog(final Alarm alarm, final Component parent, final ColorProvider colorProvider) {
		super();
		this.parent = parent;
		this.colorProvider = colorProvider;
		this.closedByCommit = false;
		this.alarm = alarm;
	}

	public NewAlarmDialog(final Component parent, final ColorProvider colorProvider) {
		this(new EmptyAlarm(), parent, colorProvider);
	}

	@Override
	protected void buildComponent() {
		JDialog dialog = new JDialog();
		dialog.setModal(true);
		dialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		dialog.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		dialog.setUndecorated(true);
		dialog.setAlwaysOnTop(true);
		dialog.setSize(this.parent.getWidth(), 125);
		dialog.setTitle(new DirectI18N("Create Alarm").toString());
		dialog.setLocationRelativeTo(this.parent);
		JPanel panel = new JPanel(new MigLayout("", ":push[center]:push", ":push[]2[]2[]:push"));
		panel.setBackground(this.colorProvider.backgroundColor());
		dialog.setContentPane(panel);

		final Font baseFont = FontLoader.loadFromWithFallback("monofonto.ttf", new JPanel().getFont());
		Font font = baseFont.deriveFont(25f);
		JPanel timeSettingsPanel = new JPanel(new MigLayout("", ":push[]:push", ""));
		timeSettingsPanel.setOpaque(false);
		this.hourTextField = new SelfHighlightningValidatingTextField(3);
		this.hourTextField.addValidator(new MinMaxIntegerStringInputValidator(0, 23));
		this.hourTextField.setFont(font);
		this.hourTextField.setHorizontalAlignment(JTextField.CENTER);
		this.hourTextField.setText(String.format("%02d", this.alarm.getAlarmTime().getHour()));
		timeSettingsPanel.add(this.hourTextField);

		JLabel colonLabel = new JLabel(":");
		colonLabel.setFont(font);
		timeSettingsPanel.add(colonLabel);

		this.minuteTextField = new SelfHighlightningValidatingTextField(3);
		this.minuteTextField.addValidator(new MinMaxIntegerStringInputValidator(0, 59));
		this.minuteTextField.setFont(font);
		this.minuteTextField.setHorizontalAlignment(JTextField.CENTER);
		this.minuteTextField.setText(String.format("%02d", this.alarm.getAlarmTime().getMinute()));
		timeSettingsPanel.add(this.minuteTextField);
		panel.add(timeSettingsPanel, "wrap");

		this.weekdayRepetitionSettingsPanel = new WeekdayRepetitionSettingsPanel(this.colorProvider, this.alarm);
		this.weekdayRepetitionSettingsPanel.build();
		panel.add(this.weekdayRepetitionSettingsPanel.getPanel(), "wrap");

		JPanel buttonsPanel = new JPanel(new MigLayout("", ":push[]50[]:push", "0[]0"));
		ImageButton acceptButton = new ImageButton(ImageLoader.loadImage("check.png"));
		acceptButton.addActionListener(new AcceptButtonActionListener());
		ImageButton denyButton = new ImageButton(ImageLoader.loadImage("deny.png"));
		denyButton.addActionListener(new DenyButtonActionListener());
		buttonsPanel.setOpaque(false);
		buttonsPanel.add(acceptButton);
		buttonsPanel.add(denyButton);
		panel.add(buttonsPanel, "south");
		setComponent(dialog);
	}

	public void show() {
		getComponent().setVisible(true);
	}

	public void hide() {
		getComponent().setVisible(false);
	}

	public boolean wasClosedByCommit() {
		return this.closedByCommit;
	}

	public Alarm getResult() {
		this.alarm.setAlarmTime(new AlarmTime(Integer.valueOf(this.hourTextField.getText()), Integer.valueOf(this.minuteTextField.getText())));
		for (Weekday day : Weekday.values()) {
			this.alarm.setRepetitionForWeekday(day, this.weekdayRepetitionSettingsPanel.getWeekdays().contains(day));
		}
		return this.alarm;
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
