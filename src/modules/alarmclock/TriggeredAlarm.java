package modules.alarmclock;

import hochberger.utilities.application.Lifecycle;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;
import hochberger.utilities.gui.dialog.BasicModalDialog;
import hochberger.utilities.text.Text;
import hochberger.utilities.text.i18n.DirectI18N;

import org.joda.time.DateTime;

public class TriggeredAlarm extends SessionBasedObject implements Lifecycle {

    private final boolean alarmActive;

    protected TriggeredAlarm(final BasicSession session) {
        super(session);
        this.alarmActive = false;
    }

    @Override
    public void start() {
        if (this.alarmActive) {
            logger().info("Alarm is active. New Alarm is skipped.");
            return;
        }
        final DateTime now = DateTime.now();
        final BasicModalDialog dialog = new BasicModalDialog(new DirectI18N(
                "${0}:${1}", String.valueOf(now.getHourOfDay()),
                String.valueOf(now.getMinuteOfHour())), new DirectI18N(
                Text.empty()), new DirectI18N("Snooze"), new DirectI18N(
                "Cancel"));
        dialog.build();
        dialog.show();
        // ThreadRunner.startThread(new Runnable() {
        //
        // @Override
        // public void run() {
        // try {
        // TriggeredAlarm.this.alarmActive = true;
        // final Player player = new Player(ResourceLoader
        // .loadAsStream("modules/alarmclock/alarm.mp3"));
        // player.play();
        // TriggeredAlarm.this.alarmActive = false;
        // } catch (final JavaLayerException e) {
        // logger().error(e);
        // }
        // }
        // });
    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub
    }
}
