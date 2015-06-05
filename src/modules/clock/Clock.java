package modules.clock;

import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.threading.ThreadRunner;

import java.awt.Dimension;

import javax.swing.JComponent;

import modules.VisibleCustosModule;

import org.joda.time.DateTime;

import view.ColorProvider;
import edt.EDT;

public class Clock extends VisibleCustosModule {

	private final ClockComponent clockComponent;

	public Clock(final BasicSession session, final ColorProvider colorProvider) {
		super(session, colorProvider);
		EDT.never();
		this.clockComponent = new ClockComponent(logger(), colorProvider());
		this.clockComponent.setPreferredSize(new Dimension(200, 200));
		this.clockComponent.setSize(this.clockComponent.getPreferredSize());
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
						Thread.sleep(100);
					} catch (InterruptedException e) {
						logger().error(e);
					}
				}
			}
		}, "ClockThread");
		EDT.perform(new Runnable() {

			@Override
			public void run() {
				Clock.this.clockComponent.build();
			}
		});

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public JComponent getWidget() {
		return this.clockComponent;
	}

}
