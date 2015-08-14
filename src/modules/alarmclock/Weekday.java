package modules.alarmclock;

import hochberger.utilities.text.i18n.DirectI18N;

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
}
