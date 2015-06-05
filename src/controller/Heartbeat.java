package controller;

import hochberger.utilities.application.Lifecycle;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;

import java.util.Timer;
import java.util.TimerTask;

public class Heartbeat extends SessionBasedObject implements Lifecycle {

	private final Timer timer;

	public Heartbeat(final BasicSession session) {
		super(session);
		this.timer = new Timer();
	}

	@Override
	public void start() {
		logger().info("Heartbeat started.");
		this.timer.scheduleAtFixedRate(new HeartbeatTimerTask(), 0, 1000);
	}

	@Override
	public void stop() {
		this.timer.cancel();
		logger().info("Heartbeat stopped.");
	}

	public class HeartbeatTimerTask extends TimerTask {

		public HeartbeatTimerTask() {
			super();
		}

		@Override
		public void run() {
			session().getEventBus().publish(new HeartbeatEvent());
		}
	}
}
