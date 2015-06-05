package view;

import java.awt.Color;

public class SimpleColorProvider implements ColorProvider {

	public SimpleColorProvider() {
		super();
	}

	@Override
	public Color backgroundColor() {
		return Color.WHITE;
	}

	@Override
	public Color foregroundColor() {
		return Color.BLACK;
	}

	@Override
	public Color shadowColor() {
		return Color.DARK_GRAY;
	}

	@Override
	public Color highlightColor() {
		return Color.YELLOW.darker();
	}

	@Override
	public Color warningColor() {
		return Color.RED;
	}

	@Override
	public Color everythingOkColor() {
		return Color.GREEN.darker();
	}

}
