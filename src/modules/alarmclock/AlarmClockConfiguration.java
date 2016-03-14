package modules.alarmclock;

import hochberger.utilities.application.ResourceLoader;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.text.i18n.DirectI18N;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import model.configuration.CustosModuleConfiguration;
import model.configuration.CustosModuleFileConfigurationEntry;

public class AlarmClockConfiguration extends CustosModuleConfiguration {

    private static final String ALARM_FILE = "alarm.file";

    private final CustosModuleFileConfigurationEntry configuredFile;

    private final BasicSession session;

    public AlarmClockConfiguration(final BasicSession session) {
        super(new DirectI18N("Alarm Clock"));
        this.session = session;
        this.configuredFile = new CustosModuleFileConfigurationEntry(new DirectI18N("Alarm sound file"), new DirectI18N("Sound file to be played when an alrm is triggered."), ALARM_FILE, null);
        addConfigurationEntry(this.configuredFile);
    }

    public InputStream alarmSoundFileStream() {
        if (null == this.configuredFile.getValue() || !this.configuredFile.getValue().exists()) {
            this.session.getLogger().info("No alarm sound was configured or the configured file cannot be accessed. Using default alarm sound.");
            return ResourceLoader.loadAsStream("modules/alarmclock/alarm.mp3");
        }
        try {
            this.session.getLogger().debug("Using configured alarm sound: " + this.configuredFile.getValue().getAbsolutePath());
            return new FileInputStream(this.configuredFile.getValue());
        } catch (final FileNotFoundException e) {
            this.session.getLogger().error("File containing slarm sound not found", e);
            return ResourceLoader.loadAsStream("modules/alarmclock/alarm.mp3");
        }
    }
}
