package view;

import hochberger.utilities.gui.UndecoratedEDTSafeFrame;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JComponent;

import net.miginfocom.swing.MigLayout;

public class CustosMainFrame extends UndecoratedEDTSafeFrame {

	private final List<JComponent> widgets;

	public CustosMainFrame(final String title) {
		super(title);
		this.widgets = new CopyOnWriteArrayList<>();
	}

	@Override
	protected synchronized void buildUI() {
		exitOnClose();
		center();
		useLayoutManager(new MigLayout());
		frame().setAlwaysOnTop(true);
		getContentPane().setBackground(Color.DARK_GRAY);
		for (JComponent widget : this.widgets) {
			add(widget);
		}
		maximize();
	}

	@Override
	public synchronized void add(final JComponent component) {
		this.widgets.add(component);
	}
}
