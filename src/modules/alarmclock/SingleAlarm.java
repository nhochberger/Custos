package modules.alarmclock;

import hochberger.utilities.eventbus.EventBus;
import hochberger.utilities.gui.ImageButton;
import hochberger.utilities.gui.WrappedComponent;
import hochberger.utilities.gui.font.FontLoader;
import hochberger.utilities.images.loader.ImageLoader;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import view.ColorProvider;

public class SingleAlarm extends WrappedComponent<JPanel> {

	private final class RemoveButtonActionListener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent arg0) {
			SingleAlarm.this.eventBus.publishFromEDT(new DeleteAlarmEvent(SingleAlarm.this.alarm));
		}
	}

	private final Alarm alarm;
	private final ColorProvider colorProvider;
	private final EventBus eventBus;
	private JLabel alarmTimeLabel;
	private JLabel weekdayRepetitionLabel;

	public SingleAlarm(final EventBus eventBus, final ColorProvider colorProvider, final Alarm alarm) {
		super();
		this.eventBus = eventBus;
		this.colorProvider = colorProvider;
		this.alarm = alarm;
	}

	@Override
	protected void buildComponent() {
		setComponent(new JPanel(new MigLayout("", "4[42!]3[145!]3[16!]3[16!]2", "0[]0")));
		component().setOpaque(false);
		final Font baseFont = FontLoader.loadFromWithFallback("monofonto.ttf", new JPanel().getFont());
		this.alarmTimeLabel = new JLabel(this.alarm.getAlarmTime().toString());
		this.alarmTimeLabel.setFont(baseFont.deriveFont(16f));
		this.alarmTimeLabel.setForeground(this.colorProvider.foregroundColor());
		component().add(this.alarmTimeLabel);
		this.weekdayRepetitionLabel = new JLabel();
		this.weekdayRepetitionLabel.setFont(baseFont.deriveFont(14f));
		this.weekdayRepetitionLabel.setForeground(this.colorProvider.foregroundColor());
		final StringBuilder builder = new StringBuilder();
		for (final Weekday day : this.alarm.getWeekdayRepetition()) {
			builder.append(day.getShortName());
			builder.append(" ");
		}
		this.weekdayRepetitionLabel.setText(builder.toString());
		component().add(this.weekdayRepetitionLabel);
		final ImageButton editButton = new ImageButton(ImageLoader.loadImage("modules/alarmclock/edit.png"));
		editButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent arg0) {
				final NewAlarmDialog dialog = new NewAlarmDialog(SingleAlarm.this.alarm, getComponent().getParent().getParent(), SingleAlarm.this.colorProvider);
				dialog.build();
				dialog.show();
				if (dialog.wasClosedByCommit()) {
					SingleAlarm.this.alarm.setAlarmTime(dialog.getResult().getAlarmTime());
					for (final Weekday day : Weekday.values()) {
						SingleAlarm.this.alarm.setRepetitionForWeekday(day, dialog.getResult().getWeekdayRepetition().contains(day));
						SingleAlarm.this.eventBus.publishFromEDT(new AlarmEditedEvent());
					}
				}
			}
		});
		component().add(editButton);
		final ImageButton removeButton = new ImageButton(ImageLoader.loadImage("modules/alarmclock/trash.png"));
		removeButton.addActionListener(new RemoveButtonActionListener());
		component().add(removeButton);
	}

	public void updateWidget() {
		if (!isBuilt()) {
			return;
		}
		this.alarmTimeLabel.setForeground(this.colorProvider.foregroundColor());
		this.weekdayRepetitionLabel.setForeground(this.colorProvider.foregroundColor());
	}
}
