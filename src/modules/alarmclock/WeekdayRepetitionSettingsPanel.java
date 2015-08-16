package modules.alarmclock;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import view.ColorProvider;

public class WeekdayRepetitionSettingsPanel {

	private JPanel panel;
	private boolean isBuilt;
	private final ColorProvider colorProvider;
	private final Alarm alarm;
	private final Set<Weekday> weekdays;

	public WeekdayRepetitionSettingsPanel(final ColorProvider colorProvider, final Alarm alarm) {
		super();
		this.colorProvider = colorProvider;
		this.alarm = alarm;
		this.isBuilt = false;
		this.weekdays = new TreeSet<>(new Weekday.WeekdayComparator());
		this.weekdays.addAll(alarm.getWeekdayRepetition());
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
					if (box.isSelected()) {
						WeekdayRepetitionSettingsPanel.this.weekdays.add(day);
					} else {
						WeekdayRepetitionSettingsPanel.this.weekdays.remove(day);
					}
				}
			});
			this.panel.add(box);
		}
	}

	public Set<Weekday> getWeekdays() {
		return this.weekdays;
	}

	public JPanel getPanel() {
		return this.panel;
	}
}
