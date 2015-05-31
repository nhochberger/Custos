package view;

import hochberger.utilities.gui.UndecoratedEDTSafeFrame;

import java.awt.Color;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JComponent;

import net.miginfocom.swing.MigLayout;

public class CustosMainFrame extends UndecoratedEDTSafeFrame {

	private final ConcurrentLinkedQueue<JComponent> widgets;

	public CustosMainFrame(final String title) {
		super(title);
		this.widgets = new ConcurrentLinkedQueue();
	}

	@Override
	protected void buildUI() {
		exitOnClose();
		center();
		useLayoutManager(new MigLayout());
		frame().setAlwaysOnTop(true);
		getContentPane().setBackground(Color.DARK_GRAY);
		System.err.println(this.widgets.size());
		for (JComponent widget : this.widgets) {
			add(widget);
		}
		maximize();
	}

	@Override
	public void add(final JComponent component) {
		this.widgets.add(component);
	}
}
