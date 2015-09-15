package modules.alarmclock;

import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;
import hochberger.utilities.files.Closer;
import hochberger.utilities.text.i18n.DirectI18N;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import controller.SystemMessage;
import controller.SystemMessage.MessageSeverity;

public class AlarmPersistenceManager extends SessionBasedObject {

	private static final String ALARM_FILE_NAME = "alarms.cfg";
	private static final String FILE_SEPARATOR = "/";
	private static final String USER_DIR = "user.dir";

	public AlarmPersistenceManager(final BasicSession session) {
		super(session);

	}

	public void persistAlarms(final List<Alarm> alarms) {
		final Gson gson = new GsonBuilder().create();
		final String serializedAlarms = gson.toJson(alarms);
		logger().debug("Serialized alarms:\n" + serializedAlarms);
		FileWriter writer = null;
		try {
			final File storeLocation = new File(storeLocationPath());
			storeLocation.createNewFile();
			writer = new FileWriter(storeLocation);
			writer.write(serializedAlarms);
			final String infoMessage = new DirectI18N("Alarms successfully saved to ${0}", storeLocationPath()).toString();
			logger().info(infoMessage);
			session().getEventBus().publish(new SystemMessage(MessageSeverity.SUCCESS, infoMessage));
		} catch (final IOException e) {
			final String errorMessage = new DirectI18N("Unable to store alarms to ${0}", storeLocationPath()).toString();
			logger().error(errorMessage);
			session().getEventBus().publish(new SystemMessage(MessageSeverity.SEVERE, errorMessage));
		} finally {
			Closer.close(writer);
		}
	}

	private String storeLocationPath() {
		final String userDirectoryPath = System.getProperty(USER_DIR);
		final String storeLocationPath = userDirectoryPath + FILE_SEPARATOR + ALARM_FILE_NAME;
		return storeLocationPath;
	}

	public List<Alarm> readAlarms() {
		LinkedList<Alarm> result = new LinkedList<>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File(storeLocationPath())));
			final String alarmsJson = reader.readLine();
			final Gson gson = new Gson();
			final Type listType = new TypeToken<LinkedList<Alarm>>() {
				private static final long serialVersionUID = 1L;
			}.getType();
			result = gson.fromJson(alarmsJson, listType);
			final String infoMessage = new DirectI18N("Alarms read from ${0}", storeLocationPath()).toString();
			logger().info(infoMessage);
			session().getEventBus().publish(new SystemMessage(MessageSeverity.SUCCESS, infoMessage));
		} catch (final IOException e) {
			final String errorMessage = new DirectI18N("Unable to store alarms to ${0}", storeLocationPath()).toString();
			logger().error(errorMessage);
			session().getEventBus().publish(new SystemMessage(MessageSeverity.SEVERE, errorMessage));
		} finally {
			Closer.close(reader);
		}
		return result;
	}
}
