package modules.alarmclock;

import hochberger.utilities.application.session.BasicSession;

import java.util.LinkedList;
import java.util.List;

import modules.CustosModuleWidget;
import modules.VisibleCustosModule;
import view.ColorProvider;
import controller.HeartbeatEvent;

public class AlarmClock extends VisibleCustosModule {

	private final List<Alarm> alarms;
	private final AlarmClockWidget widget;

	public AlarmClock(final BasicSession session, final ColorProvider colorProvider) {
		super(session, colorProvider);
		this.alarms = new LinkedList<>();
		this.widget = new AlarmClockWidget(colorProvider);
	}

	@Override
	public CustosModuleWidget getWidget() {
		return this.widget;
	}

	@Override
	public void updateWidget() {

	}

	@Override
	public void start() {
		this.widget.build();
	}

	@Override
	public void stop() {

	}

	@Override
	public void receive(final HeartbeatEvent event) {
		boolean alarmDue = checkDueAlarms();
		if (alarmDue) {
			triggerAlarm();
		}
	}

	private boolean checkDueAlarms() {
		boolean alarmDue = false;
		for (Alarm alarm : this.alarms) {
			if (alarm.checkTriggered()) {
				alarmDue = true;
			}
		}
		return alarmDue;
	}

	private void triggerAlarm() {
		System.err.println("alarm triggered");
	}
}
