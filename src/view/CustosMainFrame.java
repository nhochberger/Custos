package view;

import hochberger.utilities.gui.UndecoratedEDTSafeFrame;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JComponent;

import modules.CustosModule;
import net.miginfocom.swing.MigLayout;

public class CustosMainFrame extends UndecoratedEDTSafeFrame {

	private final List<CustosModule> widgets;
	private final ColorProvider colorProvider;

	public CustosMainFrame(final String title, final ColorProvider colorProvider) {
		super(title);
		this.colorProvider = colorProvider;
		this.widgets = new CopyOnWriteArrayList<>();
	}

	@Override
	protected void buildUI() {
		exitOnClose();
		center();
		useLayoutManager(new MigLayout("", "", "25[]:[]:[]:[]25"));
		frame().getContentPane().setBackground(this.colorProvider.backgroundColor());
		frame().setAlwaysOnTop(true);
		for (CustosModule module : this.widgets) {
			JComponent widget = module.getWidget();
			add(widget);
		}
		maximize();
	}

	public void addModuleToView(final CustosModule module) {
		this.widgets.add(module);
	}
}
