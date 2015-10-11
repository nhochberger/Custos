package controller;

import hochberger.utilities.application.Lifecycle;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.Timer;
import java.util.TimerTask;

import com.sun.glass.events.KeyEvent;

public class ScreenSaverProhibiter extends SessionBasedObject implements Lifecycle {

    private final Timer timer;

    public ScreenSaverProhibiter(final BasicSession session) {
        super(session);
        this.timer = new Timer();
    }

    @Override
    public void start() {
        this.timer.schedule(new ScreenSaverProhibiterTimerTask(), 10000, 10000);
    }

    @Override
    public void stop() {
        this.timer.cancel();
    }

    public class ScreenSaverProhibiterTimerTask extends TimerTask {

        public ScreenSaverProhibiterTimerTask() {
            super();
        }

        @Override
        public void run() {
            try {
                final Robot robot = new Robot();
                robot.keyPress(KeyEvent.VK_SCROLL_LOCK);
                Thread.sleep(50);
                robot.keyPress(KeyEvent.VK_SCROLL_LOCK);
            } catch (final AWTException | InterruptedException e) {
                logger().error("Error while trying to prevent screensaver.", e);
            }
        }
    }
}
