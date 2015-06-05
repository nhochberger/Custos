package modules.clock;

import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.threading.ThreadRunner;
import modules.CustosModuleWidget;
import modules.VisibleCustosModule;

import org.joda.time.DateTime;

import view.ColorProvider;
import edt.EDT;

public class Clock extends VisibleCustosModule {

	private final ClockWidget widget;

	public Clock(final BasicSession session, final ColorProvider colorProvider) {
		super(session, colorProvider);
		EDT.never();
		this.widget = new ClockWidget(logger(), colorProvider());
		session().getEventBus().register(this.widget, DateTimeEvent.class);
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
				Clock.this.widget.build();
			}
		});

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public CustosModuleWidget getWidget() {
		return this.widget;
	}

	@Override
	public void updateWidget() {
		getWidget().updateWidget();
	}
}
