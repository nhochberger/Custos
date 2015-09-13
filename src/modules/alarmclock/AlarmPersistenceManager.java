package modules.alarmclock;

import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AlarmPersistenceManager extends SessionBasedObject {

	public AlarmPersistenceManager(final BasicSession session) {
		super(session);

	}

	public void persistAlarms(final List<Alarm> alarms) {
		Gson gson = new GsonBuilder().create();
		String result = gson.toJson(alarms);
		System.err.println(result);
	}

	public List<Alarm> readAlarms() {
		return new LinkedList<>();
	}
}
