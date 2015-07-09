package controller;

import hochberger.utilities.application.ApplicationProperties;
import hochberger.utilities.application.BasicLoggedApplication;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.eventbus.SimpleEventBus;

import java.util.LinkedList;
import java.util.List;

import modules.CustosModule;
import modules.clock.Clock;
import modules.weather.Weather;
import view.ColorProvider;
import view.CustosGui;
import view.DayTimeAwareColorProvider;

public class CustosApplication extends BasicLoggedApplication {

	private final Heartbeat heartbeat;
	private final ScreenSaverProhibiter screenSaverProhibiter;
	private final ColorProvider colorProvider;
	private final BasicSession session;
	private final CustosGui gui;
	private final List<CustosModule> modules;

	public static void main(final String... args) {
		setUpLoggingServices(CustosApplication.class);
		try {
			final ApplicationProperties applicationProperties = new ApplicationProperties();
			final CustosApplication application = new CustosApplication(applicationProperties);
			application.start();
		} catch (final Exception e) {
			getLogger().fatal("Error while starting application. Shutting down.", e);
		}
	}

	public CustosApplication(final ApplicationProperties applicationProperties) {
		super();
		this.session = new BasicSession(applicationProperties, new SimpleEventBus(), getLogger());
		this.colorProvider = new DayTimeAwareColorProvider(this.session);
		this.heartbeat = new Heartbeat(this.session);
		this.screenSaverProhibiter = new ScreenSaverProhibiter(this.session);
		this.gui = new CustosGui(this.session, this.colorProvider);
		this.session.getEventBus().register(this.gui, HeartbeatEvent.class);

		this.modules = new LinkedList<>();
		this.modules.add(new Clock(this.session, this.colorProvider));
		this.modules.add(new Weather(this.session, this.colorProvider));
		for (final CustosModule custosModule : this.modules) {
			this.gui.addModule(custosModule);
		}
	}

	@Override
	public void start() {
		super.start();
		for (final CustosModule custosModule : this.modules) {
			custosModule.start();
		}
		this.gui.activate();
		this.screenSaverProhibiter.start();
		this.heartbeat.start();
	}
}
