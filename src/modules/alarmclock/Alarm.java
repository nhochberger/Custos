package modules.alarmclock;

import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;

public class Alarm {

	public enum Weekday {
		SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY;

		public static Weekday getWeekdayFor(final int day) {
			for (Weekday weekday : values()) {
				if (day == weekday.ordinal()) {
					return weekday;
				}
			}
			return null;
		}
	}

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

	public boolean checkTriggered() {
		DateTime now = DateTime.now();
		Weekday today = Weekday.getWeekdayFor(now.getDayOfWeek());
		return this.weekdayRepetition.contains(today) && this.alarmTime.applies(now);
	}
}
