package model.configuration;

import hochberger.utilities.text.i18n.DirectI18N;
import hochberger.utilities.text.i18n.I18N;

public enum WeekdayName {

	SUNDAY("Sunday"), MONDAY("Monday"), TUESDAY("Tuesday"), WEDNESDAY("Wednesday"), THURSDAY("Thursday"), FRIDAY("Friday"), SATURDAY("Saturday");

	private final String textualRepresentation;

	private WeekdayName(final String textualRepresentation) {
		this.textualRepresentation = textualRepresentation;
	}

	public static I18N getNameFor(final int i) {
		String name = "Unknown";
		if (SUNDAY.ordinal() <= i && i <= FRIDAY.ordinal()) {
			name = values()[i].textualRepresentation;
		}
		return new DirectI18N(name);
	}

	public I18N getName() {
		return new DirectI18N(this.textualRepresentation);
	}
}
