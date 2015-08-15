package modules.alarmclock;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import view.ColorProvider;

public class WeekdayRepetitionSettingsPanel {

	private JPanel panel;
	private boolean isBuilt;
	private final ColorProvider colorProvider;
	private final Alarm alarm;

	public WeekdayRepetitionSettingsPanel(final ColorProvider colorProvider, final Alarm alarm) {
		super();
		this.colorProvider = colorProvider;
		this.alarm = alarm;
		this.isBuilt = false;
	}

	public void build() {
		if (this.isBuilt) {
			return;
		}
		this.isBuilt = true;
		this.panel = new JPanel(new MigLayout("", ":push[]3[]3[]3[]3[]3[]3[]:push", ""));
		this.panel.setOpaque(false);
		for (final Weekday day : Weekday.values()) {
			final JCheckBox box = new JCheckBox(day.getShortName());
			box.setOpaque(false);
			box.setSelected(this.alarm.getRepetitionFor(day));
			box.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					WeekdayRepetitionSettingsPanel.this.alarm.setRepetitionForWeekday(day, box.isSelected());
				}
			});
			this.panel.add(box);
		}
	}

	public JPanel getPanel() {
		return this.panel;
	}
}
