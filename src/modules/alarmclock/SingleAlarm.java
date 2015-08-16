package modules.alarmclock;

import hochberger.utilities.gui.ImageButton;
import hochberger.utilities.gui.WrappedComponent;
import hochberger.utilities.gui.font.FontLoader;
import hochberger.utilities.images.loader.ImageLoader;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import view.ColorProvider;

public class SingleAlarm extends WrappedComponent<JPanel> {

	private final Alarm alarm;
	private final ColorProvider colorProvider;

	public SingleAlarm(final ColorProvider colorProvider, final Alarm alarm) {
		super();
		this.colorProvider = colorProvider;
		this.alarm = alarm;
	}

	@Override
	protected void buildComponent() {
		setComponent(new JPanel(new MigLayout("", "4[42!]3[145!]3[16!]3[16!]2", "0[]0")));
		component().setOpaque(false);
		final Font baseFont = FontLoader.loadFromWithFallback("monofonto.ttf", new JPanel().getFont());
		JLabel alarmTimeLabel = new JLabel(this.alarm.getAlarmTime().toString());
		alarmTimeLabel.setFont(baseFont.deriveFont(16f));
		alarmTimeLabel.setForeground(this.colorProvider.foregroundColor());
		component().add(alarmTimeLabel);
		JLabel weekdayRepetitionLabel = new JLabel();
		weekdayRepetitionLabel.setFont(baseFont.deriveFont(14f));
		StringBuilder builder = new StringBuilder();
		for (Weekday day : this.alarm.getWeekdayRepetition()) {
			builder.append(day.getShortName());
			builder.append(" ");
		}
		weekdayRepetitionLabel.setText(builder.toString());
		component().add(weekdayRepetitionLabel);
		ImageButton editButton = new ImageButton(ImageLoader.loadImage("modules/alarmclock/edit.png"));
		component().add(editButton);
		ImageButton removeButton = new ImageButton(ImageLoader.loadImage("modules/alarmclock/trash.png"));
		component().add(removeButton);
	}
}
