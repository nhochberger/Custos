package view;

import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;
import hochberger.utilities.eventbus.EventReceiver;
import hochberger.utilities.gui.ApplicationGui;

import java.util.LinkedList;

import modules.CustosModule;
import controller.HeartbeatEvent;

public class CustosGui extends SessionBasedObject implements ApplicationGui, EventReceiver<HeartbeatEvent> {

	private final LinkedList<CustosModule> modules;
	private final ColorProvider colorProvider;
	private CustosMainFrame mainFrame;

	public CustosGui(final BasicSession session, final ColorProvider colorProvider) {
		super(session);
		this.colorProvider = colorProvider;
		this.modules = new LinkedList<>();
	}

	@Override
	public void activate() {
		this.mainFrame = new CustosMainFrame(session(), this.colorProvider);
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
