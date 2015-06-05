package view;

import hochberger.utilities.gui.ApplicationGui;

import java.util.LinkedList;

import modules.CustosModule;

public class CustosGui implements ApplicationGui {

	private final String applicationTitle;
	private final LinkedList<CustosModule> modules;
	private final ColorProvider colorProvider;

	public CustosGui(final String applicationTitle, final ColorProvider colorProvider) {
		super();
		this.applicationTitle = applicationTitle;
		this.colorProvider = colorProvider;
		this.modules = new LinkedList<>();
	}

	@Override
	public void activate() {
		CustosMainFrame mainFrame = new CustosMainFrame(this.applicationTitle, this.colorProvider);
		for (CustosModule module : this.modules) {
			mainFrame.addModuleToView(module);
		}
		mainFrame.show();
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub

	}

	public void addModule(final CustosModule module) {
		this.modules.add(module);
	}

}
