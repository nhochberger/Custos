package view;

import hochberger.utilities.gui.ApplicationGui;

import java.util.LinkedList;

import modules.CustosModule;

public class CustosGui implements ApplicationGui {

	private final String applicationTitle;
	private final LinkedList<CustosModule> modules;

	public CustosGui(final String applicationTitle) {
		super();
		this.applicationTitle = applicationTitle;
		this.modules = new LinkedList<>();
	}

	@Override
	public void activate() {
		CustosMainFrame mainFrame = new CustosMainFrame(this.applicationTitle);
		for (CustosModule module : this.modules) {
			mainFrame.add(module.getWidget());
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
