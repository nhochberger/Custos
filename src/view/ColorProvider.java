package view;

import java.awt.Color;

public interface ColorProvider {

	public Color backgroundColor();

	public Color foregroundColor();

	public Color shadowColor();

	public Color highlightColor();

	public Color warningColor();

	public Color everythingOkColor();
}
