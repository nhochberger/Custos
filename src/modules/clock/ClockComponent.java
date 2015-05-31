package modules.clock;

import hochberger.utilities.eventbus.EventReceiver;
import hochberger.utilities.text.CommonDateTimeFormatters;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import edt.EDT;

public class ClockComponent extends JPanel implements EventReceiver<DateTimeEvent> {

	private static final long serialVersionUID = -7826398186177345453L;
	private DateTime time;
	private final Logger logger;
	private final DateTimeFormatter formatter;

	public ClockComponent(final Logger logger) {
		super(true);
		this.logger = logger;
		this.time = DateTime.now();
		this.formatter = CommonDateTimeFormatters.hourMinuteSecond();
	}

	@Override
	protected void paintComponent(final Graphics g) {
		Graphics2D graphics = (Graphics2D) g.create();
		graphics.drawString(this.formatter.print(this.time), 10, 10);
		graphics.dispose();
		super.paintComponent(g);
	}

	@Override
	public void receive(final DateTimeEvent event) {
		this.time = event.getTime();
		this.logger.info("Received DateTimeEvent. Repainting.");
		EDT.perform(new Runnable() {

			@Override
			public void run() {
				repaint();
			}
		});
	}
}
