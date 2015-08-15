package modules.alarmclock;

import hochberger.utilities.gui.WrappedComponent;

import javax.swing.JPanel;

import view.ColorProvider;

public class WeekdayToggleButton extends WrappedComponent<JPanel> {

	private final Weekday day;
	private final ColorProvider colorProvider;

	public WeekdayToggleButton(final Weekday day, final ColorProvider colorProvider) {
		super();
		this.day = day;
		this.colorProvider = colorProvider;
	}

	@Override
	protected void buildComponent() {

	}
}
