package modules.alarmclock;

import hochberger.utilities.text.Text;

import java.util.Set;
import java.util.TreeSet;

import org.joda.time.DateTime;

public class Alarm {

    private AlarmTime alarmTime;
    private final Set<Weekday> weekdayRepetition;
    private boolean active;

    public Alarm() {
        super();
        this.weekdayRepetition = new TreeSet<>(new Weekday.WeekdayComparator());
        this.active = true;
    }

    public AlarmTime getAlarmTime() {
        return this.alarmTime;
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

    public void setRepetitionForWeekday(final Weekday day, final boolean repeat) {
        if (repeat) {
            this.weekdayRepetition.add(day);
        } else {
            this.weekdayRepetition.remove(day);
        }
    }

    public void setAlarmTime(final AlarmTime alarmTime) {
        this.alarmTime = alarmTime;
    }

    public boolean checkTriggered() {
        DateTime now = DateTime.now();
        // HACK: DateTime delivers dayOfWeek as Mo = 1, ... SU = 7.
        Weekday today = Weekday.getWeekdayFor(now.getDayOfWeek() % 7);
        return this.weekdayRepetition.contains(today) && this.alarmTime.applies(now);
    }

    public void setActive(final boolean isActive) {
        this.active = isActive;
    }

    public boolean getActive() {
        return this.active;
    }

    public Set<Weekday> getWeekdayRepetition() {
        return this.weekdayRepetition;
    }

    @Override
    public String toString() {
        return this.alarmTime.toString() + Text.space() + Text.fromIterable(this.weekdayRepetition, ", ");
    }

    public static class EmptyAlarm extends Alarm {

        public EmptyAlarm() {
            super();
            setAlarmTime(new AlarmTime(0, 0));
        }
    }
}
