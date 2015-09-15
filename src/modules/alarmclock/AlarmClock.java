package modules.alarmclock;

import hochberger.utilities.application.ResourceLoader;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.eventbus.EventReceiver;
import hochberger.utilities.threading.ThreadRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import modules.CustosModuleWidget;
import modules.VisibleCustosModule;
import view.ColorProvider;
import controller.HeartbeatEvent;

public class AlarmClock extends VisibleCustosModule {

	private final List<Alarm> alarms;
	private final AlarmClockWidget widget;
	private final AlarmPersistenceManager persistenceManager;

	public AlarmClock(final BasicSession session, final ColorProvider colorProvider) {
		super(session, colorProvider);
		this.alarms = new LinkedList<>();
		this.widget = new AlarmClockWidget(session.getEventBus(), colorProvider, this.alarms);
		this.persistenceManager = new AlarmPersistenceManager(session);
	}

	@Override
	public CustosModuleWidget getWidget() {
		return this.widget;
	}

	@Override
	public void updateWidget() {
		// do not use by now!
		// Currently breaks widget's behavior
	}

	@Override
	public void start() {
		this.alarms.addAll(this.persistenceManager.readAlarms());
		this.widget.build();
		session().getEventBus().register(new NewAlarmHandler(), NewAlarmEvent.class);
		session().getEventBus().register(new DeleteAlarmHandler(), DeleteAlarmEvent.class);
		session().getEventBus().register(new EditAlarmHander(), AlarmEditedEvent.class);
	}

	@Override
	public void stop() {
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
				alarmDue = true;
			}
		}
		return alarmDue;
	}

	private void triggerAlarm() {
		ThreadRunner.startThread(new Runnable() {

			@Override
			public void run() {
				try {
					final Player player = new Player(new FileInputStream(ResourceLoader.loadFile("modules/alarmclock/alarm.mp3")));
					player.play();
				} catch (FileNotFoundException | JavaLayerException e) {
					logger().error(e);
				}
			}
		});
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
			getWidget().updateWidget();
			persistAlarms();
		}
	}
}
