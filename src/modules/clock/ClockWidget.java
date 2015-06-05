package modules.clock;

import hochberger.utilities.eventbus.EventReceiver;
import hochberger.utilities.gui.EnhancedLabel;
import hochberger.utilities.text.CommonDateTimeFormatters;

import javax.swing.JComponent;
import javax.swing.JPanel;

import modules.CustosModuleWidget;
import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import view.ColorProvider;
import edt.EDT;

public class ClockWidget implements CustosModuleWidget, EventReceiver<DateTimeEvent> {

	private DateTime time;
	private final Logger logger;
	private final DateTimeFormatter timeFormatter;
	private final DateTimeFormatter dateFormatter;
	private EnhancedLabel dateLabel;
	private EnhancedLabel timeLabel;
	private JPanel panel;
	private boolean isBuilt;
	private final ColorProvider colorProvider;

	public ClockWidget(final Logger logger, final ColorProvider colorProvider) {
		super();
		this.logger = logger;
		this.colorProvider = colorProvider;
		this.time = DateTime.now();
		this.timeFormatter = CommonDateTimeFormatters.hourMinuteSecond();
		this.dateFormatter = CommonDateTimeFormatters.dayMonthYearOnlyDigits();
		this.isBuilt = false;
	}

	public void build() {
		if (this.isBuilt) {
			return;
		}
		this.isBuilt = true;
		this.panel = new JPanel(new MigLayout());
		this.panel.setBackground(this.colorProvider.backgroundColor());
		this.timeLabel = new EnhancedLabel(this.timeFormatter.print(this.time));
		this.timeLabel.setFont(this.timeLabel.getFont().deriveFont(75f));
		this.timeLabel.setForeground(this.colorProvider.foregroundColor());
		this.timeLabel.setRightShadow(2, 2, this.colorProvider.shadowColor());
		this.dateLabel = new EnhancedLabel(this.dateFormatter.print(this.time), 2);
		this.dateLabel.setFont(this.timeLabel.getFont().deriveFont(25f));
		this.dateLabel.setForeground(this.colorProvider.foregroundColor());
		this.dateLabel.setRightShadow(1, 1, this.colorProvider.shadowColor());
		this.panel.add(this.dateLabel, "center, wrap");
		this.panel.add(this.timeLabel);
	}

	@Override
	public void receive(final DateTimeEvent event) {
		this.time = event.getTime();
		this.logger.debug("Received DateTimeEvent. Repainting.");
		EDT.perform(new Runnable() {

			@Override
			public void run() {
				ClockWidget.this.timeLabel.setText(ClockWidget.this.timeFormatter.print(ClockWidget.this.time));
			}
		});
	}

	@Override
	public void updateWidget() {
		this.panel.setBackground(this.colorProvider.backgroundColor());
		this.timeLabel.setBackground(this.colorProvider.backgroundColor());
		this.timeLabel.setForeground(this.colorProvider.foregroundColor());
		this.timeLabel.setRightShadow(2, 2, this.colorProvider.shadowColor());
		this.dateLabel.setBackground(this.colorProvider.backgroundColor());
		this.dateLabel.setForeground(this.colorProvider.foregroundColor());
		this.dateLabel.setRightShadow(1, 1, this.colorProvider.shadowColor());
	}

	@Override
	public JComponent getComponent() {
		return this.panel;
	}
}
