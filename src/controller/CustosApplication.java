package controller;

import hochberger.utilities.application.ApplicationProperties;
import hochberger.utilities.application.BasicLoggedApplication;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.eventbus.SimpleEventBus;
import modules.clock.Clock;
import view.ColorProvider;
import view.CustosGui;
import view.SimpleColorProvider;

public class CustosApplication extends BasicLoggedApplication {

	public static void main(final String... args) {
		setUpLoggingServices(CustosApplication.class);
		try {
			ApplicationProperties applicationProperties = new ApplicationProperties();
			BasicSession session = new BasicSession(applicationProperties, new SimpleEventBus(), getLogger());

			ColorProvider colorProvider = new SimpleColorProvider();

			CustosGui gui = new CustosGui(applicationProperties.title(), colorProvider);

			Clock clock = new Clock(session, colorProvider);

			clock.start();
			gui.addModule(clock);
			gui.activate();
		} catch (Exception e) {
			getLogger().fatal("Error while starting application. Shutting down.", e);
		}
	}

}
