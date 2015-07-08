package view;

import hochberger.utilities.eventbus.EventReceiver;
import hochberger.utilities.gui.ApplicationGui;

import java.util.LinkedList;

import modules.CustosModule;
import controller.HeartbeatEvent;

public class CustosGui implements ApplicationGui, EventReceiver<HeartbeatEvent> {

	private final String applicationTitle;
	private final LinkedList<CustosModule> modules;
	private final ColorProvider colorProvider;
	private CustosMainFrame mainFrame;

	public CustosGui(final String applicationTitle, final ColorProvider colorProvider) {
		super();
		this.applicationTitle = applicationTitle;
		this.colorProvider = colorProvider;
		this.modules = new LinkedList<>();
	}

	@Override
	public void activate() {
		this.mainFrame = new CustosMainFrame(this.applicationTitle, this.colorProvider);
		for (final CustosModule module : this.modules) {
			this.mainFrame.addModuleToView(module);
		}
		this.mainFrame.show();
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub

	}

	public void addModule(final CustosModule module) {
		this.modules.add(module);
	}

	@Override
	public void receive(final HeartbeatEvent event) {
		if (null == this.mainFrame) {
			return;
		}
		this.mainFrame.update();
	}
}
