package modules.alarmclock;

import hochberger.utilities.text.Text;

import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;

public class Alarm {

	private AlarmTime alarmTime;
	private final List<Weekday> weekdayRepetition;

	public Alarm() {
		super();
		this.weekdayRepetition = new LinkedList<>();
	}

	public void setRepetitionForWeekDay(final Weekday day) {
		this.weekdayRepetition.add(day);
	}

	public void unsetRepetitionForWeekDay(final Weekday day) {
		this.weekdayRepetition.remove(day);
	}

	public boolean getRepetitionFor(final Weekday day) {
		return this.weekdayRepetition.contains(day);
	}

	public void setAlarmTime(final AlarmTime alarmTime) {
		this.alarmTime = alarmTime;
	}

	public boolean checkTriggered() {
		DateTime now = DateTime.now();
		Weekday today = Weekday.getWeekdayFor(now.getDayOfWeek());
		return this.weekdayRepetition.contains(today) && this.alarmTime.applies(now);
	}

	@Override
	public String toString() {
		return this.alarmTime.toString() + Text.space() + Text.fromIterable(this.weekdayRepetition);
	}
}
