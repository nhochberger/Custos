package view;

import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;

import java.awt.Color;

import org.joda.time.DateTime;

public class DayTimeAwareColorProvider extends SessionBasedObject implements ColorProvider {

	public DayTimeAwareColorProvider(final BasicSession session) {
		super(session);
	}

	@Override
	public Color backgroundColor() {
		if (isDayTime()) {
			return Color.WHITE;
		}
		return Color.BLACK;
	}

	@Override
	public Color foregroundColor() {
		if (isDayTime()) {
			return Color.BLACK.brighter();
		}
		return Color.RED.darker();
	}

	@Override
	public Color shadowColor() {
		if (isDayTime()) {
			return Color.LIGHT_GRAY;
		}
		return Color.DARK_GRAY.darker();
	}

	@Override
	public Color highlightColor() {
		if (isDayTime()) {
			return Color.YELLOW.darker();
		}
		return Color.YELLOW.darker().darker();
	}

	@Override
	public Color warningColor() {
		if (isDayTime()) {
			return Color.RED;
		}
		return Color.WHITE;
	}

	@Override
	public Color everythingOkColor() {
		if (isDayTime()) {
			return Color.GREEN.darker();
		}
		return Color.GREEN.darker().darker();
	}

	protected boolean isDayTime() {
		final DateTime time = DateTime.now();
		return 7 <= time.hourOfDay().get() && time.hourOfDay().get() < 22;
	}

}
