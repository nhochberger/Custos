package controller;

import hochberger.utilities.application.ApplicationProperties;
import hochberger.utilities.application.BasicLoggedApplication;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.eventbus.SimpleEventBus;
import hochberger.utilities.text.i18n.DirectI18N;

import java.util.LinkedList;
import java.util.List;

import modules.CustosModule;
import modules.VisibleCustosModule;
import modules.alarmclock.AlarmClock;
import modules.clock.Clock;
import modules.weather.Weather;
import view.ColorProvider;
import view.CustosGui;
import view.DayTimeAwareColorProvider;
import controller.SystemMessage.MessageSeverity;

public class CustosApplication extends BasicLoggedApplication {

	private final Heartbeat heartbeat;
	private final ScreenSaverProhibiter screenSaverProhibiter;
	private final ColorProvider colorProvider;
	private final BasicSession session;
	private final CustosGui gui;
	private final List<CustosModule> modules;
	private final VersionChecker versionChecker;
	private final SystemMessageMemory systemMessageMemory;

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
		this.versionChecker = new VersionChecker(this.session);
		this.systemMessageMemory = new SystemMessageMemory();
		this.session.getEventBus().register(this.systemMessageMemory, SystemMessage.class);
		this.gui = new CustosGui(this.session, this.colorProvider, this.systemMessageMemory);
		this.session.getEventBus().register(this.gui, HeartbeatEvent.class);

		this.modules = new LinkedList<>();
		this.modules.add(new Clock(this.session, this.colorProvider));
		this.modules.add(new Weather(this.session, this.colorProvider));
		this.modules.add(new AlarmClock(this.session, this.colorProvider));
		for (final CustosModule custosModule : this.modules) {
			if (custosModule instanceof VisibleCustosModule) {
				this.gui.addModule((VisibleCustosModule) custosModule);
			}
		}
	}

	@Override
	public void start() {
		this.session.getEventBus().publish(new SystemMessage(MessageSeverity.SUCCESS, new DirectI18N("Starting application").toString()));
		super.start();
		for (final CustosModule custosModule : this.modules) {
			this.session.getEventBus().register(custosModule, HeartbeatEvent.class);
			custosModule.start();
		}
		this.gui.activate();
		this.screenSaverProhibiter.start();
		this.heartbeat.start();
		this.versionChecker.start();
		this.session.getEventBus().publish(new SystemMessage(MessageSeverity.SUCCESS, new DirectI18N("Custos ${0} successfully started.", this.session.getProperties().version()).toString()));
	}
}
