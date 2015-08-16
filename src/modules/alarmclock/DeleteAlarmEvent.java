package modules.alarmclock;

import hochberger.utilities.eventbus.Event;
import hochberger.utilities.exceptions.NotYetImplementedException;

public class DeleteAlarmEvent implements Event {

	private final Alarm alarm;

	public DeleteAlarmEvent(final Alarm alarm) {
		super();
		this.alarm = alarm;
	}

	public Alarm getAlarm() {
		return this.alarm;
	}

	@Override
	public void performEvent() {
		throw new NotYetImplementedException();
	}
}
