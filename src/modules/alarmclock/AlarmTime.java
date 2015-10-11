package modules.alarmclock;

import org.joda.time.DateTime;

public class AlarmTime {

    private final int hour;
    private final int minute;

    public AlarmTime(final int hour, final int minute) {
        super();
        this.hour = hour;
        this.minute = minute;
    }

    public boolean applies(final DateTime time) {
        return (this.minute == time.getMinuteOfHour() && this.hour == time.getHourOfDay() && 0 == time.getSecondOfMinute());
    }

    public int getHour() {
        return this.hour;
    }

    public int getMinute() {
        return this.minute;
    }

    @Override
    public String toString() {
        return String.format("%02d", this.hour) + ":" + String.format("%02d", this.minute);
    }
}
