package modules.alarmclock;

import hochberger.utilities.text.i18n.DirectI18N;

import java.util.Comparator;

public enum Weekday {
	SUNDAY(new DirectI18N("Su")), MONDAY(new DirectI18N("Mo")), TUESDAY(new DirectI18N("Tu")), WEDNESDAY(new DirectI18N("We")), THURSDAY(new DirectI18N("Th")), FRIDAY(new DirectI18N("Fr")), SATURDAY(
			new DirectI18N("Sa"));

	public static Weekday getWeekdayFor(final int day) {
		for (Weekday weekday : values()) {
			if (day == weekday.ordinal()) {
				return weekday;
			}
		}
		return null;
	}

	private final String shortName;

	private Weekday(final DirectI18N shortName) {
		this.shortName = shortName.toString();
	}

	public String getShortName() {
		return this.shortName;
	}

	public static class WeekdayComparator implements Comparator<Weekday> {

		public WeekdayComparator() {
			super();
		}

		@Override
		public int compare(final Weekday arg0, final Weekday arg1) {
			if (null == arg0) {
				return -1;
			}
			if (null == arg1) {
				return 1;
			}
			return arg0.ordinal() - arg1.ordinal();
		}
	}
}
