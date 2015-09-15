package modules.clock;

import hochberger.utilities.eventbus.Event;

import org.joda.time.DateTime;

public class DateTimeEvent implements Event {

	private final DateTime time;

	@Override
	public void performEvent() {
	}

	public DateTimeEvent(final DateTime time) {
		super();
		this.time = time;
	}

	public DateTime getTime() {
		return this.time;
	}
}
