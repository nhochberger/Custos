package controller;

import hochberger.utilities.eventbus.Event;

import org.joda.time.DateTime;

public class HeartbeatEvent implements Event {

	private final DateTime heartbeatTime;

	public HeartbeatEvent() {
		super();
		this.heartbeatTime = DateTime.now();
	}

	public DateTime getHeartbeatTime() {
		return this.heartbeatTime;
	}

	@Override
	public void performEvent() {

	}
}
