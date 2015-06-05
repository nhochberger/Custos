package view;

import hochberger.utilities.gui.UndecoratedEDTSafeFrame;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import modules.CustosModule;
import net.miginfocom.swing.MigLayout;

public class CustosMainFrame extends UndecoratedEDTSafeFrame {

	private final List<CustosModule> modules;
	private final ColorProvider colorProvider;

	public CustosMainFrame(final String title, final ColorProvider colorProvider) {
		super(title);
		this.colorProvider = colorProvider;
		this.modules = new CopyOnWriteArrayList<>();
	}

	@Override
	protected void buildUI() {
		exitOnClose();
		center();
		useLayoutManager(new MigLayout("", "", "25[]:[]:[]:[]25"));
		frame().getContentPane().setBackground(this.colorProvider.backgroundColor());
		frame().setAlwaysOnTop(true);
		for (CustosModule module : this.modules) {
			add(module.getWidget().getComponent());
		}
		maximize();
	}

	public void addModuleToView(final CustosModule module) {
		this.modules.add(module);
	}

	public void update() {
		frame().getContentPane().setBackground(this.colorProvider.backgroundColor());
		for (CustosModule custosModule : this.modules) {
			custosModule.updateWidget();
		}
	}
}
