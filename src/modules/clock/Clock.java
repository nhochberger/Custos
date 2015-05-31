package modules.clock;

import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;
import hochberger.utilities.threading.ThreadRunner;

import javax.swing.JComponent;

import modules.CustosModule;

import org.joda.time.DateTime;

import edt.EDT;

public class Clock extends SessionBasedObject implements CustosModule {

	private final ClockComponent clockComponent;

	public Clock(final BasicSession session) {
		super(session);
		EDT.never();
		this.clockComponent = new ClockComponent(logger());
		session().getEventBus().register(this.clockComponent, DateTimeEvent.class);
	}

	@Override
	public void start() {
		ThreadRunner.startThread(new Runnable() {

			@Override
			public void run() {
				int lastSecond = 0;
				while (true) {
					DateTime time = DateTime.now();
					if (time.getSecondOfMinute() != lastSecond) {
						lastSecond = time.getSecondOfMinute();
						session().getEventBus().publish(new DateTimeEvent(time));
					}
					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						logger().error(e);
					}
				}
			}
		}, "ClockThread");

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public JComponent getWidget() {
		// TODO Auto-generated method stub
		return null;
	}

}
