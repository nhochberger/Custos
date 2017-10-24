package view;

import java.util.LinkedList;

import controller.CustosSession;
import controller.HeartbeatEvent;
import controller.SystemMessageMemory;
import hochberger.utilities.application.session.SessionBasedObject;
import hochberger.utilities.eventbus.EventReceiver;
import hochberger.utilities.gui.ApplicationGui;
import model.configuration.CustosConfiguration;
import modules.VisibleCustosModule;

public class CustosGui extends SessionBasedObject implements ApplicationGui, EventReceiver<HeartbeatEvent> {

	private final LinkedList<VisibleCustosModule> modules;
	private final ColorProvider colorProvider;
	private CustosMainFrame mainFrame;
	private final SystemMessageMemory messageMemory;
	private final CustosConfiguration custosConfiguration;

	public CustosGui(final CustosSession session, final SystemMessageMemory messageMemory, final CustosConfiguration custosConfiguration) {
		super(session);
		this.colorProvider = session.getColorProvider();
		this.messageMemory = messageMemory;
		this.custosConfiguration = custosConfiguration;
		this.modules = new LinkedList<>();
	}

	@Override
	public void activate() {
		logger().info("GUI activated");
		this.mainFrame = new CustosMainFrame(session(), this.colorProvider, this.messageMemory, this.custosConfiguration);
		for (final VisibleCustosModule module : this.modules) {
			this.mainFrame.addModuleToView(module);
		}
		this.mainFrame.show();
	}

	@Override
	public void deactivate() {
		this.mainFrame.hide();
		logger().info("GUI deactivated");
	}

	public void addModule(final VisibleCustosModule module) {
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
