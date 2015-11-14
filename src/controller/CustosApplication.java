package controller;

import hochberger.utilities.application.ApplicationProperties;
import hochberger.utilities.application.ApplicationShutdownEvent;
import hochberger.utilities.application.ApplicationShutdownEventReceiver;
import hochberger.utilities.application.BasicLoggedApplication;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.eventbus.SimpleEventBus;
import hochberger.utilities.text.i18n.DirectI18N;
import hochberger.utilities.timing.Sleeper;
import hochberger.utilities.timing.ToMilis;

import java.util.LinkedList;
import java.util.List;

import model.configuration.CustosConfiguration;
import model.configuration.CustosModuleConfiguration;
import model.configuration.CustosModuleConfigurationEntry;
import modules.CustosModule;
import modules.VisibleCustosModule;
import modules.alarmclock.AlarmClock;
import modules.clock.Clock;
import modules.newsreader.NewsReader;
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
	private final CustosConfiguration custosConfiguration;

	public static void main(final String... args) {
		setUpLoggingServices(CustosApplication.class);
		try {
			final ApplicationProperties applicationProperties = new ApplicationProperties();
			final CustosApplication application = new CustosApplication(applicationProperties);
			application.start();
		} catch (final Exception e) {
			getLogger().fatal("Error while starting application. Shutting down.", e);
			System.exit(0);
		}
	}

	public CustosApplication(final ApplicationProperties applicationProperties) {
		super();
		this.session = new BasicSession(applicationProperties, new SimpleEventBus(), getLogger());
		this.custosConfiguration = new CustosConfiguration(this.session);
		this.custosConfiguration.load();
		this.colorProvider = new DayTimeAwareColorProvider(this.session);
		this.heartbeat = new Heartbeat(this.session);
		this.screenSaverProhibiter = new ScreenSaverProhibiter(this.session);
		this.versionChecker = new VersionChecker(this.session);
		this.systemMessageMemory = new SystemMessageMemory();
		this.session.getEventBus().register(this.systemMessageMemory, SystemMessage.class);
		this.gui = new CustosGui(this.session, this.colorProvider, this.systemMessageMemory, this.custosConfiguration);
		this.session.getEventBus().register(this.gui, HeartbeatEvent.class);
		this.modules = new LinkedList<>();

		setUpModules();

		final UpdateConfigurationEventHandler updateConfigurationEventHandler = new UpdateConfigurationEventHandler(this.modules);
		this.session.getEventBus().register(updateConfigurationEventHandler, UpdateConfigurationEvent.class);
		for (final CustosModule custosModule : this.modules) {
			final CustosModuleConfiguration currentModuleConfiguration = custosModule.getConfiguration();
			this.custosConfiguration.addModuleConfiguration(currentModuleConfiguration);
			for (final CustosModuleConfigurationEntry entry : currentModuleConfiguration.getConfigurationEntries().values()) {
				entry.setValue(this.custosConfiguration.getValueFor(entry.getKey(), String.valueOf(entry.getValue())));
			}
			custosModule.applyConfiguration();
			if (custosModule instanceof VisibleCustosModule) {
				this.gui.addModule((VisibleCustosModule) custosModule);
			}
		}
	}

	private void setUpModules() {
		logger().info("Setting up modules");
		this.modules.add(new Clock(this.session, this.colorProvider));
		this.modules.add(new Weather(this.session, this.colorProvider));
		this.modules.add(new AlarmClock(this.session, this.colorProvider));
		this.modules.add(new NewsReader(this.session, this.colorProvider));
	}

	@Override
	public void start() {
		super.start();
		this.session.getEventBus().register(new ApplicationShutdownEventReceiver(this.session, this), ApplicationShutdownEvent.class);
		this.session.getEventBus().publish(new SystemMessage(MessageSeverity.SUCCESS, new DirectI18N("Starting application").toString()));
		for (final CustosModule custosModule : this.modules) {
			this.session.getEventBus().register(custosModule, HeartbeatEvent.class);
			custosModule.start();
		}
		this.gui.activate();
		this.screenSaverProhibiter.start();
		this.heartbeat.start();
		this.versionChecker.start();
		final String startSuccessfulMessage = new DirectI18N("Custos ${0} successfully started.", this.session.getProperties().version()).toString();
		logger().info(startSuccessfulMessage);
		this.session.getEventBus().publish(new SystemMessage(MessageSeverity.SUCCESS, startSuccessfulMessage));
	}

	@Override
	public void stop() {
		logger().info("Custos is shutting down.");
		this.session.getEventBus().publish(new SystemMessage(MessageSeverity.NORMAL, new DirectI18N("Custos is shutting down. Please wait.").toString()));
		Sleeper.sleep(ToMilis.seconds(0.75));
		this.gui.deactivate();
		this.heartbeat.stop();
		for (final CustosModule custosModule : this.modules) {
			custosModule.stop();
		}
		this.versionChecker.stop();
		this.screenSaverProhibiter.stop();
		Sleeper.sleep(ToMilis.seconds(2));
		logger().info("Threads stopped.");
		super.stop();
	}
}
