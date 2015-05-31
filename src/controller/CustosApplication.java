package controller;

import hochberger.utilities.application.ApplicationProperties;
import hochberger.utilities.application.BasicLoggedApplication;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.eventbus.SimpleEventBus;
import modules.clock.Clock;
import view.CustosGui;

public class CustosApplication extends BasicLoggedApplication {

	public static void main(final String... args) {
		setUpLoggingServices(CustosApplication.class);
		try {
			ApplicationProperties applicationProperties = new ApplicationProperties();
			BasicSession session = new BasicSession(applicationProperties, new SimpleEventBus(), getLogger());
			CustosGui gui = new CustosGui(applicationProperties.title());

			Clock clock = new Clock(session);

			clock.start();
			gui.addModule(clock);
			gui.activate();
		} catch (Exception e) {
			getLogger().fatal("Error while starting application. Shutting down.", e);
		}
	}

}
