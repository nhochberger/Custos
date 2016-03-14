package modules.alarmclock;

import hochberger.utilities.application.Lifecycle;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;
import hochberger.utilities.gui.dialog.BasicModalDialog;
import hochberger.utilities.gui.dialog.BasicModalDialog.DialogCloseListener;
import hochberger.utilities.gui.dialog.SelfClosingModalDialog;
import hochberger.utilities.text.CommonDateTimeFormatters;
import hochberger.utilities.text.Text;
import hochberger.utilities.text.i18n.DirectI18N;
import hochberger.utilities.threading.ThreadRunner;
import hochberger.utilities.timing.ToMilis;

import java.io.InputStream;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import org.joda.time.DateTime;

import controller.SystemMessage;
import controller.SystemMessage.MessageSeverity;
import edt.EDT;

public class TriggeredAlarm extends SessionBasedObject implements Lifecycle {

    private final InputStream alarmSoundStream;

    protected TriggeredAlarm(final BasicSession session, final InputStream alarmSoundStream) {
        super(session);
        this.alarmSoundStream = alarmSoundStream;
    }

    @Override
    public void start() {
        final DateTime now = DateTime.now();
        logger().info("Alarm triggered at " + CommonDateTimeFormatters.hourMinuteSecond().print(now));
        session().getEventBus().publish(new SystemMessage(MessageSeverity.NORMAL, new DirectI18N("Alarm triggered at ${0}", CommonDateTimeFormatters.hourMinute().print(now))));
        final BasicModalDialog dialog = new SelfClosingModalDialog(new DirectI18N("${0}:${1}", String.format("%02d", now.getHourOfDay()), String.format("%02d", now.getMinuteOfHour())),
                new DirectI18N(Text.empty()), new DirectI18N("Snooze"), new DirectI18N("Cancel"), ToMilis.minutes(15));
        try {
            final Player player = new Player(this.alarmSoundStream);
            dialog.addCloseListener(new DialogCloseListener() {

                @Override
                public void actionPerformed() {
                    player.close();
                }
            });
            ThreadRunner.startThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        player.play();
                    } catch (final JavaLayerException e) {
                        logger().error(e);
                    }
                }
            });
        } catch (final JavaLayerException e) {
            logger().error("Unable to load alarm sound.", e);
        }
        EDT.perform(new Runnable() {
            @Override
            public void run() {
                dialog.build();
                dialog.show();
            }
        });
    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub
    }
}
