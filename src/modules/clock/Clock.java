package modules.clock;

import controller.CustosSession;
import controller.HeartbeatEvent;
import edt.EDT;
import model.configuration.CustosModuleConfiguration;
import modules.CustosModuleWidget;
import modules.VisibleCustosModule;

public class Clock extends VisibleCustosModule {

	private final ClockWidget widget;
	private final CustosModuleConfiguration configuration;

	public Clock(final CustosSession session) {
		super(session);
		this.widget = new ClockWidget(colorProvider());
		this.configuration = new CustosModuleConfiguration.NoCustosModuleConfiguration();
	}

	@Override
	public void start() {
		logger().info("Starting clock");
		EDT.perform(new Runnable() {

			@Override
			public void run() {
				Clock.this.widget.build();
			}
		});
	}

	@Override
	public void stop() {
		logger().info("Clock stopped");
	}

	@Override
	public CustosModuleWidget getWidget() {
		return this.widget;
	}

	@Override
	public void receive(final HeartbeatEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public CustosModuleConfiguration getConfiguration() {
		return this.configuration;
	}

	@Override
	public void applyConfiguration() {
		// do nothing on purpose, since clock hs no configuration
	}
}
