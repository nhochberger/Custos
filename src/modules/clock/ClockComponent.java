package modules.clock;

import hochberger.utilities.eventbus.EventReceiver;
import hochberger.utilities.gui.EnhancedLabel;
import hochberger.utilities.text.CommonDateTimeFormatters;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import view.ColorProvider;
import edt.EDT;

public class ClockComponent extends JPanel implements EventReceiver<DateTimeEvent> {

	private static final long serialVersionUID = -7826398186177345453L;
	private DateTime time;
	private final Logger logger;
	private final DateTimeFormatter formatter;
	private EnhancedLabel timeLabel;
	private final boolean isBuilt;
	private final ColorProvider colorProvider;

	public ClockComponent(final Logger logger, final ColorProvider colorProvider) {
		super(true);
		this.logger = logger;
		this.colorProvider = colorProvider;
		this.time = DateTime.now();
		this.formatter = CommonDateTimeFormatters.hourMinuteSecond();
		this.isBuilt = false;
	}

	public void build() {
		if (this.isBuilt) {
			return;
		}
		setBackground(this.colorProvider.backgroundColor());
		this.timeLabel = new EnhancedLabel(this.formatter.print(this.time));
		this.timeLabel.setFont(this.timeLabel.getFont().deriveFont(75f));
		this.timeLabel.setForeground(this.colorProvider.foregroundColor());
		this.timeLabel.setRightShadow(2, 2, this.colorProvider.shadowColor());
		add(this.timeLabel);
	}

	@Override
	public void receive(final DateTimeEvent event) {
		this.time = event.getTime();
		this.logger.debug("Received DateTimeEvent. Repainting.");
		EDT.perform(new Runnable() {

			@Override
			public void run() {
				ClockComponent.this.timeLabel.setText(ClockComponent.this.formatter.print(ClockComponent.this.time));
				repaint();
			}
		});
	}
}
