package modules.alarmclock;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import controller.CustosSession;
import controller.HeartbeatEvent;
import hochberger.utilities.eventbus.EventReceiver;
import model.configuration.CustosModuleConfiguration;
import modules.CustosModuleWidget;
import modules.VisibleCustosModule;
import view.ColorProvider;

public class AlarmClock extends VisibleCustosModule {

	private final List<Alarm> alarms;
	private final AlarmClockWidget widget;
	private final AlarmPersistenceManager persistenceManager;
	private final boolean alarmActive;
	private final AlarmClockConfiguration configuration;
	// HACK
	private Color lastForegroundColor;

	public AlarmClock(final CustosSession session, final ColorProvider colorProvider) {
		super(session, colorProvider);
		this.alarms = new LinkedList<>();
		this.widget = new AlarmClockWidget(session.getEventBus(), colorProvider, this.alarms);
		this.persistenceManager = new AlarmPersistenceManager(session);
		this.alarmActive = false;
		this.configuration = new AlarmClockConfiguration(session);
		this.lastForegroundColor = colorProvider.foregroundColor();
	}

	@Override
	public CustosModuleWidget getWidget() {
		return this.widget;
	}

	@Override
	public void updateWidget() {
		// HACK
		if (this.lastForegroundColor.equals(colorProvider().foregroundColor())) {
			return;
		}
		this.lastForegroundColor = colorProvider().foregroundColor();
		super.updateWidget();
	}

	@Override
	public void start() {
		logger().info("Starting AlarmClock");
		this.alarms.addAll(this.persistenceManager.readAlarms());
		this.widget.build();
		session().getEventBus().register(new NewAlarmHandler(), NewAlarmEvent.class);
		session().getEventBus().register(new DeleteAlarmHandler(), DeleteAlarmEvent.class);
		session().getEventBus().register(new EditAlarmHander(), AlarmEditedEvent.class);
	}

	@Override
	public void stop() {
		logger().info("AlarmClock stopped");
	}

	@Override
	public void receive(final HeartbeatEvent event) {
		final boolean alarmDue = checkDueAlarms();
		if (alarmDue) {
			triggerAlarm();
		}
	}

	private boolean checkDueAlarms() {
		boolean alarmDue = false;
		for (final Alarm alarm : this.alarms) {
			if (alarm.checkTriggered()) {
				logger().info("Alarm " + alarm + " is due");
				alarmDue = true;
			}
		}
		return alarmDue;
	}

	private void triggerAlarm() {
		if (this.alarmActive) {
			logger().info("Alarm is active. New Alarm is skipped.");
			return;
		}
		final TriggeredAlarm triggeredAlarm = new TriggeredAlarm(session(), this.configuration.alarmSoundFileStream());
		triggeredAlarm.start();
	}

	@Override
	public CustosModuleConfiguration getConfiguration() {
		return this.configuration;
	}

	private void persistAlarms() {
		this.persistenceManager.persistAlarms(this.alarms);
	}

	private final class NewAlarmHandler implements EventReceiver<NewAlarmEvent> {

		public NewAlarmHandler() {
			super();
		}

		@Override
		public void receive(final NewAlarmEvent event) {
			logger().info("New alarm added: " + event.getAlarm());
			AlarmClock.this.alarms.add(event.getAlarm());
			getWidget().updateWidget();
			persistAlarms();
		}
	}

	private final class DeleteAlarmHandler implements EventReceiver<DeleteAlarmEvent> {

		public DeleteAlarmHandler() {
			super();
		}

		@Override
		public void receive(final DeleteAlarmEvent event) {
			logger().info("Alarm was deleted: " + event.getAlarm());
			AlarmClock.this.alarms.remove(event.getAlarm());
			getWidget().updateWidget();
			persistAlarms();
		}
	}

	private final class EditAlarmHander implements EventReceiver<AlarmEditedEvent> {

		public EditAlarmHander() {
			super();
		}

		@Override
		public void receive(final AlarmEditedEvent event) {
			logger().info("Alarm was edited");
			getWidget().updateWidget();
			persistAlarms();
		}
	}

	@Override
	public void applyConfiguration() {
		// do nothing on purpose, since AlrmClock has no configuration yet
	}
}
