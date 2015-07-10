package view;

import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.gui.UndecoratedEDTSafeFrame;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import modules.CustosModule;
import net.miginfocom.swing.MigLayout;
import controller.SystemMessage;

public class CustosMainFrame extends UndecoratedEDTSafeFrame {

	private final List<CustosModule> modules;
	private final ColorProvider colorProvider;
	private final SystemMessageLabel systemMessageLabel;

	public CustosMainFrame(final BasicSession session, final ColorProvider colorProvider) {
		super(session.getProperties().title());
		this.colorProvider = colorProvider;
		this.modules = new CopyOnWriteArrayList<>();
		this.systemMessageLabel = new SystemMessageLabel(colorProvider);
		session.getEventBus().register(this.systemMessageLabel, SystemMessage.class);
	}

	@Override
	protected void buildUI() {
		exitOnClose();
		center();
		frame().getContentPane().setBackground(this.colorProvider.backgroundColor());
		useLayoutManager(new MigLayout("", "20[200]:push[200]:push[200]20", "20[]push"));
		frame().setAlwaysOnTop(true);
		this.systemMessageLabel.build();
		for (final CustosModule module : this.modules) {
			add(module.getWidget().getComponent());
		}
		add(this.systemMessageLabel.getLabel(), "dock south, gapleft 5, gapright 5, gapbottom 5");
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
