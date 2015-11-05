package controller;

import hochberger.utilities.application.Lifecycle;
import hochberger.utilities.application.VersionComparator;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;
import hochberger.utilities.files.Closer;
import hochberger.utilities.text.i18n.DirectI18N;
import hochberger.utilities.timing.ToMilis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import sun.net.www.protocol.http.HttpURLConnection;
import controller.SystemMessage.MessageSeverity;

public class VersionChecker extends SessionBasedObject implements Lifecycle {

    private final Timer timer;

    public VersionChecker(final BasicSession session) {
        super(session);
        this.timer = new Timer();
    }

    @Override
    public void start() {
        logger().info("Starting version checker");
        this.timer.schedule(new VersionCheckTimerTask(), ToMilis.seconds(5), ToMilis.hours(1));
    }

    @Override
    public void stop() {
        this.timer.cancel();
        logger().info("Version checker stopped");
    }

    public void compareVersions(final String newVersion) {
        final String oldVersion = session().getProperties().version();
        final VersionComparator comparator = new VersionComparator();
        SystemMessage event = new SystemMessage(MessageSeverity.SUCCESS, new DirectI18N("Application is up to date. Current version is ${0}.", oldVersion).toString());
        if (0 > comparator.compare(oldVersion, newVersion)) {
            event = new SystemMessage(MessageSeverity.WARNING, new DirectI18N("There is a new version available. Current version is ${0}. New version is ${1}.", oldVersion, newVersion).toString());
        }
        session().getEventBus().publish(event);
    }

    public class VersionCheckTimerTask extends TimerTask {

        public VersionCheckTimerTask() {
            super();
        }

        @Override
        public void run() {
            URL url;
            try {
                url = new URL(session().getProperties().otherProperty("application.version.url"));
                final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoOutput(true);
                connection.connect();

                final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                final StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                Closer.close(in);
                connection.disconnect();
                compareVersions(response.toString());
            } catch (final IOException e) {
                session().getEventBus().publish(new SystemMessage(MessageSeverity.WARNING, new DirectI18N("Unable to retrieve version information.").toString()));
                logger().error("Unable to retrieve version information.", e);

            }
        }
    }
}
