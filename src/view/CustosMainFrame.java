package view;

import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.gui.UndecoratedEDTSafeFrame;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import modules.CustosModule;
import net.miginfocom.swing.MigLayout;

public class CustosMainFrame extends UndecoratedEDTSafeFrame {

	private final List<CustosModule> modules;
	private final ColorProvider colorProvider;

	public CustosMainFrame(final BasicSession session, final ColorProvider colorProvider) {
		super(session.getProperties().title());
		this.colorProvider = colorProvider;
		this.modules = new CopyOnWriteArrayList<>();
	}

	@Override
	protected void buildUI() {
		exitOnClose();
		center();
		frame().getContentPane().setBackground(this.colorProvider.backgroundColor());
		useLayoutManager(new MigLayout("", "20[200]:push[200]:push[200]20", "20[]20"));
		frame().setAlwaysOnTop(true);
		for (final CustosModule module : this.modules) {
			add(module.getWidget().getComponent());
		}
		maximize();
	}

	public void addModuleToView(final CustosModule module) {
		this.modules.add(module);
	}

	public void update() {
		if (!isBuilt()) {
			return;
		}
		frame().getContentPane().setBackground(this.colorProvider.backgroundColor());
		for (final CustosModule custosModule : this.modules) {
			custosModule.updateWidget();
		}
	}
}
